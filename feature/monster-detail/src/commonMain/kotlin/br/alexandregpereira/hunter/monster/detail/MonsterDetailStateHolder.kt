/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnMonsterPageChanges
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Hide
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.detail.collectOnVisibilityChanges
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEvent
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.ADD_TO_FOLDER
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.CHANGE_TO_FEET
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.CHANGE_TO_METERS
import br.alexandregpereira.hunter.monster.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.monster.detail.domain.model.MonsterDetail
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.state.DefaultStateHolder
import br.alexandregpereira.hunter.state.ScopeManager
import br.alexandregpereira.hunter.state.StateHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class MonsterDetailStateHolder(
    private val getMonsterDetailUseCase: GetMonsterDetailUseCase,
    private val changeMonstersMeasurementUnitUseCase: ChangeMonstersMeasurementUnitUseCase,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val monsterLoreDetailEventDispatcher: MonsterLoreDetailEventDispatcher,
    private val folderInsertEventDispatcher: FolderInsertEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val initialState: MonsterDetailState = MonsterDetailState()
) : ScopeManager(), StateHolder<MonsterDetailState> {

    private val stateHolder = DefaultStateHolder(initialState)
    override val state: StateFlow<MonsterDetailState> = stateHolder.state

    private var monsterChangeDispatchEnabled = false

    private val monsterIndex: String
        get() = state.value.monsterIndex

    private val monsterIndexes: List<String>
        get() = state.value.monsterIndexes

    init {
        observeEvents()
        state.value.run {
            if (showDetail && monsters.isEmpty()) {
                getMonstersByInitialIndex(monsterIndex, monsterIndexes)
            }
        }
    }

    private fun observeEvents() {
        monsterDetailEventListener.collectOnVisibilityChanges { event ->
            when (event) {
                is Show -> {
                    getMonstersByInitialIndex(event.index, event.indexes)
                    setState { copy(showDetail = true) }
                }
                Hide -> setState { copy(showDetail = false) }
            }
        }.launchIn(scope)
    }

    private fun getMonstersByInitialIndex(monsterIndex: String, monsterIndexes: List<String>) {
        setState { initialState.copy(monsterIndexes = monsterIndexes) }
        onMonsterChanged(monsterIndex, scrolled = false)
        getMonsterDetail().collectDetail()
    }

    fun onMonsterChanged(monsterIndex: String, scrolled: Boolean = true) {
        if (scrolled && monsterIndex != this.monsterIndex) {
            monsterDetailEventDispatcher.dispatchEvent(OnMonsterPageChanges(monsterIndex))
        }
        setState { copy(monsterIndex = monsterIndex) }
    }

    fun onShowOptionsClicked() {
        setState { ShowOptions }
    }

    fun onShowOptionsClosed() {
        setState { HideOptions }
    }

    fun onOptionClicked(option: MonsterDetailOptionState) {
        setState { HideOptions }
        when (option) {
            ADD_TO_FOLDER -> folderInsertEventDispatcher.dispatchEvent(
                FolderInsertEvent.Show(monsterIndexes = listOf(monsterIndex))
            )
            CHANGE_TO_FEET -> {
                changeMeasurementUnit(MeasurementUnit.FEET)
            }
            CHANGE_TO_METERS -> {
                changeMeasurementUnit(MeasurementUnit.METER)
            }
        }
    }

    fun onSpellClicked(spellIndex: String) {
        spellDetailEventDispatcher.dispatchEvent(SpellDetailEvent.ShowSpell(spellIndex))
    }

    fun onLoreClicked(monsterIndex: String) {
        monsterLoreDetailEventDispatcher.dispatchEvent(MonsterLoreDetailEvent.Show(monsterIndex))
    }

    fun onClose() {
        monsterDetailEventDispatcher.dispatchEvent(Hide)
    }

    private fun getMonsterDetail(): Flow<MonsterDetail> {
        return getMonsterDetailUseCase(monsterIndex, indexes = monsterIndexes)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun changeMeasurementUnit(measurementUnit: MeasurementUnit) {
        changeMonstersMeasurementUnitUseCase(monsterIndex, measurementUnit)
            .flatMapLatest { getMonsterDetail() }
            .collectDetail()
    }

    private fun Flow<MonsterDetail>.collectDetail() {
        this.map {
            val measurementUnit = it.measurementUnit
            getState().complete(
                initialMonsterIndex = it.monsterIndexSelected,
                monsters = it.monsters,
                options = when (measurementUnit) {
                    MeasurementUnit.FEET -> listOf(ADD_TO_FOLDER, CHANGE_TO_METERS)
                    MeasurementUnit.METER -> listOf(ADD_TO_FOLDER, CHANGE_TO_FEET)
                }
            )
        }.flowOn(dispatcher)
            .catch {
                it.printStackTrace()
            }.onEach { state ->
                setState { state }
            }
            .onStart { monsterChangeDispatchEnabled = false }
            .onCompletion { monsterChangeDispatchEnabled = true }
            .launchIn(scope)
    }

    private suspend fun getState(): MonsterDetailState = withContext(Dispatchers.Main) {
        state.value
    }

    private fun setState(block: MonsterDetailState.() -> MonsterDetailState) {
        stateHolder.setState(block)
    }
}
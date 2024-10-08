/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.folder.detail

import br.alexandregpereira.hunter.domain.folder.GetMonstersByFolderUseCase
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEvent
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResult.OnVisibilityChanges
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnSaved
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResultListener
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.event.collectOnMonsterCompendiumChanges
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FolderDetailStateHolder internal constructor(
    private val stateRecovery: StateRecovery,
    private val getMonstersByFolder: GetMonstersByFolderUseCase,
    private val folderDetailEventManager: FolderDetailEventManager,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val folderInsertResultListener: FolderInsertResultListener,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: FolderDetailAnalytics,
) : UiModel<FolderDetailState>(stateRecovery.getState()) {

    init {
        observeEvents()
        observeFolderInsertResults()
        if (state.value.isOpen && state.value.monsters.isEmpty()) {
            loadMonsters(state.value.folderName)
        }
    }

    fun onItemClick(index: String) {
        analytics.trackItemClicked(index)
        monsterEventDispatcher.dispatchEvent(
            Show(
                index = index,
                indexes = state.value.monsters.map { it.index }
            )
        )
    }

    fun onItemLongClick(index: String) {
        analytics.trackItemLongClicked(index)
        folderPreviewEventDispatcher.dispatchEvent(FolderPreviewEvent.AddMonster(index))
    }

    fun onClose() {
        analytics.trackClose()
        setState { copy(isOpen = false).saveState(stateRecovery) }
        folderDetailEventManager.dispatchResult(OnVisibilityChanges(isShowing = false))
    }

    fun onScrollChanges(firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) {
        setState {
            copy(
                firstVisibleItemIndex = firstVisibleItemIndex,
                firstVisibleItemScrollOffset = firstVisibleItemScrollOffset
            )
        }
    }

    private fun loadMonsters(folderName: String) {
        getMonstersByFolder(folderName)
            .flowOn(dispatcher)
            .catch {
                analytics.logException(it)
            }
            .onEach { monsters ->
                analytics.trackMonstersLoaded(monsters)
                setState {
                    copy(monsters = monsters, folderName = folderName)
                        .saveState(stateRecovery)
                }
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        folderDetailEventManager.events.onEach { event ->
            when (event) {
                is FolderDetailEvent.Show -> {
                    analytics.trackShow()
                    setState { copy(isOpen = true).saveState(stateRecovery) }
                    folderDetailEventManager.dispatchResult(OnVisibilityChanges(isShowing = true))
                    loadMonsters(event.folderName)
                }
            }
        }.launchIn(scope)

        monsterEventDispatcher.collectOnMonsterCompendiumChanges {
            loadMonsters(state.value.folderName)
        }.launchIn(scope)
    }

    private fun observeFolderInsertResults() {
        folderInsertResultListener.result
            .filter { result ->
                result is OnSaved && state.value.isOpen
            }
            .onEach {
                loadMonsters(state.value.folderName)
            }
            .launchIn(scope)
    }
}

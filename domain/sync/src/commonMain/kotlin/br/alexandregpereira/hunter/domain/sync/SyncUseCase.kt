/*
 * Copyright 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.domain.sync

import br.alexandregpereira.hunter.domain.monster.lore.SyncMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.spell.SyncSpellsUseCase
import br.alexandregpereira.hunter.domain.usecase.SyncMonstersUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class SyncUseCase(
    private val syncMonsters: SyncMonstersUseCase,
    private val syncSpells: SyncSpellsUseCase,
    private val syncMonstersLoreUseCase: SyncMonstersLoreUseCase
) {

    operator fun invoke(): Flow<Unit> {
        return flow {
            coroutineScope {
                awaitAll(
                    async { syncMonsters().collect() },
                    async { syncSpells().collect() },
                    async { syncMonstersLoreUseCase().collect() },
                )
            }
            emit(Unit)
        }
    }
}
/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.remote

import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import kotlinx.coroutines.flow.Flow

interface MonsterRemoteDataSource {

    fun getMonsters(): Flow<List<MonsterDto>>

    fun insertMonsters(monsters: List<MonsterDto>): Flow<Unit>
}
/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.monster.remote

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

internal interface MonsterApi {

    @GET("monsters.json")
    suspend fun getMonsters(): List<MonsterDto>

    @GET("sources/{sourceAcronym}/monsters.json")
    suspend fun getMonsters(@Path("sourceAcronym") sourceAcronym: String): List<MonsterDto>

    @GET
    suspend fun getMonsterImages(@Url url: String): List<MonsterImageDto>
}
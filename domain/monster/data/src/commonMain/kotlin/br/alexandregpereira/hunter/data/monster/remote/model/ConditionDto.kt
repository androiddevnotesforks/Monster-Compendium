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

package br.alexandregpereira.hunter.data.monster.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConditionDto(
    @SerialName("index")
    val index: String,
    @SerialName("type")
    val type: ConditionTypeDto,
    @SerialName("name")
    val name: String
)

@Serializable
enum class ConditionTypeDto {
    @SerialName("BLINDED")
    BLINDED,
    @SerialName("CHARMED")
    CHARMED,
    @SerialName("DEAFENED")
    DEAFENED,
    @SerialName("EXHAUSTION")
    EXHAUSTION,
    @SerialName("FRIGHTENED")
    FRIGHTENED,
    @SerialName("GRAPPLED")
    GRAPPLED,
    @SerialName("PARALYZED")
    PARALYZED,
    @SerialName("PETRIFIED")
    PETRIFIED,
    @SerialName("POISONED")
    POISONED,
    @SerialName("PRONE")
    PRONE,
    @SerialName("RESTRAINED")
    RESTRAINED,
    @SerialName("STUNNED")
    STUNNED,
    @SerialName("UNCONSCIOUS")
    UNCONSCIOUS,
}

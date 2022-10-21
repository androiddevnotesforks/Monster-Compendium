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

package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import br.alexandregpereira.hunter.domain.model.MonsterImage

internal fun List<MonsterImageDto>.toDomain(): List<MonsterImage> {
    return this.map {
        MonsterImage(
            monsterIndex = it.monsterIndex,
            backgroundColor = it.backgroundColor.toDomain(),
            isHorizontalImage = it.isHorizontalImage,
            imageUrl = it.imageUrl
        )
    }
}
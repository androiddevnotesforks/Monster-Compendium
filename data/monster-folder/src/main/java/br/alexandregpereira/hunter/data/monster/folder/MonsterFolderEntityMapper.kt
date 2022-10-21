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

package br.alexandregpereira.hunter.data.monster.folder

import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderType

internal fun List<MonsterFolderCompleteEntity>.asDomain(): List<MonsterFolder> {
    return this.map { it.asDomain() }
}

internal fun MonsterFolderCompleteEntity.asDomain(): MonsterFolder {
    return MonsterFolder(
        name = monsterFolderEntity.folderName,
        monsters = monsters.asDomain()
    )
}

@JvmName("asDomainMonsterPreviewFolderEntity")
private fun List<MonsterEntity>.asDomain(): List<MonsterPreviewFolder> {
    return map {
        it.run {
            MonsterPreviewFolder(
                index = index,
                name = name,
                type = MonsterPreviewFolderType.valueOf(type),
                challengeRating = challengeRating,
                imageUrl = imageUrl,
                backgroundColorLight = backgroundColorLight,
                backgroundColorDark = backgroundColorDark,
                isHorizontalImage = isHorizontalImage,
            )
        }
    }
}
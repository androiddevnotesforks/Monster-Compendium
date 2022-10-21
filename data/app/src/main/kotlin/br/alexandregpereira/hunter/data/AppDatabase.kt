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

package br.alexandregpereira.hunter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.alexandregpereira.hunter.data.monster.folder.local.dao.MonsterFolderDao
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderEntity
import br.alexandregpereira.hunter.data.monster.local.dao.AbilityScoreDao
import br.alexandregpereira.hunter.data.monster.local.dao.ActionDao
import br.alexandregpereira.hunter.data.monster.local.dao.ConditionDao
import br.alexandregpereira.hunter.data.monster.local.dao.DamageDao
import br.alexandregpereira.hunter.data.monster.local.dao.DamageDiceDao
import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.local.dao.ReactionDao
import br.alexandregpereira.hunter.data.monster.local.dao.SavingThrowDao
import br.alexandregpereira.hunter.data.monster.local.dao.SkillDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpecialAbilityDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpeedDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpeedValueDao
import br.alexandregpereira.hunter.data.monster.local.entity.AbilityScoreEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ConditionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageVulnerabilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ReactionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SavingThrowEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SkillEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpecialAbilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedValueEntity
import br.alexandregpereira.hunter.data.monster.spell.local.dao.SpellUsageDao
import br.alexandregpereira.hunter.data.monster.spell.local.dao.SpellcastingDao
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellPreviewEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageSpellCrossRefEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingSpellUsageCrossRefEntity
import br.alexandregpereira.hunter.data.spell.local.dao.SpellDao
import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity

@Database(
    entities = [
        AbilityScoreEntity::class,
        ActionEntity::class,
        ConditionEntity::class,
        DamageVulnerabilityEntity::class,
        DamageResistanceEntity::class,
        DamageImmunityEntity::class,
        DamageDiceEntity::class,
        MonsterEntity::class,
        MonsterFolderEntity::class,
        ReactionEntity::class,
        SavingThrowEntity::class,
        SkillEntity::class,
        SpecialAbilityEntity::class,
        SpeedEntity::class,
        SpeedValueEntity::class,
        SpellEntity::class,
        SpellcastingEntity::class,
        SpellcastingSpellUsageCrossRefEntity::class,
        SpellUsageEntity::class,
        SpellUsageSpellCrossRefEntity::class,
        SpellPreviewEntity::class,
    ],
    version = 17,
    exportSchema = false
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun abilityScoreDao(): AbilityScoreDao
    abstract fun actionDao(): ActionDao
    abstract fun conditionDao(): ConditionDao
    abstract fun damageDao(): DamageDao
    abstract fun damageDiceDao(): DamageDiceDao
    abstract fun monsterDao(): MonsterDao
    abstract fun monsterFolderDao(): MonsterFolderDao
    abstract fun reactionDao(): ReactionDao
    abstract fun savingThrowDao(): SavingThrowDao
    abstract fun skillDao(): SkillDao
    abstract fun specialAbilityDao(): SpecialAbilityDao
    abstract fun speedDao(): SpeedDao
    abstract fun speedValueDao(): SpeedValueDao
    abstract fun spellcastingDao(): SpellcastingDao
    abstract fun spellDao(): SpellDao
    abstract fun spellUsageDao(): SpellUsageDao
}
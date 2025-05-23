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

package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntityStatus
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageSpellCrossRefEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingSpellUsageCrossRefEntity
import br.alexandregpereira.hunter.database.AbilityScoreQueries
import br.alexandregpereira.hunter.database.ActionQueries
import br.alexandregpereira.hunter.database.ConditionQueries
import br.alexandregpereira.hunter.database.DamageDiceQueries
import br.alexandregpereira.hunter.database.DamageImmunityQueries
import br.alexandregpereira.hunter.database.DamageResistanceQueries
import br.alexandregpereira.hunter.database.DamageVulnerabilityQueries
import br.alexandregpereira.hunter.database.LegendaryActionQueries
import br.alexandregpereira.hunter.database.MonsterQueries
import br.alexandregpereira.hunter.database.ReactionQueries
import br.alexandregpereira.hunter.database.SavingThrowQueries
import br.alexandregpereira.hunter.database.SkillQueries
import br.alexandregpereira.hunter.database.SpecialAbilityQueries
import br.alexandregpereira.hunter.database.SpeedQueries
import br.alexandregpereira.hunter.database.SpeedValueQueries
import br.alexandregpereira.hunter.database.SpellPreviewQueries
import br.alexandregpereira.hunter.database.SpellUsageQueries
import br.alexandregpereira.hunter.database.SpellUsageSpellCrossRefQueries
import br.alexandregpereira.hunter.database.SpellcastingQueries
import br.alexandregpereira.hunter.database.SpellcastingSpellUsageCrossRefQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import br.alexandregpereira.hunter.database.MonsterEntity as MonsterDatabaseEntity

internal class MonsterDaoImpl(
    private val monsterQueries: MonsterQueries,
    private val abilityScoreQueries: AbilityScoreQueries,
    private val actionQueries: ActionQueries,
    private val conditionQueries: ConditionQueries,
    private val damageImmunityQueries: DamageImmunityQueries,
    private val damageResistanceQueries: DamageResistanceQueries,
    private val damageVulnerabilityQueries: DamageVulnerabilityQueries,
    private val damageDiceQueries: DamageDiceQueries,
    private val savingThrowQueries: SavingThrowQueries,
    private val skillQueries: SkillQueries,
    private val specialAbilityQueries: SpecialAbilityQueries,
    private val speedQueries: SpeedQueries,
    private val speedValueQueries: SpeedValueQueries,
    private val reactionQueries: ReactionQueries,
    private val spellcastingQueries: SpellcastingQueries,
    private val spellUsageQueries: SpellUsageQueries,
    private val spellcastingSpellUsageCrossRefQueries: SpellcastingSpellUsageCrossRefQueries,
    private val spellUsageCrossRefQueries: SpellUsageSpellCrossRefQueries,
    private val spellPreviewQueries: SpellPreviewQueries,
    private val legendaryActionQueries: LegendaryActionQueries,
    private val dispatcher: CoroutineDispatcher
) : MonsterDao {

    override suspend fun getMonsterPreviews(): List<MonsterEntity> = withContext(dispatcher) {
        monsterQueries.getMonsterPreviews().executeAsList().map { it.toLocalEntity() }
    }

    override suspend fun getMonsterPreviews(indexes: List<String>): List<MonsterEntity> =
        withContext(dispatcher) {
            monsterQueries.getMonsterPreviewsByIndexes(indexes).executeAsList()
                .map { it.toLocalEntity() }
        }

    override suspend fun getMonstersPreviewsEdited(): List<MonsterEntity> {
        return withContext(dispatcher) {
            monsterQueries.getMonstersEdited()
                .executeAsList()
                .map { it.toLocalEntity() }
        }
    }

    override suspend fun getMonsters(): List<MonsterCompleteEntity> = withContext(dispatcher) {
        monsterQueries.getMonsters().executeAsList().queryMonsterCompleteEntities()
    }

    override suspend fun getMonsters(indexes: List<String>): List<MonsterCompleteEntity> =
        withContext(dispatcher) {
            monsterQueries.getMonsterPreviewsByIndexes(indexes).executeAsList()
                .queryMonsterCompleteEntities()
        }

    override suspend fun getMonster(index: String): MonsterCompleteEntity =
        withContext(dispatcher) {
            monsterQueries.getMonster(index).executeAsList().queryMonsterCompleteEntities().first()
        }

    override suspend fun getMonstersByQuery(query: String): List<MonsterEntity> =
        withContext(dispatcher) {
            emptyList()
        }

    override suspend fun insert(monsters: List<MonsterCompleteEntity>, deleteAll: Boolean) = withContext(dispatcher) {
        monsterQueries.transaction {
            if (deleteAll) {
                deleteAllEntries(getMonstersByIsNotClone())
                monsterQueries.deleteAllVanilla()
            } else {
                deleteAllEntries(monsters)
            }
            monsterQueries.insert(monsters.map { it.monster })
            abilityScoreQueries.insert(monsters.mapAndReduce { abilityScores })
            actionQueries.run {
                val actions = monsters.mapAndReduce { actions }
                insert(actions.map { it.action })
                damageDiceQueries.insert(actions.mapAndReduce { damageDices })
            }
            savingThrowQueries.insert(monsters.mapAndReduce { savingThrows })
            skillQueries.insert(monsters.mapAndReduce { skills })
            specialAbilityQueries.insert(monsters.mapAndReduce { specialAbilities })
            speedQueries.insert(monsters.mapNotNull { it.speed?.speed })
            speedValueQueries.insert(monsters.mapNotNull { it.speed?.values }.reduceList())
            damageImmunityQueries.insert(monsters.mapAndReduce { damageImmunities })
            damageResistanceQueries.insert(monsters.mapAndReduce { damageResistances })
            damageVulnerabilityQueries.insert(monsters.mapAndReduce { damageVulnerabilities })
            conditionQueries.insert(monsters.mapAndReduce { conditionImmunities })
            reactionQueries.insert(monsters.mapAndReduce { reactions })

            val legendaryActions = monsters.mapAndReduce { legendaryActions }
            legendaryActionQueries.insert(legendaryActions.map { it.action })
            damageDiceQueries.insert(legendaryActions.mapAndReduce { damageDices })

            val spellcastings = monsters.mapAndReduce { spellcastings }
            spellcastingQueries.insert(spellcastings.map { it.spellcasting })

            val usages = spellcastings.mapAndReduce { usages }
            spellUsageQueries.insert(usages.map { it.spellUsage })

            val spellUsageCrossRefs = usages.map { usage ->
                SpellcastingSpellUsageCrossRefEntity(
                    spellcastingId = usage.spellUsage.spellcastingId,
                    spellUsageId = usage.spellUsage.spellUsageId
                )
            }
            spellcastingSpellUsageCrossRefQueries.insert(spellUsageCrossRefs)

            val spellsAndCrossRefs = usages.mapAndReduce {
                spells.map { spell ->
                    spell to SpellUsageSpellCrossRefEntity(
                        spellUsageId = spellUsage.spellUsageId, spellIndex = spell.spellIndex
                    )
                }
            }
            spellPreviewQueries.insert(spellsAndCrossRefs.map { it.first })
            spellUsageCrossRefQueries.insert(spellsAndCrossRefs.map { it.second })
        }
    }

    override suspend fun deleteMonster(index: String) = withContext(dispatcher) {
        monsterQueries.transaction {
            val monsters = monsterQueries.getMonster(index).executeAsList()
                .queryMonsterCompleteEntities()
            deleteAllEntries(monsters)
            monsterQueries.deleteByIndex(index)
        }
    }

    override suspend fun getMonstersByStatus(
        status: Set<MonsterEntityStatus>
    ): List<MonsterCompleteEntity> {
        return withContext(dispatcher) {
            monsterQueries.getMonstersByStatus(status.map { it.toStatusInt() }).executeAsList()
                .queryMonsterCompleteEntities()
        }
    }

    private fun deleteAllEntries(monsters: List<MonsterCompleteEntity>) {
        val monsterIndexes = monsters.map { it.monster.index }
        val actionsIds = monsters.mapAndReduce { actions.map { it.action.id } }
        val legendaryActionsIds = monsters.mapAndReduce { legendaryActions.map { it.action.id } }
        val speedIds = monsters.mapNotNull { it.speed?.speed?.id }
        val spellcastings = monsters.mapAndReduce { spellcastings }
        val spellcastingIds = spellcastings.map { it.spellcasting.spellcastingId }
        val spellUsageIds = spellcastings.mapAndReduce { usages.map { it.spellUsage.spellUsageId } }

        abilityScoreQueries.deleteWithMonsterIndex(monsterIndexes)
        actionQueries.deleteWithMonsterIndex(monsterIndexes)
        conditionQueries.deleteWithMonsterIndex(monsterIndexes)
        damageResistanceQueries.deleteWithMonsterIndex(monsterIndexes)
        damageImmunityQueries.deleteWithMonsterIndex(monsterIndexes)
        damageVulnerabilityQueries.deleteWithMonsterIndex(monsterIndexes)
        damageDiceQueries.deleteWithActionId(actionsIds)
        damageDiceQueries.deleteWithActionId(legendaryActionsIds)
        savingThrowQueries.deleteWithMonsterIndex(monsterIndexes)
        skillQueries.deleteWithMonsterIndex(monsterIndexes)
        specialAbilityQueries.deleteWithMonsterIndex(monsterIndexes)
        speedQueries.deleteWithMonsterIndex(monsterIndexes)
        speedValueQueries.deleteWithSpeedId(speedIds)
        reactionQueries.deleteWithMonsterIndex(monsterIndexes)
        spellcastingQueries.deleteWithMonsterIndex(monsterIndexes)
        spellcastingSpellUsageCrossRefQueries.deleteWithSpellcastingId(spellcastingIds)
        spellUsageQueries.deleteWithSpellcastingId(spellcastingIds)
        spellUsageCrossRefQueries.deleteWithSpellUsageId(spellUsageIds)
        legendaryActionQueries.deleteWithMonsterIndex(monsterIndexes)
    }

    private fun getMonstersByIsNotClone(): List<MonsterCompleteEntity> {
        return monsterQueries.getMonstersThatIsNotCloned()
            .executeAsList()
            .queryMonsterCompleteEntities()
    }

    private fun List<MonsterDatabaseEntity>.queryMonsterCompleteEntities(): List<MonsterCompleteEntity> =
        map { monster ->
            val speed = getSpeed(monster, speedQueries, speedValueQueries)
            val abilityScores = getAbilityScores(monster, abilityScoreQueries)
            val actions = getActions(monster, actionQueries, damageDiceQueries)
            val reactions = getReactions(monster, reactionQueries)
            val specialAbilities = getSpecialAbilities(monster, specialAbilityQueries)
            val legendaryActions = getLegendaryActions(
                monster, legendaryActionQueries, damageDiceQueries
            )
            val spellcastings = getSpellcastings(
                monster,
                spellcastingQueries,
                spellUsageQueries,
                spellUsageCrossRefQueries
            )
            val savingThrows = getSavingThrows(monster, savingThrowQueries)
            val skills = getSkills(monster, skillQueries)
            val damageVulnerabilities = getDamageVulnerabilities(
                monster, damageVulnerabilityQueries
            )
            val damageResistances =
                getDamageResistances(monster, damageResistanceQueries)
            val damageImmunities = getDamageImmunities(monster, damageImmunityQueries)
            val conditionImmunities = getConditionImmunities(monster, conditionQueries)

            MonsterCompleteEntity(
                monster = monster.toLocalEntity(),
                speed = speed,
                abilityScores = abilityScores,
                actions = actions,
                reactions = reactions,
                specialAbilities = specialAbilities,
                legendaryActions = legendaryActions,
                spellcastings = spellcastings,
                savingThrows = savingThrows,
                skills = skills,
                damageVulnerabilities = damageVulnerabilities,
                damageResistances = damageResistances,
                damageImmunities = damageImmunities,
                conditionImmunities = conditionImmunities
            )
        }
}

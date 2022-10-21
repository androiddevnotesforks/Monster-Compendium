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

@file:OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.transition.AlphaTransition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

fun LazyListScope.monsterInfo(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    getItemsKeys: () -> List<Any> = { emptyList() },
    onSpellClicked: (String) -> Unit = {}
) {
    monsterInfoPart1(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
    )

    monsterInfoPart2(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
    )

    monsterInfoPart3(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
    )

    monsterInfoPart4(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
    )

    monsterInfoPart5(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
    )

    spellBlock(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
        onSpellClicked = onSpellClicked
    )

    item(key = "reactions") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.reactions },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ReactionBlock(reactions = it)
        }
    }

    item(key = "space") {
        Spacer(
            modifier = Modifier
                .height(contentPadding.calculateBottomPadding())
                .fillMaxWidth()
                .animateItemPlacement()
        )
    }
}

private fun LazyListScope.monsterInfoPart1(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "stats") {
        MonsterRequireSectionAlphaTransition(
            monsters,
            pagerState,
            getItemsKeys = getItemsKeys,
        ) { monster ->
            StatsBlock(stats = monster.stats)
        }
    }
    item(key = "speed") {
        MonsterRequireSectionAlphaTransition(
            monsters,
            pagerState,
            getItemsKeys = getItemsKeys,
        ) { monster ->
            SpeedBlock(speed = monster.speed)
        }
    }
    item(key = "abilityScores") {
        MonsterRequireSectionAlphaTransition(
            monsters,
            pagerState,
            getItemsKeys = getItemsKeys,
        ) { monster ->
            AbilityScoreBlock(abilityScores = monster.abilityScores)
        }
    }
}

private fun LazyListScope.monsterInfoPart2(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "savingThrows") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.savingThrows },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ProficiencyBlock(
                proficiencies = it,
                title = stringResource(R.string.monster_detail_saving_throws)
            )
        }
    }
    item(key = "skills") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.skills },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ProficiencyBlock(
                proficiencies = it,
                title = stringResource(R.string.monster_detail_skills)
            )
        }
    }
}

private fun LazyListScope.monsterInfoPart3(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "damageVulnerabilities") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.damageVulnerabilities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            DamageVulnerabilitiesBlock(damages = it)
        }
    }
    item(key = "damageResistances") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.damageResistances },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            DamageResistancesBlock(damages = it)
        }
    }
    item(key = "damageImmunities") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.damageImmunities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            DamageImmunitiesBlock(damages = it)
        }
    }
    item(key = "conditionImmunities") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.conditionImmunities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ConditionBlock(conditions = it)
        }
    }
}

private fun LazyListScope.monsterInfoPart4(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "senses") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.senses },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            SensesBlock(senses = it)
        }
    }
    item(key = "languages") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.languages },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            TextBlock(
                title = stringResource(R.string.monster_detail_languages),
                text = it
            )
        }
    }
}

private fun LazyListScope.monsterInfoPart5(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "specialAbilities") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.specialAbilities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            SpecialAbilityBlock(specialAbilities = it)
        }
    }

    item(key = "actions") {
        MonsterRequireSectionAlphaTransition(
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) { monster ->
            ActionBlock(actions = monster.actions)
        }
    }
}

@Composable
fun MonsterSectionAlphaTransition(
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    content: @Composable ColumnScope.(MonsterState) -> Unit
) {
    val enableGesture by remember {
        derivedStateOf {
            getItemsKeys().find { it is String && it.contains(SPELLCASTING_ITEM_KEY) } == null
        }
    }

    AlphaTransition(
        dataList = dataList,
        pagerState = pagerState,
        enableGesture = { enableGesture },
        modifier = modifier
    ) { monster ->
        MonsterInfoSectionColumn {
            content(monster)
        }
    }
}

@Composable
fun LazyItemScope.MonsterRequireSectionAlphaTransition(
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    content: @Composable ColumnScope.(MonsterState) -> Unit
) = MonsterSectionAlphaTransition(
    dataList, pagerState, modifier.animateItemPlacement(), getItemsKeys
) { monster ->
    BlockSection {
        content(monster)
    }
}

@Composable
fun <T> LazyItemScope.MonsterOptionalSectionAlphaTransition(
    valueToValidate: (MonsterState) -> T,
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    content: @Composable ColumnScope.(T) -> Unit
) = MonsterSectionAlphaTransition(
    dataList, pagerState, modifier.animateItemPlacement(), getItemsKeys
) { monster ->
    OptionalBlockSection(valueToValidate(monster)) { value ->
        content(value)
    }
}

@Composable
private fun MonsterInfoSectionColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = modifier.fillMaxWidth(),
    content = content
)

@Composable
private fun <T> ColumnScope.OptionalBlockSection(
    value: T,
    content: @Composable ColumnScope.(T) -> Unit,
) {
    if ((value is String && value.trim().isEmpty()) || (value is List<*> && value.isEmpty())) return
    BlockSection {
        content(value)
    }
}

@Composable
private fun ColumnScope.BlockSection(
    content: @Composable ColumnScope.() -> Unit,
) {
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    )

    content()
}
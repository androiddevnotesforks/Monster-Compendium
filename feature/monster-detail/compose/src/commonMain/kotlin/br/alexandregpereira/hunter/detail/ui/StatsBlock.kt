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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.ui.resources.Res
import br.alexandregpereira.hunter.detail.ui.resources.ic_hit_point
import br.alexandregpereira.hunter.detail.ui.resources.ic_shield
import br.alexandregpereira.hunter.monster.detail.StatsState
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StatsBlock(
    stats: StatsState,
    modifier: Modifier = Modifier
) = Block(modifier = modifier) {

    StatsGrid(stats = stats)
}

@Composable
private fun StatsGrid(
    stats: StatsState,
) = Grid {
    IconInfo(
        title = strings.armorClass,
        painter = painterResource(Res.drawable.ic_shield),
        iconSize = 72.dp,
        iconText = stats.armorClass.toString(),
    )

    IconInfo(
        title = stats.hitDice,
        painter = painterResource(Res.drawable.ic_hit_point),
        iconSize = 72.dp,
        iconText = stats.hitPoints.toString(),
        iconPadding = PaddingValues(top = 4.dp),
    )
}

@Preview
@Composable
private fun StatsGridPreview() = Window {
    StatsGrid(
        stats = StatsState(armorClass = 20, hitPoints = 100, hitDice = "28d20 + 252")
    )
}

@Preview
@Composable
private fun StatsBlockPreview() = Window {
    StatsBlock(stats = StatsState(armorClass = 0, hitPoints = 0, hitDice = "teasdas"))
}

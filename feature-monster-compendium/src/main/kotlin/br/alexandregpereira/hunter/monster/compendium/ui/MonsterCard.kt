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

package br.alexandregpereira.hunter.monster.compendium.ui

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun MonsterCard(
    imageUrl: String,
    backgroundColor: String,
    contentDescription: String,
    challengeRating: Float,
    challengeRatingFormatted: String,
    modifier: Modifier = Modifier
) {
    MonsterImage(
        imageUrl = imageUrl,
        backgroundColor = backgroundColor,
        contentDescription = contentDescription,
        challengeRatingFormatted = challengeRatingFormatted,
        modifier = modifier.size(width = 156.dp, height = 208.dp)
    )
}

@Composable
fun MonsterImage(
    imageUrl: String,
    backgroundColor: String,
    contentDescription: String,
    challengeRatingFormatted: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(8.dp),
) {
    Box(
        modifier
            .padding(paddingValues)
            .clip(Shapes.large)
    ) {
        CoilImage(
            data = imageUrl,
            contentDescription = contentDescription,
            fadeIn = true,
            modifier = Modifier
                .height(208.dp)
                .fillMaxWidth()
                .background(
                    color = Color(
                        backgroundColor
                            .runCatching { parseColor(this) }
                            .getOrNull() ?: 0
                    ),
                    shape = Shapes.large
                )
        )

        ChallengeRatingCircle(
            modifier = Modifier.offset(x = -(53.dp), y = -(53.dp))
        )

        Text(
            challengeRatingFormatted,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(29.dp).offset(x = 0.dp, y = 4.dp)
        )
    }
}

@Composable
fun ChallengeRatingCircle(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(Color.Red)
    )
}

@Preview
@Composable
fun MonsterCardPreview() {
    HunterTheme {
        MonsterCard(
            "https://raw.githubusercontent.com/alexandregpereira/dnd-monster-manual/main/images/aboleth.png",
            "#80e3efef",
            "any",
            22f,
            "22"
        )
    }
}

@Preview
@Composable
fun ChallengeRatingPreview() {
    HunterTheme {
        ChallengeRatingCircle()
    }
}
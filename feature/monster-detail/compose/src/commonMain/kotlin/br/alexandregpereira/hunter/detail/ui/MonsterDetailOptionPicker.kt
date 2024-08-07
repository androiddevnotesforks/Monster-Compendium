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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.animatePressed

@Composable
internal fun MonsterDetailOptionPicker(
    options: List<MonsterDetailOptionState>,
    showOptions: Boolean,
    contentPadding: PaddingValues = PaddingValues(),
    onOptionSelected: (MonsterDetailOptionState) -> Unit = {},
    onClosed: () -> Unit = {}
) = BottomSheet(
    opened = showOptions,
    onClose = onClosed,
) {
    MonsterDetailOptions(options, contentPadding, onOptionSelected)
}

@Composable
private fun MonsterDetailOptions(
    options: List<MonsterDetailOptionState>,
    contentPadding: PaddingValues = PaddingValues(),
    onOptionSelected: (MonsterDetailOptionState) -> Unit = {},
) {
    Column {
        Text(
            text = strings.options,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(all = 16.dp)
        )

        options.forEach {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
                    .animatePressed(
                        onClick = { onOptionSelected(it) }
                    )
            ) {
                Text(
                    text = it.name,
                    fontSize = 16.sp,
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(contentPadding.calculateBottomPadding() + 16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun MonsterDetailOptionPickerPreview() {
    MonsterDetailOptionPicker(
        listOf(
            MonsterDetailOptionState(name = "Change to feet"),
            MonsterDetailOptionState(name = "Change to meters"),
        ),
        showOptions = true
    )
}
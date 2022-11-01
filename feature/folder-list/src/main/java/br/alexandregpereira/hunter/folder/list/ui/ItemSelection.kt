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

package br.alexandregpereira.hunter.folder.list.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.folder.list.R
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun ItemSelection(
    itemSelectionCount: Int,
    modifier: Modifier = Modifier,
    isOpen: Boolean = true,
    contentBottomPadding: Dp = 0.dp,
    onClose: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    BackHandler(enabled = isOpen, onBack = onClose)

    AnimatedVisibility(
        visible = isOpen,
        enter = slideInVertically { fullHeight -> fullHeight * 2 },
        exit = slideOutVertically { fullHeight -> fullHeight * 2 },
    ) {
        Box(modifier.fillMaxHeight()) {
            Card(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .align(BottomCenter)
            ) {
                Column(Modifier.padding(16.dp)) {
                    val titleRes = if (itemSelectionCount == 1) {
                        R.string.folder_list_item_selected
                    } else R.string.folder_list_items_selected
                    ScreenHeader(
                        title = stringResource(titleRes, itemSelectionCount),
                    )

                    AppButton(
                        text = stringResource(R.string.folder_list_delete),
                        modifier = Modifier.padding(top = 24.dp),
                        onClick = onDeleteClick
                    )

                    Spacer(modifier = Modifier.height(contentBottomPadding))
                }
            }
        }
    }
}

@Preview
@Composable
private fun ItemSelectionPreview() = HunterTheme {
    ItemSelection(
        itemSelectionCount = 2,
    )
}
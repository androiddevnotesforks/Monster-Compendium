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

package br.alexandregpereira.hunter.monster.content.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewAction
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewActionType.GO_TO_COMPENDIUM_INDEX
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewState
import br.alexandregpereira.hunter.state.ActionHandler
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCompendium
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppScreen
import br.alexandregpereira.hunter.ui.compose.Closeable
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.PopupContainer
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemState
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemTypeState
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentPopup
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MonsterContentPreviewScreen(
    state: MonsterContentPreviewState,
    actionHandler: ActionHandler<MonsterContentPreviewAction> = MutableActionHandler(),
    contentPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {},
    onTableContentOpenButtonClick: () -> Unit = {},
    onTableContentClose: () -> Unit = {},
    onTableContentClick: (Int) -> Unit = {},
    onFirstVisibleItemChange: (Int) -> Unit = {},
) {
    Closeable(
        opened = state.isOpen,
        onClosed = onClose,
    )

    AppScreen(isOpen = state.isOpen, contentPadding, onClose = onClose) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(top = 24.dp + contentPadding.calculateTopPadding())
                .clip(shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
        ) {
            LoadingScreen(
                isLoading = state.isLoading,
                showCircularLoading = false,
                modifier = Modifier.background(MaterialTheme.colors.surface)
            ) {
                var compendiumIndex by remember {
                    mutableStateOf(-1)
                }
                MonsterContentPreviewScreenContent(
                    state = state,
                    contentPadding = contentPadding,
                    compendiumIndex = compendiumIndex,
                    onTableContentOpenButtonClick = onTableContentOpenButtonClick,
                    onTableContentClose = onTableContentClose,
                    onTableContentClick = onTableContentClick,
                    onFirstVisibleItemChange = onFirstVisibleItemChange,
                )
                LaunchedEffect(actionHandler) {
                    actionHandler.action.collect { action ->
                        when (action.type) {
                            GO_TO_COMPENDIUM_INDEX -> compendiumIndex = action.index
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MonsterContentPreviewScreenContent(
    state: MonsterContentPreviewState,
    contentPadding: PaddingValues,
    compendiumIndex: Int = -1,
    onTableContentOpenButtonClick: () -> Unit = {},
    onTableContentClose: () -> Unit = {},
    onTableContentClick: (Int) -> Unit = {},
    onFirstVisibleItemChange: (Int) -> Unit = {},
) {
    PopupContainer(
        isOpened = state.tableContentOpened,
        onPopupClosed = onTableContentClose,
        content = {
            val title = MonsterCompendiumItem.Title(
                id = state.title,
                value = state.title,
                isHeader = true
            )
            val listState = rememberLazyGridState()
            MonsterCompendium(
                items = (listOf(title) + state.monsterCompendiumItems).asState(),
                listState = listState
            )

            OnFirstVisibleItemChange(listState, onFirstVisibleItemChange)

            if (compendiumIndex >= 0) {
                LaunchedEffect(compendiumIndex) {
                    listState.scrollToItem(compendiumIndex + 1)
                }
            }
        },
        popupContent = {
            TableContentPopup(
                alphabet = state.alphabet,
                alphabetSelectedIndex = state.alphabetSelectedIndex,
                tableContent = state.tableContent.asStateTableContentItem(),
                tableContentSelectedIndex = state.tableContentSelectedIndex,
                opened = state.tableContentOpened,
                onOpenButtonClicked = onTableContentOpenButtonClick,
                onCloseButtonClicked = onTableContentClose,
                onTableContentClicked = onTableContentClick,
                tableContentOpened = state.tableContentOpened,
                onTableContentClosed = onTableContentClose,
                modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding())
            )
        }
    )
}

@Composable
private fun OnFirstVisibleItemChange(
    listState: LazyGridState,
    onChange: (position: Int) -> Unit
) {
    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    onChange(firstVisibleItemIndex)
}

@Preview
@Composable
private fun MonsterContentPreviewScreenPreview() {
    MonsterContentPreviewScreen(
        state = MonsterContentPreviewState(
            title = "Monster Content Preview",
            monsterCompendiumItems = listOf(
                MonsterCompendiumItem.Title(
                    id = "eum",
                    value = "potenti",
                    isHeader = false
                )
            ) + (0..10).map {
                MonsterCompendiumItem.Item(
                    monster = Monster(
                        index = "usu+$it",
                        name = "Raphael Bolton",
                        type = MonsterType.ABERRATION,
                        challengeRating = 13f,
                    )
                )
            },
            isOpen = true,
            alphabet = listOf("A", "B", "C", "D", "E", "F", "G"),
        ),
    )
}

private fun List<MonsterCompendiumItem>.asState(): List<CompendiumItemState> {
    return this.map { item ->
        when (item) {
            is MonsterCompendiumItem.Title -> CompendiumItemState.Title(
                value = item.value,
                id = item.id,
                isHeader = item.isHeader
            )

            is MonsterCompendiumItem.Item -> CompendiumItemState.Item(
                value = item.monster.asState()
            )
        }
    }
}

private fun Monster.asState(): MonsterCardState {
    return MonsterCardState(
        index = index,
        name = name,
        imageState = MonsterImageState(
            url = imageData.url,
            type = MonsterTypeState.valueOf(type.name),
            challengeRating = challengeRatingFormatted,
            backgroundColor = ColorState(
                light = imageData.backgroundColor.light,
                dark = imageData.backgroundColor.dark
            ),
            isHorizontal = imageData.isHorizontal
        )
    )
}

fun List<TableContentItem>.asStateTableContentItem(): List<TableContentItemState> {
    return this.map {
        TableContentItemState(
            id = it.id,
            text = it.text,
            type = TableContentItemTypeState.valueOf(it.type.name)
        )
    }
}

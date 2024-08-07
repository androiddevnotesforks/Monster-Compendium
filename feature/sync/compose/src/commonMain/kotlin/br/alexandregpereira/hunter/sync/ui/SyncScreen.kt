package br.alexandregpereira.hunter.sync.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.sync.SyncState
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessage
import br.alexandregpereira.hunter.ui.compose.LoadingIndicator
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun SyncScreen(
    state: SyncState,
    onTryAgain: () -> Unit = {},
) {
    AnimatedVisibility(
        visible = state.isOpen,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight },
    ) {
        Window(Modifier.fillMaxSize()) {
            Crossfade(targetState = state.hasError) { hasError ->
                if (hasError) {
                    EmptyScreenMessage(
                        title = "No Internet Connection",
                        buttonText = "Try Again",
                        onButtonClick = onTryAgain
                    )
                } else {
                    LoadingIndicator()
                }
            }
        }
    }
}

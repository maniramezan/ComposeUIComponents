package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Toast Host", group = "Feedback")
@Composable
public fun ToastHostPreview(): Unit =
    AppTheme {
        val hostState = rememberToastHostState()
        // Indefinite keeps the toast on screen for the preview without an
        // auto-dismiss timer firing.
        LaunchedEffect(Unit) {
            hostState.showToast(
                message = "Saved",
                actionLabel = "Undo",
                duration = ToastDuration.Indefinite,
            )
        }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(AppTheme.spacing.xl * 6),
        ) {
            ToastHost(hostState = hostState, dismissContentDescription = "Dismiss")
        }
    }

package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Feedback Components", group = "Feedback")
@Composable
public fun FeedbackComponentsPreview(): Unit =
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            ProgressIndicator(label = "Loading")
            ProgressIndicator(progress = 0.65f, label = "Syncing")
            Skeleton()
            Toast(
                message = "Saved",
                actionLabel = "Undo",
                onAction = {},
            )
        }
    }

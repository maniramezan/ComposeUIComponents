package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Feedback Components", group = "Feedback")
@Composable
public fun FeedbackComponentsPreview(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            ProgressIndicator(label = "Loading")
            ProgressIndicator(progress = 0.65f, label = "Syncing")
            Skeleton()
            Toast(
                message = "Saved",
                icon = AppTheme.icons.check,
                actionLabel = "Undo",
                onAction = {},
            )
            Toast(
                message = "Your changes are saved and will sync to all of your devices shortly.",
                icon = AppTheme.icons.check,
                actionLabel = "View",
                onAction = {},
            )
        }
    }

@PreviewLightDark
@Preview(name = "Skeleton Shimmer", group = "Feedback")
@Composable
public fun SkeletonShimmerPreview(): Unit =
    AppTheme {
        // skeletonShimmer applied once at the root of the ghost layout. Force-disabled here
        // so the preview / screenshot golden is deterministic — a static frame cannot show
        // the sweep anyway, and this also exercises the inert pass-through path.
        CompositionLocalProvider(LocalSkeletonShimmerEnabled provides false) {
            Column(
                modifier = Modifier.skeletonShimmer(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            ) {
                SkeletonBlock(height = AppTheme.spacing.lg, width = 220.dp)
                SkeletonBlock(height = AppTheme.spacing.md, width = 240.dp)
                SkeletonBlock(height = AppTheme.spacing.md, width = 240.dp)
                SkeletonBlock(height = AppTheme.spacing.md, width = 140.dp)
            }
        }
    }

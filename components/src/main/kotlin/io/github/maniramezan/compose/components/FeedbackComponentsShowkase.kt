package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Feedback", group = "Feedback")
@Composable
public fun FeedbackComponentsShowkase(): Unit = FeedbackComponentsPreview()

@ShowkaseComposable(name = "Progress Indicator", group = "Feedback")
@Composable
public fun ProgressIndicatorShowkase(): Unit =
    AppTheme {
        ProgressIndicator(progress = 0.65f, label = "Syncing")
    }

@ShowkaseComposable(name = "Skeleton", group = "Feedback")
@Composable
public fun SkeletonShowkase(): Unit =
    AppTheme {
        Skeleton()
    }

@ShowkaseComposable(name = "Skeleton Block", group = "Feedback")
@Composable
public fun SkeletonBlockShowkase(): Unit =
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
            SkeletonBlock(height = 20.dp, width = 160.dp)
            SkeletonBlock(height = AppTheme.spacing.x2)
        }
    }

@ShowkaseComposable(name = "Skeleton Shimmer", group = "Feedback")
@Composable
public fun SkeletonShimmerShowkase(): Unit = SkeletonShimmerPreview()

@ShowkaseComposable(name = "Toast", group = "Feedback")
@Composable
public fun ToastShowkase(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        Toast(
            message = "Saved",
            icon = AppTheme.icons.check,
            actionLabel = "Undo",
            onAction = {},
        )
    }

@ShowkaseComposable(name = "Chat Log", group = "Feedback")
@Composable
public fun ChatLogShowkase(): Unit = ChatLogPreview()

@ShowkaseComposable(name = "Assistant Chat Surface", group = "Feedback")
@Composable
public fun AssistantChatSurfaceShowkase(): Unit = AssistantChatSurfacePreview()

@ShowkaseComposable(name = "Assistant Quick Actions", group = "Feedback")
@Composable
public fun AssistantQuickActionChipsShowkase(): Unit = AssistantQuickActionChipsPreview()

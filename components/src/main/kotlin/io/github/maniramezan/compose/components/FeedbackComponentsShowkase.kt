package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Feedback", group = "Feedback")
@Composable
public fun FeedbackComponentsShowkase(): Unit = FeedbackComponentsPreview()

@ShowkaseComposable(name = "Progress Indicator", group = "Feedback")
@Composable
public fun ProgressIndicatorShowkase(): Unit = AppTheme {
    ProgressIndicator(progress = 0.65f, label = "Syncing")
}

@ShowkaseComposable(name = "Skeleton", group = "Feedback")
@Composable
public fun SkeletonShowkase(): Unit = AppTheme {
    Skeleton()
}

@ShowkaseComposable(name = "Toast", group = "Feedback")
@Composable
public fun ToastShowkase(): Unit = AppTheme {
    Toast(
        message = "Saved",
        actionLabel = "Undo",
        onAction = {},
    )
}

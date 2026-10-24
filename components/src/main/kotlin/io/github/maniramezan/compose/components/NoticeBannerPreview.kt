package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Notice Banner", group = "Feedback")
@Composable
public fun NoticeBannerPreview(): Unit = AppTheme(icons = defaultAppIcons()) { NoticeBannerPreviewContent() }

/** The preview rows without a theme, so callers can supply their own [AppTheme]. */
@Composable
internal fun NoticeBannerPreviewContent() {
    Column(
        modifier = Modifier.padding(AppTheme.spacing.x2),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
    ) {
        NoticeBanner(
            message = "Your changes are saved.",
            severity = NoticeSeverity.Success,
            icon = AppTheme.icons.check,
            iconContentDescription = "Success",
        )
        NoticeBanner(
            message = "A new version of this section is available.",
            severity = NoticeSeverity.Info,
            actionLabel = "Refresh",
            onAction = {},
        )
        NoticeBanner(
            message = "Some items could not be synced and will be retried later.",
            severity = NoticeSeverity.Warning,
            actionLabel = "Details",
            onAction = {},
            dismissContentDescription = "Dismiss",
            onDismiss = {},
        )
        NoticeBanner(
            message = "The request failed. Check your connection and try again.",
            severity = NoticeSeverity.Error,
            icon = AppTheme.icons.check,
            iconContentDescription = "Error",
            actionLabel = "Retry",
            onAction = {},
            dismissContentDescription = "Dismiss",
            onDismiss = {},
        )
    }
}

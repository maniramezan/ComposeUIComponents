package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Thumbs Feedback Row", group = "Feedback")
@Composable
public fun ThumbsFeedbackRowPreview(): Unit = AppTheme { ThumbsFeedbackRowPreviewContent() }

/** The preview rows without a theme, so callers can supply their own [AppTheme]. */
@Composable
internal fun ThumbsFeedbackRowPreviewContent() {
    Column(
        modifier = Modifier.padding(AppTheme.spacing.x2),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
    ) {
        ThumbsFeedbackPreviewRow(rating = ThumbsRating.None)
        ThumbsFeedbackPreviewRow(rating = ThumbsRating.Positive)
        ThumbsFeedbackPreviewRow(rating = ThumbsRating.Negative)
        ThumbsFeedbackPreviewRow(rating = ThumbsRating.Positive, enabled = false)
        ThumbsFeedbackPreviewRow(rating = ThumbsRating.None, enabled = false)
    }
}

@Composable
private fun ThumbsFeedbackPreviewRow(
    rating: ThumbsRating,
    enabled: Boolean = true,
) {
    ThumbsFeedbackRow(
        rating = rating,
        onRatingChange = {},
        positiveContentDescription = "Helpful",
        negativeContentDescription = "Not helpful",
        enabled = enabled,
    )
}

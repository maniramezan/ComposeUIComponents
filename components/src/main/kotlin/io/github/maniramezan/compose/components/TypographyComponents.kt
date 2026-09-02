package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import io.github.maniramezan.compose.theme.AppTheme

/**
 * Semantic text roles for [AppText], each resolving to a theme typography token.
 *
 * The first four are the library's original 4-slot scale. The remaining roles
 * map onto the `AppTypography` weight-variant slots — a Material 3 base slot
 * re-cut at a heavier weight, with size and line height kept on the scale — so
 * a caller can reach them without going through `AppTheme.typography` directly.
 */
public enum class AppTextStyle {
    Display,
    Title,
    Body,
    Label,

    // ===== Weight variants (see AppTypography) =====
    LabelSmallSemibold,
    LabelSmallBold,
    LabelMediumSemibold,
    LabelMediumBold,
    LabelLargeBold,
    BodySmallMedium,
    BodySmallSemibold,
    BodyMediumSemibold,
    BodyLargeSemibold,
}

/** A themed text component that resolves [style] to the current [AppTheme] typography token. */
@Composable
public fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: AppTextStyle = AppTextStyle.Body,
    color: Color = AppTheme.colors.onSurface,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style.toTextStyle(),
    )
}

@Composable
private fun AppTextStyle.toTextStyle(): TextStyle =
    when (this) {
        AppTextStyle.Display -> AppTheme.typography.display
        AppTextStyle.Title -> AppTheme.typography.title
        AppTextStyle.Body -> AppTheme.typography.body
        AppTextStyle.Label -> AppTheme.typography.label
        AppTextStyle.LabelSmallSemibold -> AppTheme.typography.labelSmallSemibold
        AppTextStyle.LabelSmallBold -> AppTheme.typography.labelSmallBold
        AppTextStyle.LabelMediumSemibold -> AppTheme.typography.labelMediumSemibold
        AppTextStyle.LabelMediumBold -> AppTheme.typography.labelMediumBold
        AppTextStyle.LabelLargeBold -> AppTheme.typography.labelLargeBold
        AppTextStyle.BodySmallMedium -> AppTheme.typography.bodySmallMedium
        AppTextStyle.BodySmallSemibold -> AppTheme.typography.bodySmallSemibold
        AppTextStyle.BodyMediumSemibold -> AppTheme.typography.bodyMediumSemibold
        AppTextStyle.BodyLargeSemibold -> AppTheme.typography.bodyLargeSemibold
    }

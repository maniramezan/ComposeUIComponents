package io.github.maniramezan.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Stroke widths for borders, dividers, and indicators.
 *
 * @property hairline Subtle separator or outline. Defaults to 0.5 dp and scales with display density.
 * @property thin Standard divider or outline.
 * @property regular Emphasized outline or compact indicator.
 * @property thick Strong emphasis or progress indicator.
 */
@Immutable
public data class AppStroke(
    public val hairline: Dp = 0.5.dp,
    public val thin: Dp = 1.dp,
    public val regular: Dp = 2.dp,
    public val thick: Dp = 4.dp,
)

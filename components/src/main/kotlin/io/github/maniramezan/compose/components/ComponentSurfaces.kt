package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import io.github.maniramezan.compose.theme.AppTheme

/**
 * Draws a capsule (pill) surface with a [containerColor] fill, clipped to the
 * [AppTheme.shapes.pill] shape. Components that share the pill visual language
 * (chips, pills, badges, compact indicators) reuse this so the fill/clip/border
 * stay consistent and themeable.
 *
 * @param containerColor Fill color for the surface.
 * @param border Whether to draw the theme's thin outline border around the surface.
 */
@Composable
@ReadOnlyComposable
internal fun Modifier.pillSurface(
    containerColor: Color,
    border: Boolean = false,
): Modifier =
    clip(AppTheme.shapes.pill).background(containerColor).let {
        if (border) it.border(AppTheme.spacing.strokeThin, AppTheme.colors.outline, AppTheme.shapes.pill) else it
    }

/**
 * The standard title/subtitle/supporting text block for list rows, read as a single
 * TalkBack focus stop via merged descendant semantics.
 *
 * Kept as a small [RowScope] composable so the leading text column can take the
 * remaining row width via [RowScope.weight] and the headline/supporting layout and
 * semantics stay consistent across the list-row components.
 *
 * @param title Primary text shown in the first line.
 * @param titleStyle The typography style for [title].
 * @param titleColor Color for [title].
 * @param supportingStyle The typography style for [secondaryText] and [supportingText].
 * @param supportingColor Color for [secondaryText] and [supportingText].
 * @param secondaryText Optional text shown inline beside [title] on the first line.
 * @param supportingText Optional second-line supporting text.
 * @param supportingMaxLines Maximum lines for [supportingText].
 */
@Composable
internal fun RowScope.ListPrimaryTextBlock(
    title: String,
    titleStyle: TextStyle,
    titleColor: Color,
    supportingStyle: TextStyle,
    supportingColor: Color,
    modifier: Modifier = Modifier,
    secondaryText: String? = null,
    supportingText: String? = null,
    supportingMaxLines: Int = Int.MAX_VALUE,
) {
    Column(
        // Read the title/secondary/supporting block as one focus stop instead
        // of several separate ones.
        modifier = modifier.weight(1f).semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = titleStyle,
                color = titleColor,
            )
            if (secondaryText != null) {
                Text(
                    text = secondaryText,
                    style = supportingStyle,
                    color = supportingColor,
                )
            }
        }
        if (supportingText != null) {
            Text(
                text = supportingText,
                style = supportingStyle,
                color = supportingColor,
                maxLines = supportingMaxLines,
            )
        }
    }
}

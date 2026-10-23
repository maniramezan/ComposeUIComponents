package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.compose.theme.AppTheme
import androidx.compose.material3.HorizontalDivider as MaterialHorizontalDivider
import androidx.compose.material3.VerticalDivider as MaterialVerticalDivider

/**
 * A full-width horizontal rule. Defaults to `AppTheme.colors.outlineVariant` so it
 * blends with themed surfaces without extra configuration.
 */
@Composable
public fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = AppTheme.colors.outlineVariant,
) {
    MaterialHorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

/**
 * A full-height vertical rule. Defaults to `AppTheme.colors.outlineVariant` so it
 * blends with themed surfaces without extra configuration.
 */
@Composable
public fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = AppTheme.colors.outlineVariant,
) {
    MaterialVerticalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

/**
 * A centered label between two horizontal rules.
 *
 * The label wraps within half the available width, keeping both rules visible at large font
 * scales and narrow widths. Blank labels render a single [HorizontalDivider] with no text node.
 *
 * @param label supply a localized section or alternative label. Blank values omit the text.
 * @param thickness the thickness of both rules.
 * @param color the color of both rules; the label uses `AppTheme.colors.onSurfaceVariant`.
 */
@Composable
public fun LabeledDivider(
    label: String,
    modifier: Modifier = Modifier,
    thickness: Dp = AppTheme.spacing.strokeThin,
    color: Color = AppTheme.colors.outlineVariant,
) {
    if (label.isBlank()) {
        HorizontalDivider(modifier = modifier, thickness = thickness, color = color)
        return
    }
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val labelMaxWidth = maxWidth / 2
        val labelSpacing = minOf(AppTheme.spacing.x1_5, maxWidth / 8)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(labelSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), thickness = thickness, color = color)
            Text(
                text = label,
                modifier = Modifier.widthIn(max = labelMaxWidth),
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            HorizontalDivider(modifier = Modifier.weight(1f), thickness = thickness, color = color)
        }
    }
}

package io.github.maniramezan.compose.components

import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

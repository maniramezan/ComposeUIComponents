package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.theme.AppTheme

/**
 * Places content from start to end and wraps items onto additional rows when
 * the available width is exhausted.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
public fun FlowLayout(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(AppTheme.spacing.x1),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(AppTheme.spacing.x1),
    content: @Composable FlowRowScope.() -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = content,
    )
}

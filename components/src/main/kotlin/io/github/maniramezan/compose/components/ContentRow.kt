package io.github.maniramezan.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.theme.AppTheme

/**
 * A two-line list row with title, optional supporting/secondary text, and
 * leading + trailing content slots. Suitable for vocabulary rows, document
 * lists, settings rows, and similar single-tap entry points.
 *
 * The whole row is clickable when [onClick] is non-null; pass `null` for a
 * read-only display.
 */
@Composable
public fun ContentRow(
    title: String,
    modifier: Modifier = Modifier,
    secondaryText: String? = null,
    supportingText: String? = null,
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val rowModifier =
        modifier
            .fillMaxWidth()
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.x1_5)

    Row(
        modifier = rowModifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingContent != null) {
            leadingContent()
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.titleSmall,
                    color = AppTheme.colors.onSurface,
                )
                if (secondaryText != null) {
                    Text(
                        text = secondaryText,
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onSurfaceVariant,
                    )
                }
            }
            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.onSurfaceVariant,
                    maxLines = 2,
                )
            }
        }
        if (trailingContent != null) {
            trailingContent()
        }
    }
}

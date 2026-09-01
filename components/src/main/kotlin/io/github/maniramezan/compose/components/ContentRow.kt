package io.github.maniramezan.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight

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
            .let { if (onClick != null) it.clickable(role = Role.Button, onClick = onClick) else it }
            .semantics(mergeDescendants = onClick != null) {}
            .minimumTouchTargetHeight(minimumTouchTargetSize())
            .padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.x1_5)

    Row(
        modifier = rowModifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingContent != null) {
            leadingContent()
        }
        ListPrimaryTextBlock(
            title = title,
            titleStyle = AppTheme.typography.titleSmall,
            titleColor = AppTheme.colors.onSurface,
            supportingStyle = AppTheme.typography.bodySmall,
            supportingColor = AppTheme.colors.onSurfaceVariant,
            secondaryText = secondaryText,
            supportingText = supportingText,
            supportingMaxLines = if (supportingText != null) 2 else Int.MAX_VALUE,
        )
        if (trailingContent != null) {
            trailingContent()
        }
    }
}

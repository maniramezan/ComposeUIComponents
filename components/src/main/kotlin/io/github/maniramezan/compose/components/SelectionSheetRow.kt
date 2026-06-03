package io.github.maniramezan.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight

/**
 * A single themed row inside [SelectionSheetContent].
 *
 * Renders an optional leading glyph, a title with optional subtitle, and a trailing
 * indicator. When [expanded] is non-null the row is an expandable parent and shows a
 * disclosure chevron (rotated when expanded); when [expanded] is null it is a selectable
 * leaf/child and shows a checkmark when [isSelected].
 */
@Composable
internal fun SelectionSheetRow(
    title: String,
    subtitle: String?,
    leadingGlyph: String?,
    isSelected: Boolean,
    isIndented: Boolean,
    expanded: Boolean?,
    onClick: () -> Unit,
) {
    val interaction =
        if (expanded == null) {
            Modifier.selectable(selected = isSelected, role = Role.Button, onClick = onClick)
        } else {
            Modifier.clickable(role = Role.Button, onClick = onClick)
        }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .minimumTouchTargetHeight(minimumTouchTargetSize())
                .then(interaction)
                .padding(
                    start = if (isIndented) AppTheme.spacing.x4 else AppTheme.spacing.x2,
                    end = AppTheme.spacing.x2,
                    top = AppTheme.spacing.x1,
                    bottom = AppTheme.spacing.x1,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1_5),
    ) {
        if (leadingGlyph != null) {
            Text(text = leadingGlyph, style = AppTheme.typography.titleLarge)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.onSurface,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        when {
            expanded != null ->
                Icon(
                    imageVector = AppTheme.icons.expand.imageVector,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = AppTheme.colors.onSurfaceVariant,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f),
                )
            isSelected ->
                Icon(
                    imageVector = AppTheme.icons.check.imageVector,
                    contentDescription = "Selected",
                    tint = AppTheme.colors.primary,
                )
        }
    }
}

/** The comma-joined titles of [node]'s selected children, or `null` when none are selected. */
internal fun <ID : Any> selectedChildrenSummary(
    node: SelectionSheetNode<ID>,
    selectedIds: Set<ID>,
): String? {
    val titles = node.children.filter { it.id in selectedIds }.map { it.title }
    return if (titles.isEmpty()) null else titles.joinToString(", ")
}

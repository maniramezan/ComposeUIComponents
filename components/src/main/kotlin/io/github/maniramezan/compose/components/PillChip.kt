package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import io.github.maniramezan.compose.theme.AppTheme

/**
 * A capsule-shaped tappable chip that toggles between a selected and
 * unselected visual state. Typical use cases are filter rows, level
 * selectors, and persistent multi-option toggles.
 *
 * Defaults pull colors from [AppTheme.colors] so the component picks up the
 * surrounding theme automatically. Override [selectedBackground],
 * [unselectedBackground], [selectedLabel], and [unselectedLabel] when a
 * specific tint is needed (for example, per-level filter capsules).
 */
@Composable
public fun PillChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedBackground: Color = AppTheme.colors.primary,
    unselectedBackground: Color = AppTheme.colors.primaryContainer,
    selectedLabel: Color = AppTheme.colors.onPrimary,
    unselectedLabel: Color = AppTheme.colors.onSurface,
) {
    val backgroundColor = if (isSelected) selectedBackground else unselectedBackground
    val textColor = if (isSelected) selectedLabel else unselectedLabel

    Box(
        modifier =
            modifier
                .clip(AppTheme.shapes.pill)
                .background(backgroundColor)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.x1),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style =
                AppTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                ),
            color = textColor,
        )
    }
}

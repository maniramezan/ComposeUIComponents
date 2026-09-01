package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTarget

/**
 * A capsule-shaped command with arbitrary slot content.
 *
 * Unlike [PillChip], this component does not expose selection semantics. Use it
 * for compact links and commands whose labels contain multiple styled elements.
 */
@Composable
public fun ActionPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colors.surfaceVariant,
    contentColor: Color = AppTheme.colors.onSurfaceVariant,
    contentPadding: PaddingValues =
        PaddingValues(
            horizontal = AppTheme.spacing.x1_5,
            vertical = AppTheme.spacing.x1,
        ),
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(
            modifier =
                modifier
                    .minimumTouchTarget(minimumTouchTargetSize())
                    .background(containerColor, AppTheme.shapes.pill)
                    .border(AppTheme.spacing.strokeThin, AppTheme.colors.outline, AppTheme.shapes.pill)
                    .clickable(role = Role.Button, onClick = onClick)
                    .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

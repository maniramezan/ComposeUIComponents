package io.github.maniramezan.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTarget

/**
 * A themed card that keeps [summary] visible and reveals [detail] on demand.
 *
 * The expandable control's state is announced to accessibility services through
 * [expandedStateDescription] and [collapsedStateDescription]. Supply localized strings for
 * both; omitting them would leave the control without a readable expanded/collapsed state.
 *
 * @param expandedStateDescription Localized state description announced while [expanded]. Supply a localized string.
 * @param collapsedStateDescription Localized state description announced while collapsed. Supply a localized string.
 */
@Composable
public fun DisclosureCard(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    expandedStateDescription: String,
    collapsedStateDescription: String,
    summary: @Composable RowScope.() -> Unit,
    detail: @Composable () -> Unit,
) {
    val stateLabel = if (expanded) expandedStateDescription else collapsedStateDescription

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.surfaceVariant),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .minimumTouchTarget(minimumTouchTargetSize())
                    .clickable(role = Role.Button) {
                        onExpandedChange(!expanded)
                    }.semantics { stateDescription = stateLabel }
                    .padding(AppTheme.spacing.x2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Row(content = summary)
            }
            Icon(
                imageVector = AppTheme.icons.expand.imageVector,
                contentDescription = null, // @check:suppress
                modifier = Modifier.rotate(if (expanded) 180f else 0f),
                tint = AppTheme.colors.onSurfaceVariant,
            )
        }
        AnimatedVisibility(visible = expanded) {
            Box(
                modifier =
                    Modifier.padding(
                        start = AppTheme.spacing.x2,
                        end = AppTheme.spacing.x2,
                        bottom = AppTheme.spacing.x2,
                    ),
            ) {
                detail()
            }
        }
    }
}

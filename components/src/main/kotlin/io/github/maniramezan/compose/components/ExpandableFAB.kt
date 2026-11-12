package io.github.maniramezan.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.IconToken
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight

/** Direction in which an [ExpandableFAB] reveals actions. Start and end follow layout direction. */
public enum class FABExpansionDirection {
    Up,
    Down,
    Start,
    End,
}

/**
 * An icon and visible [label] for an [ExpandableFAB] action.
 *
 * [id] must be unique within the menu and stable across updates. [label] must be nonblank
 * and supplies the action's accessibility name; the icon is decorative.
 */
@Immutable
public data class FABAction(
    public val id: String,
    public val label: String,
    public val icon: IconToken,
    public val onClick: () -> Unit,
    public val enabled: Boolean = true,
) {
    init {
        require(label.isNotBlank()) { "FABAction label must not be blank" }
    }
}

/**
 * A floating action button that reveals labeled icon actions along one axis.
 *
 * Hoist [expanded] and apply [onExpandedChange] to accept toggle and dismissal requests.
 * Supply localized [contentDescription], [expandedStateDescription], and
 * [collapsedStateDescription]. Selecting an enabled action requests collapse before invoking
 * its callback when [collapseOnAction] is true. An empty [actions] list leaves a normal toggle FAB.
 *
 * Place this in an aligned container, such as a Scaffold floatingActionButton slot, to keep
 * the trigger anchored as the menu changes size. Up/start place actions before the trigger;
 * down/end place them after it. Actions scroll along the expansion axis when constrained.
 * The caller handles outside taps and back navigation if dismissal is desired.
 *
 * Theme motion tokens control interruptible fade and size transitions. Compose honors the
 * system animation duration scale. Exiting actions immediately lose interaction and semantics.
 */
@Composable
public fun ExpandableFAB(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    actions: List<FABAction>,
    icon: IconToken,
    contentDescription: String,
    expandedStateDescription: String,
    collapsedStateDescription: String,
    modifier: Modifier = Modifier,
    direction: FABExpansionDirection = FABExpansionDirection.Up,
    collapseOnAction: Boolean = true,
) {
    require(actions.map { it.id }.distinct().size == actions.size) { "FABAction ids must be unique" }
    val horizontal = direction == FABExpansionDirection.Start || direction == FABExpansionDirection.End
    val actionsFirst = direction == FABExpansionDirection.Up || direction == FABExpansionDirection.Start
    val trigger: @Composable () -> Unit = {
        FAB(
            icon = icon,
            contentDescription = contentDescription,
            onClick = { onExpandedChange(!expanded) },
            modifier =
                Modifier.semantics {
                    stateDescription = if (expanded) expandedStateDescription else collapsedStateDescription
                },
        )
    }
    if (horizontal) {
        Row(modifier = modifier, verticalAlignment = Alignment.Bottom) {
            if (!actionsFirst) trigger()
            FABActionMenu(
                expanded = expanded,
                actions = actions,
                horizontal = true,
                actionsFirst = actionsFirst,
                modifier = Modifier.weight(1f, fill = false),
                onAction = { action ->
                    if (collapseOnAction) onExpandedChange(false)
                    action.onClick()
                },
            )
            if (actionsFirst) trigger()
        }
    } else {
        Column(modifier = modifier, horizontalAlignment = Alignment.End) {
            if (!actionsFirst) trigger()
            FABActionMenu(
                expanded = expanded,
                actions = actions,
                horizontal = false,
                actionsFirst = actionsFirst,
                modifier = Modifier.weight(1f, fill = false),
                onAction = { action ->
                    if (collapseOnAction) onExpandedChange(false)
                    action.onClick()
                },
            )
            if (actionsFirst) trigger()
        }
    }
}

@Composable
private fun FABActionMenu(
    expanded: Boolean,
    actions: List<FABAction>,
    horizontal: Boolean,
    actionsFirst: Boolean,
    modifier: Modifier,
    onAction: (FABAction) -> Unit,
) {
    val duration = AppTheme.motion.mediumMillis
    val easing = AppTheme.motion.emphasizedEasing
    val horizontalAlignment = if (actionsFirst) Alignment.End else Alignment.Start
    val verticalAlignment = if (actionsFirst) Alignment.Bottom else Alignment.Top
    val enter =
        if (horizontal) {
            expandHorizontally(tween(duration, easing = easing), expandFrom = horizontalAlignment)
        } else {
            expandVertically(tween(duration, easing = easing), expandFrom = verticalAlignment)
        }
    val exit =
        if (horizontal) {
            shrinkHorizontally(tween(duration, easing = easing), shrinkTowards = horizontalAlignment)
        } else {
            shrinkVertically(tween(duration, easing = easing), shrinkTowards = verticalAlignment)
        }
    AnimatedVisibility(
        visible = expanded && actions.isNotEmpty(),
        modifier = modifier,
        enter = enter + fadeIn(tween(duration, easing = easing)),
        exit = exit + fadeOut(tween(duration, easing = easing)),
    ) {
        val content: @Composable () -> Unit = {
            actions.forEach { action ->
                key(action.id) {
                    FABActionButton(action = action, enabled = expanded && action.enabled, onClick = { onAction(action) })
                }
            }
        }
        BoxWithConstraints {
            val gap = AppTheme.spacing.x1
            val semantics = if (expanded) Modifier else Modifier.clearAndSetSemantics { }
            val scrollState = rememberScrollState()
            if (horizontal) {
                Row(
                    modifier =
                        semantics
                            .padding(if (actionsFirst) PaddingValues(end = gap) else PaddingValues(start = gap))
                            .then(if (constraints.hasBoundedWidth) Modifier.horizontalScroll(scrollState) else Modifier),
                    horizontalArrangement = Arrangement.spacedBy(gap),
                    verticalAlignment = Alignment.CenterVertically,
                ) { content() }
            } else {
                Column(
                    modifier =
                        semantics
                            .padding(if (actionsFirst) PaddingValues(bottom = gap) else PaddingValues(top = gap))
                            .then(if (constraints.hasBoundedHeight) Modifier.verticalScroll(scrollState) else Modifier),
                    verticalArrangement = Arrangement.spacedBy(gap),
                    horizontalAlignment = Alignment.End,
                ) { content() }
            }
        }
    }
}

@Composable
private fun FABActionButton(
    action: FABAction,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.minimumTouchTargetHeight(minimumTouchTargetSize()),
        shape = AppTheme.shapes.pill,
        colors =
            ButtonDefaults.filledTonalButtonColors(
                containerColor = AppTheme.colors.surfaceVariant,
                contentColor = AppTheme.colors.onSurfaceVariant,
            ),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = action.icon.imageVector,
                contentDescription = null, // @check:suppress
                modifier = Modifier.size(standardIconSize()),
            )
            Text(text = action.label, style = AppTheme.typography.labelLarge)
        }
    }
}

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
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
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
 * and supplies the action's accessibility name; the icon is decorative. This is a plain class
 * rather than a data class so new properties can be added without breaking binary compatibility.
 */
@Immutable
public class FABAction(
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
 * its callback when [collapseOnAction] is true. An empty [actions] list leaves a normal toggle FAB
 * that does not report expanded or collapsed state.
 *
 * Place this in an aligned container, such as a Scaffold floatingActionButton slot, to keep
 * the trigger anchored as the menu changes size. Up/start place actions before the trigger;
 * down/end place them after it. Actions scroll along the expansion axis when constrained,
 * starting at the end nearest the trigger. In a parent with unbounded space along the
 * expansion axis, such as a scrolling container, the menu takes its full size.
 * The caller handles outside taps and back navigation if dismissal is desired.
 *
 * Theme motion tokens control interruptible fade and size transitions. Compose honors the
 * system animation duration scale. Exiting actions immediately lose interaction and semantics.
 *
 * @throws IllegalArgumentException if two [actions] share an [FABAction.id].
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
    val seenIds = HashSet<String>(actions.size)
    require(actions.all { seenIds.add(it.id) }) { "FABAction ids must be unique" }
    val horizontal = direction == FABExpansionDirection.Start || direction == FABExpansionDirection.End
    val actionsFirst = direction == FABExpansionDirection.Up || direction == FABExpansionDirection.Start
    val hasActions = actions.isNotEmpty()
    val measurePolicy = remember(horizontal, actionsFirst) { FABMeasurePolicy(horizontal, actionsFirst) }
    Layout(
        content = {
            Box {
                FABActionMenu(
                    expanded = expanded,
                    actions = actions,
                    horizontal = horizontal,
                    actionsFirst = actionsFirst,
                    onAction = { action ->
                        if (collapseOnAction) onExpandedChange(false)
                        action.onClick()
                    },
                )
            }
            Box {
                FAB(
                    icon = icon,
                    contentDescription = contentDescription,
                    onClick = { onExpandedChange(!expanded) },
                    modifier =
                        Modifier.semantics {
                            if (hasActions) {
                                stateDescription = if (expanded) expandedStateDescription else collapsedStateDescription
                            }
                        },
                )
            }
        },
        modifier = modifier,
        measurePolicy = measurePolicy,
    )
}

/**
 * Measures the trigger first so it is never squeezed out, then gives the menu the remaining
 * space along the expansion axis. When that axis is unbounded the menu is limited to its own
 * intrinsic size, which keeps its scroll container from receiving infinite constraints.
 */
private class FABMeasurePolicy(
    private val horizontal: Boolean,
    private val actionsFirst: Boolean,
) : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult {
        val menu = measurables[0]
        val trigger = measurables[1]
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val triggerPlaceable = trigger.measure(loose)
        val menuConstraints =
            if (horizontal) {
                val available =
                    if (loose.hasBoundedWidth) {
                        loose.maxWidth - triggerPlaceable.width
                    } else {
                        menu.maxIntrinsicWidth(loose.maxHeight)
                    }
                loose.copy(maxWidth = available.coerceAtLeast(0))
            } else {
                val available =
                    if (loose.hasBoundedHeight) {
                        loose.maxHeight - triggerPlaceable.height
                    } else {
                        menu.maxIntrinsicHeight(loose.maxWidth)
                    }
                loose.copy(maxHeight = available.coerceAtLeast(0))
            }
        val menuPlaceable = menu.measure(menuConstraints)
        val width =
            if (horizontal) {
                triggerPlaceable.width + menuPlaceable.width
            } else {
                maxOf(triggerPlaceable.width, menuPlaceable.width)
            }
        val height =
            if (horizontal) {
                maxOf(triggerPlaceable.height, menuPlaceable.height)
            } else {
                triggerPlaceable.height + menuPlaceable.height
            }
        val layoutWidth = constraints.constrainWidth(width)
        val layoutHeight = constraints.constrainHeight(height)
        return layout(layoutWidth, layoutHeight) {
            if (horizontal) {
                val menuX = if (actionsFirst) 0 else triggerPlaceable.width
                val triggerX = if (actionsFirst) menuPlaceable.width else 0
                menuPlaceable.placeRelative(menuX, layoutHeight - menuPlaceable.height)
                triggerPlaceable.placeRelative(triggerX, layoutHeight - triggerPlaceable.height)
            } else {
                val menuY = if (actionsFirst) 0 else triggerPlaceable.height
                val triggerY = if (actionsFirst) menuPlaceable.height else 0
                menuPlaceable.placeRelative(layoutWidth - menuPlaceable.width, menuY)
                triggerPlaceable.placeRelative(layoutWidth - triggerPlaceable.width, triggerY)
            }
        }
    }
}

@Composable
private fun FABActionMenu(
    expanded: Boolean,
    actions: List<FABAction>,
    horizontal: Boolean,
    actionsFirst: Boolean,
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
        enter = enter + fadeIn(tween(duration, easing = easing)),
        exit = exit + fadeOut(tween(duration, easing = easing)),
    ) {
        val gap = AppTheme.spacing.x1
        val scrollState = rememberScrollState()
        LaunchedEffect(actionsFirst, scrollState.maxValue) {
            if (actionsFirst) scrollState.scrollTo(scrollState.maxValue)
        }
        val interaction =
            if (expanded) {
                Modifier
            } else {
                Modifier.clearAndSetSemantics { }.consumeAllPointerInput()
            }
        val content: @Composable () -> Unit = {
            actions.forEach { action ->
                key(action.id) {
                    FABActionButton(action = action, onClick = { onAction(action) })
                }
            }
        }
        if (horizontal) {
            Row(
                modifier =
                    interaction
                        .padding(if (actionsFirst) PaddingValues(end = gap) else PaddingValues(start = gap))
                        .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(gap),
                verticalAlignment = Alignment.CenterVertically,
            ) { content() }
        } else {
            Column(
                modifier =
                    interaction
                        .padding(if (actionsFirst) PaddingValues(bottom = gap) else PaddingValues(top = gap))
                        .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(gap),
                horizontalAlignment = Alignment.End,
            ) { content() }
        }
    }
}

private fun Modifier.consumeAllPointerInput(): Modifier =
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                awaitPointerEvent(PointerEventPass.Initial).changes.forEach { it.consume() }
            }
        }
    }

@Composable
private fun FABActionButton(
    action: FABAction,
    onClick: () -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        enabled = action.enabled,
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
                // @check:suppress decorative; the adjacent text labels the action
                contentDescription = null,
                modifier = Modifier.size(standardIconSize()),
            )
            Text(text = action.label, style = AppTheme.typography.labelLarge)
        }
    }
}

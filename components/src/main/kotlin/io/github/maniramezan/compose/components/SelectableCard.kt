package io.github.maniramezan.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTarget

/**
 * A card surface for picking one option from a set, such as a plan, a tier, or a delivery
 * method. The whole card is one selectable target: tapping it calls [onClick], and its content
 * is merged into a single TalkBack focus stop.
 *
 * Selection is shown with an outline drawn inside the card bounds, so toggling [selected]
 * recolors and thickens the outline without changing the card's size or moving its content.
 * The outline uses `AppTheme.colors.primary` when selected and `AppTheme.colors.outlineVariant`
 * otherwise.
 *
 * The component is state-hoisted: it never changes [selected] itself. Keep the selected option
 * in your own state and pass `selected = option == current` to each card.
 *
 * ## Single-choice groups
 *
 * Place the cards in a container that applies `Modifier.selectableGroup()`, such as a
 * `Column`, so TalkBack announces them as one group of mutually exclusive options. The
 * component does not apply the group semantics itself because it cannot see its siblings.
 *
 * ```kotlin
 * Column(
 *     modifier = Modifier.selectableGroup(),
 *     verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
 * ) {
 *     plans.forEach { plan ->
 *         SelectableCard(
 *             selected = plan == current,
 *             onClick = { current = plan },
 *             modifier = Modifier.fillMaxWidth(),
 *         ) {
 *             Text(plan.name)
 *         }
 *     }
 * }
 * ```
 *
 * The card keeps at least a 48dp touch target. It does not fill the available width by default;
 * pass `Modifier.fillMaxWidth()` or `Modifier.weight(1f)` to size it. While [enabled] is
 * `false` the card is dimmed, ignores taps, and is exposed as disabled to accessibility
 * services, but still shows its selected outline.
 *
 * @param selected Whether this option is the current choice.
 * @param onClick Called when the user taps the card. Select this option in the callback.
 * @param modifier Applied to the card.
 * @param enabled Whether the card can be selected.
 * @param stateDescription Optional caller-supplied, localized state text announced by
 *   TalkBack in place of the default "selected" / "not selected" state, for example
 *   "Current plan". Blank or `null` keeps the platform wording. There is no English default
 *   (ADR 0004).
 * @param role Accessibility role. Defaults to [Role.RadioButton] for single-choice groups; pass
 *   [Role.Checkbox] when several cards can be selected at once.
 * @param shape Shape used to clip the card and draw its outline.
 * @param containerColor Fill color of the card.
 * @param contentColor Default content color for text and icons inside the card.
 * @param contentPadding Padding between the card edge and [content].
 * @param content Card content, laid out in a column.
 */
@Composable
public fun SelectableCard(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    stateDescription: String? = null,
    role: Role = Role.RadioButton,
    shape: Shape = AppTheme.shapes.large,
    containerColor: Color = AppTheme.colors.surfaceVariant,
    contentColor: Color = AppTheme.colors.onSurfaceVariant,
    contentPadding: PaddingValues = PaddingValues(AppTheme.spacing.lg),
    content: @Composable ColumnScope.() -> Unit,
) {
    val outline =
        if (selected) {
            BorderStroke(AppTheme.spacing.strokeRegular, AppTheme.colors.primary)
        } else {
            BorderStroke(AppTheme.spacing.strokeThin, AppTheme.colors.outlineVariant)
        }
    val customState = stateDescription?.takeIf { it.isNotBlank() }

    Column(
        modifier =
            modifier
                .graphicsLayer { alpha = if (enabled) 1f else DISABLED_CONTENT_ALPHA }
                .minimumTouchTarget(minimumTouchTargetSize())
                .clip(shape)
                .background(containerColor)
                .border(outline, shape)
                .selectable(selected = selected, enabled = enabled, role = role, onClick = onClick)
                .then(
                    if (customState != null) {
                        Modifier.semantics { this.stateDescription = customState }
                    } else {
                        Modifier
                    },
                ).padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

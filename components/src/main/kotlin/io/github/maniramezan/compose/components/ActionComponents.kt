package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.IconToken
import io.github.maniramezan.compose.utils.minimumTouchTarget
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight
import androidx.compose.material3.ExtendedFloatingActionButton as MaterialExtendedFloatingActionButton
import androidx.compose.material3.IconButton as MaterialIconButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow as MaterialSingleChoiceSegmentedButtonRow
import androidx.compose.material3.TextButton as MaterialTextButton

/** A themed, outlined medium-emphasis button for a secondary action. */
@Composable
public fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.minimumTouchTargetHeight(minimumTouchTargetSize()),
        enabled = enabled,
        colors =
            ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.colors.primary,
            ),
        contentPadding =
            PaddingValues(
                horizontal = AppTheme.spacing.lg,
                vertical = AppTheme.spacing.sm,
            ),
    ) {
        Text(text = text)
    }
}

/** A themed, low-emphasis text button, typically for a tertiary or dismissive action. */
@Composable
public fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    MaterialTextButton(
        onClick = onClick,
        modifier = modifier.minimumTouchTargetHeight(minimumTouchTargetSize()),
        enabled = enabled,
        colors =
            ButtonDefaults.textButtonColors(
                contentColor = AppTheme.colors.primary,
            ),
        contentPadding =
            PaddingValues(
                horizontal = AppTheme.spacing.md,
                vertical = AppTheme.spacing.sm,
            ),
    ) {
        Text(text = text)
    }
}

/**
 * A themed, icon-only tappable button.
 *
 * @param contentDescription Accessibility label announced for this control; the icon
 *   carries no visible text, so this must describe the action (e.g. "Delete").
 */
@Composable
public fun IconButton(
    icon: IconToken,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    MaterialIconButton(
        onClick = onClick,
        modifier = modifier.minimumTouchTarget(minimumTouchTargetSize()),
        enabled = enabled,
    ) {
        Icon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            tint = AppTheme.colors.primary,
        )
    }
}

/**
 * A themed, icon-only floating action button for a screen's primary action.
 *
 * @param contentDescription Accessibility label announced for this control; the icon
 *   carries no visible text, so this must describe the action (e.g. "Compose").
 */
@Composable
public fun FAB(
    icon: IconToken,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.minimumTouchTarget(minimumTouchTargetSize()),
        containerColor = AppTheme.colors.primary,
        contentColor = AppTheme.colors.onPrimary,
    ) {
        Icon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(standardIconSize()),
        )
    }
}

/**
 * A simple segmented control that renders one [Button] (selected) or a
 * [Button] with `AppColors.surfaceVariant` background (unselected) per entry
 * in [options].
 *
 * @param options Labels for each segment.
 * @param selectedIndex Index of the currently selected segment.
 * @param onOptionSelected Called with the tapped index.
 * @param modifier Modifier applied to the outer [Row].
 * @param enabled Whether all segments respond to interaction.
 * @param density [SegmentDensity.Regular] uses standard button padding;
 *   [SegmentDensity.Compact] reduces vertical padding for a shorter control.
 *   The 48dp minimum touch target is preserved in both modes.
 * @param widthMode [SegmentWidthMode.Fill] distributes buttons evenly across
 *   the full available width; [SegmentWidthMode.Fit] lets each button wrap to
 *   its content width.
 */
@Composable
public fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    density: SegmentDensity = SegmentDensity.Regular,
    widthMode: SegmentWidthMode = SegmentWidthMode.Fill,
) {
    val verticalPadding =
        if (density == SegmentDensity.Compact) AppTheme.spacing.half else AppTheme.spacing.sm
    val contentPadding =
        PaddingValues(
            horizontal = AppTheme.spacing.lg,
            vertical = verticalPadding,
        )
    Row(
        modifier = modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            val baseModifier =
                Modifier
                    .minimumTouchTargetHeight(minimumTouchTargetSize())
                    .semantics { this.selected = selected }
            val segmentModifier =
                if (widthMode == SegmentWidthMode.Fill) baseModifier.weight(1f) else baseModifier
            if (selected) {
                Button(
                    onClick = { onOptionSelected(index) },
                    enabled = enabled,
                    modifier = segmentModifier,
                    contentPadding = contentPadding,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.primary,
                            contentColor = AppTheme.colors.onPrimary,
                        ),
                ) { Text(text = option) }
            } else {
                Button(
                    onClick = { onOptionSelected(index) },
                    enabled = enabled,
                    modifier = segmentModifier,
                    contentPadding = contentPadding,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.surfaceVariant,
                            contentColor = AppTheme.colors.onSurfaceVariant,
                        ),
                ) { Text(text = option) }
            }
        }
    }
}

/**
 * An extended FAB that pairs [icon] with a visible [text] label.
 *
 * The icon is always rendered decoratively (description omitted): the
 * visible [text] already labels the action, and Material3 merges the icon and
 * text into a single accessibility node, so a separate icon description would
 * only cause TalkBack to announce the label twice.
 */
@Composable
public fun ExtendedFloatingActionButton(
    text: String,
    icon: IconToken,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = AppTheme.colors.primary,
        contentColor = AppTheme.colors.onPrimary,
        icon = {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = null, // @check:suppress — decorative; text already labels the action
                modifier = Modifier.size(standardIconSize()),
            )
        },
        text = { Text(text = text) },
    )
}

/**
 * Deprecated overload retained for one minor release for binary compatibility.
 * The icon is always decorative (see the overload above), so [contentDescription]
 * has no effect.
 */
@Deprecated(
    message =
        "contentDescription is unused; the icon is always decorative because the " +
            "visible text label already names the action.",
    replaceWith = ReplaceWith("ExtendedFloatingActionButton(text, icon, onClick, modifier)"),
)
@Composable
public fun ExtendedFloatingActionButton(
    text: String,
    icon: IconToken,
    @Suppress("UNUSED_PARAMETER") contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(text = text, icon = icon, onClick = onClick, modifier = modifier)
}

/**
 * A single-choice segmented button row, Material3's inline alternative to
 * [SegmentedControl] for choosing exactly one option from [options].
 */
@Composable
public fun SingleChoiceSegmentedButtonRow(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    MaterialSingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = index == selectedIndex,
                onClick = { onOptionSelected(index) },
                modifier = Modifier.minimumTouchTargetHeight(minimumTouchTargetSize()),
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                enabled = enabled,
                icon = { SegmentedButtonDefaults.Icon(active = index == selectedIndex) },
            ) {
                Text(text = option)
            }
        }
    }
}

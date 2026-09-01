package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.LevelTier
import io.github.maniramezan.compose.utils.selectableRole

/**
 * A capsule-shaped chip that toggles between a selected and unselected
 * visual state. Typical use cases are filter rows, level selectors, and
 * persistent multi-option toggles.
 *
 * Pass [onClick] to make the chip tappable — it gets a minimum touch target,
 * [Role.Button] semantics, and a selectable click handler. Omit [onClick]
 * (leave it `null`) to render a static, non-interactive badge instead, for
 * example a tiered indicator built from the [PillChip] overload below.
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
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    selectedBackground: Color = AppTheme.colors.primary,
    unselectedBackground: Color = AppTheme.colors.primaryContainer,
    selectedLabel: Color = AppTheme.colors.onPrimary,
    unselectedLabel: Color = AppTheme.colors.onSurface,
) {
    val backgroundColor = if (isSelected) selectedBackground else unselectedBackground
    val textColor = if (isSelected) selectedLabel else unselectedLabel

    var chipModifier = modifier
    if (onClick != null) {
        chipModifier = chipModifier.selectableRole(isSelected, onClick, minimumTouchTarget = minimumTouchTargetSize())
    }
    chipModifier = chipModifier.pillSurface(backgroundColor)
    chipModifier = chipModifier.padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.x1)

    Box(
        modifier = chipModifier,
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

/**
 * A static, non-interactive [PillChip] tinted with a [LevelTier] from a
 * [io.github.maniramezan.compose.theme.LevelPalette]. Use for tiered
 * indicators (skill levels, difficulty, priority, …) where the tint encodes
 * the tier and [label] spells it out.
 *
 * Callers typically read a tier via
 * `AppTheme.colors.levels.tier(level.ordinal)` and pass it here, decoupling
 * the component from the app's level taxonomy.
 */
@Composable
public fun PillChip(
    label: String,
    tier: LevelTier,
    modifier: Modifier = Modifier,
) {
    PillChip(
        label = label,
        isSelected = true,
        modifier = modifier,
        selectedBackground = tier.background,
        selectedLabel = tier.foreground,
    )
}

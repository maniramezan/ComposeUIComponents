package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.LevelTier

/**
 * A small inline badge labeled with text and tinted with a [LevelTier] from a
 * [io.github.maniramezan.compose.theme.LevelPalette]. Use for tiered indicators
 * (skill levels, difficulty, priority, …) where the tint encodes the tier
 * and the label spells it out.
 *
 * Callers typically read a tier via
 * `AppTheme.colors.levels.tier(level.ordinal)` and pass it here, decoupling
 * the component from the app's level taxonomy.
 */
@Composable
public fun LevelBadge(
    label: String,
    tier: LevelTier,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(AppTheme.shapes.badge)
                .background(tier.background)
                .padding(horizontal = AppTheme.spacing.x1, vertical = AppTheme.spacing.half),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.labelSmall,
            color = tier.foreground,
        )
    }
}

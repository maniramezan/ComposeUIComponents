package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.PillChip
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Chips & Badges
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun PillChipPage() {
    var selected by remember { mutableIntStateOf(0) }
    val options = listOf("All", "Beginner", "Intermediate", "Advanced")

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            options.forEachIndexed { i, label ->
                PillChip(label = label, isSelected = i == selected, onClick = { selected = i })
            }
        }
        Text(text = "Selected: ${options[selected]}")
    }
}

@Composable
internal fun PillChipTierBadgePage() {
    val tiers = listOf("Tier 0", "Tier 1", "Tier 2")
    var tierIndex by remember { mutableIntStateOf(0) }
    val labels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
    var labelIndex by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        PillChip(
            label = labels[labelIndex],
            tier = AppTheme.colors.levels.tier(tierIndex),
        )
        ControlsDivider()
        ControlSegmented(
            label = "Tier",
            options = tiers,
            selectedIndex = tierIndex,
            onOptionSelected = { tierIndex = it },
        )
        ControlSegmented(
            label = "Label",
            options = labels,
            selectedIndex = labelIndex,
            onOptionSelected = { labelIndex = it },
        )
    }
}

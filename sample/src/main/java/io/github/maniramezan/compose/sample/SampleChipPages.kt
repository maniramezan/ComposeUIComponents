package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.components.Badge
import io.github.maniramezan.compose.components.PillChip
import io.github.maniramezan.compose.theme.AppTheme
import kotlin.math.roundToInt

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

    SamplePage(
        preview = {
            PillChip(
                label = labels[labelIndex],
                tier = AppTheme.colors.levels.tier(tierIndex),
            )
        },
        controls = {
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
        },
    )
}

@Composable
internal fun BadgePage() {
    var showCount by remember { mutableStateOf(true) }
    var count by remember { mutableIntStateOf(3) }
    var lowCap by remember { mutableStateOf(false) }
    var describe by remember { mutableStateOf(false) }
    val maxCount = if (lowCap) 9 else 99
    val badgeCount = if (showCount) count else null
    val description = if (describe) "$count unread items" else ""

    SamplePage(
        preview = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x3),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Badge(count = badgeCount, maxCount = maxCount, contentDescription = description)
                // Pinned to an icon's corner, as in a TabBarItemData badge slot.
                Box {
                    Icon(imageVector = AppTheme.icons.check.imageVector, contentDescription = null)
                    Badge(
                        count = badgeCount,
                        maxCount = maxCount,
                        contentDescription = description,
                        modifier =
                            Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = AppTheme.spacing.x1, y = -AppTheme.spacing.x1),
                    )
                }
            }
        },
        controls = {
            ControlSwitch(label = "Show count (off = dot)", checked = showCount, onCheckedChange = { showCount = it })
            ControlSlider(
                label = "Count: $count",
                value = count.toFloat(),
                onValueChange = { count = it.roundToInt() },
                valueRange = 0f..150f,
            )
            ControlSwitch(label = "Cap at 9", checked = lowCap, onCheckedChange = { lowCap = it })
            ControlSwitch(label = "Custom description", checked = describe, onCheckedChange = { describe = it })
        },
    )
}

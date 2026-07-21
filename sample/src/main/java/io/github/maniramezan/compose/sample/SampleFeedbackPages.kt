package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.ProgressIndicator
import io.github.maniramezan.compose.components.Skeleton
import io.github.maniramezan.compose.components.SkeletonBlock
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Feedback
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun ProgressIndicatorPage() {
    var determinate by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0.45f) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        ProgressIndicator(
            progress = if (determinate) progress else null,
            label =
                if (determinate) {
                    "Progress: ${(progress * 100).toInt()}%"
                } else {
                    "Loading…"
                },
        )
        ControlsDivider()
        ControlSwitch(
            label = "Determinate",
            checked = determinate,
            onCheckedChange = { determinate = it },
        )
        if (determinate) {
            ControlSlider(
                label = "Progress: ${(progress * 100).toInt()}%",
                value = progress,
                onValueChange = { progress = it },
            )
        }
    }
}

@Composable
internal fun SkeletonPage() {
    var count by remember { mutableIntStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        repeat(count) { Skeleton() }
        ControlsDivider()
        ControlSegmented(
            label = "Count",
            options = listOf("1", "2", "3"),
            selectedIndex = count - 1,
            onOptionSelected = { count = it + 1 },
        )
    }
}

@Composable
internal fun SkeletonBlockPage() {
    var heightFraction by remember { mutableFloatStateOf(0.5f) }
    var widthFraction by remember { mutableFloatStateOf(0.5f) }
    val height: Dp = (40 + (heightFraction * 120)).dp
    val width: Dp = (40 + (widthFraction * 200)).dp

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SkeletonBlock(height = height, width = width)
        ControlsDivider()
        ControlSlider(
            label = "Height: ${height.value.toInt()} dp",
            value = heightFraction,
            onValueChange = { heightFraction = it },
        )
        ControlSlider(
            label = "Width: ${width.value.toInt()} dp",
            value = widthFraction,
            onValueChange = { widthFraction = it },
        )
    }
}

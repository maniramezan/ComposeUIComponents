package io.github.maniramezan.compose.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.ActionPill
import io.github.maniramezan.compose.components.AdaptiveContentContainer
import io.github.maniramezan.compose.components.Card
import io.github.maniramezan.compose.components.FlowLayout
import io.github.maniramezan.compose.theme.AppTheme
import kotlin.math.roundToInt

@Composable
internal fun FlowLayoutPage() {
    var itemCount by remember { mutableIntStateOf(6) }
    var longLabels by remember { mutableStateOf(false) }
    var roomySpacing by remember { mutableStateOf(false) }
    val labels =
        if (longLabels) {
            listOf("Alpha item", "Longer beta item", "Gamma item", "Delta item", "Epsilon item", "Zeta item")
        } else {
            listOf("Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta")
        }
    val spacing = if (roomySpacing) AppTheme.spacing.x2 else AppTheme.spacing.x1

    SamplePage(
        preview = {
            FlowLayout(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing),
            ) {
                labels.take(itemCount).forEach { label ->
                    ActionPill(onClick = {}) { Text(text = label) }
                }
            }
        },
        controls = {
            ControlSegmented(
                label = "Item count",
                options = listOf("2", "4", "6"),
                selectedIndex = itemCount / 2 - 1,
                onOptionSelected = { itemCount = (it + 1) * 2 },
            )
            ControlSwitch(label = "Long labels", checked = longLabels, onCheckedChange = { longLabels = it })
            ControlSwitch(label = "Roomy spacing", checked = roomySpacing, onCheckedChange = { roomySpacing = it })
        },
    )
}

@Composable
internal fun AdaptiveContentContainerPage() {
    var maxWidth by remember { mutableFloatStateOf(240f) }
    val alignments = listOf("TopCenter", "TopStart", "TopEnd")
    var alignmentIndex by remember { mutableIntStateOf(0) }

    SamplePage(
        preview = {
            // The tinted backdrop shows the full available width; the card shows the capped width.
            Box(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.surfaceVariant)) {
                AdaptiveContentContainer(
                    maxWidth = maxWidth.dp,
                    contentAlignment =
                        when (alignmentIndex) {
                            1 -> Alignment.TopStart
                            2 -> Alignment.TopEnd
                            else -> Alignment.TopCenter
                        },
                ) {
                    Card { Text("Content capped at ${maxWidth.roundToInt()}dp") }
                }
            }
        },
        controls = {
            ControlSlider(
                label = "maxWidth: ${maxWidth.roundToInt()}dp",
                value = maxWidth,
                onValueChange = { maxWidth = it },
                valueRange = 120f..840f,
            )
            ControlSegmented(
                label = "contentAlignment",
                options = alignments,
                selectedIndex = alignmentIndex,
                onOptionSelected = { alignmentIndex = it },
            )
        },
    )
}

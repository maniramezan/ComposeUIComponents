package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.compose.components.ActionPill
import io.github.maniramezan.compose.components.AdaptiveContentContainer
import io.github.maniramezan.compose.components.Card
import io.github.maniramezan.compose.components.FlowLayout
import io.github.maniramezan.compose.components.HorizontalDivider
import io.github.maniramezan.compose.components.VerticalDivider
import io.github.maniramezan.compose.theme.AppTheme

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
    var widthIndex by remember { mutableIntStateOf(1) }
    val maxWidths = listOf(AppTheme.spacing.x9 * 3, AppTheme.spacing.x9 * 5, Dp.Infinity)

    SamplePage(
        preview = {
            AdaptiveContentContainer(maxWidth = maxWidths[widthIndex]) {
                Card {
                    Text(text = "Content capped at a readable width")
                    Text(text = "Centered once the available width exceeds the cap.")
                }
            }
        },
        controls = {
            ControlSegmented(
                label = "Max width",
                options = listOf("Narrow", "Medium", "Unbounded"),
                selectedIndex = widthIndex,
                onOptionSelected = { widthIndex = it },
            )
        },
    )
}

@Composable
internal fun DividersPage() {
    var thicknessIndex by remember { mutableIntStateOf(0) }
    val thicknesses = listOf(AppTheme.spacing.strokeThin, AppTheme.spacing.strokeRegular, AppTheme.spacing.strokeThick)
    val thickness = thicknesses[thicknessIndex]

    SamplePage(
        preview = {
            Text(text = "Above the horizontal divider")
            HorizontalDivider(thickness = thickness)
            Text(text = "Below the horizontal divider")
            Row(
                modifier = Modifier.height(AppTheme.spacing.x5),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
            ) {
                Text(text = "Start")
                VerticalDivider(thickness = thickness)
                Text(text = "End")
            }
        },
        controls = {
            ControlSegmented(
                label = "Thickness",
                options = listOf("Thin", "Regular", "Thick"),
                selectedIndex = thicknessIndex,
                onOptionSelected = { thicknessIndex = it },
            )
        },
    )
}

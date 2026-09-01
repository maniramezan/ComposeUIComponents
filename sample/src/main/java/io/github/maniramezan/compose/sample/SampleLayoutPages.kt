package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.ActionPill
import io.github.maniramezan.compose.components.FlowLayout
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

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        FlowLayout(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            labels.take(itemCount).forEach { label ->
                ActionPill(onClick = {}) { Text(text = label) }
            }
        }
        ControlsDivider()
        ControlSegmented(
            label = "Item count",
            options = listOf("2", "4", "6"),
            selectedIndex = itemCount / 2 - 1,
            onOptionSelected = { itemCount = (it + 1) * 2 },
        )
        ControlSwitch(label = "Long labels", checked = longLabels, onCheckedChange = { longLabels = it })
        ControlSwitch(label = "Roomy spacing", checked = roomySpacing, onCheckedChange = { roomySpacing = it })
    }
}

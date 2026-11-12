package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.components.ExpandableFAB
import io.github.maniramezan.compose.components.FABAction
import io.github.maniramezan.compose.components.FABExpansionDirection
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.theme.AppTheme

@Composable
internal fun ExpandableFABPage() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var directionIndex by rememberSaveable { mutableIntStateOf(0) }
    var count by rememberSaveable { mutableIntStateOf(3) }
    var actionsEnabled by rememberSaveable { mutableStateOf(true) }
    var collapseOnAction by rememberSaveable { mutableStateOf(true) }
    var longLabels by rememberSaveable { mutableStateOf(false) }
    var iconIndex by rememberSaveable { mutableIntStateOf(0) }
    var triggerLabel by rememberSaveable { mutableStateOf("Actions") }
    var expandedLabel by rememberSaveable { mutableStateOf("Expanded") }
    var collapsedLabel by rememberSaveable { mutableStateOf("Collapsed") }
    var selected by rememberSaveable { mutableStateOf("None") }
    val direction = FABExpansionDirection.entries[directionIndex]
    val actions =
        List(count) { index ->
            val label = if (longLabels) "Action ${index + 1} with a longer label" else "Action ${index + 1}"
            FABAction(
                id = index.toString(),
                label = label,
                icon = AppTheme.icons.check,
                enabled = actionsEnabled,
                onClick = { selected = label },
            )
        }
    val alignment =
        when (direction) {
            FABExpansionDirection.Up, FABExpansionDirection.Start -> Alignment.BottomEnd
            FABExpansionDirection.Down -> Alignment.TopEnd
            FABExpansionDirection.End -> Alignment.BottomStart
        }
    SamplePage(
        preview = {
            Box(
                modifier = Modifier.fillMaxWidth().height(AppTheme.spacing.x9 * 4),
                contentAlignment = alignment,
            ) {
                ExpandableFAB(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    actions = actions,
                    icon = if (iconIndex == 0) AppTheme.icons.expand else AppTheme.icons.check,
                    contentDescription = triggerLabel,
                    expandedStateDescription = expandedLabel,
                    collapsedStateDescription = collapsedLabel,
                    direction = direction,
                    collapseOnAction = collapseOnAction,
                )
            }
            Text("Selected: $selected")
        },
        controls = {
            ControlSwitch("Expanded", expanded) { expanded = it }
            ControlSegmented("Direction", FABExpansionDirection.entries.map { it.name }, directionIndex) { directionIndex = it }
            ControlSlider("Action count: $count", count.toFloat(), { count = it.toInt() }, 0f..6f)
            ControlSwitch("Actions enabled", actionsEnabled) { actionsEnabled = it }
            ControlSwitch("Collapse on action", collapseOnAction) { collapseOnAction = it }
            ControlSwitch("Long labels", longLabels) { longLabels = it }
            ControlSegmented("Trigger icon", listOf("Expand", "Check"), iconIndex) { iconIndex = it }
            TextField(value = triggerLabel, onValueChange = { triggerLabel = it }, label = "Trigger description")
            TextField(value = expandedLabel, onValueChange = { expandedLabel = it }, label = "Expanded description")
            TextField(value = collapsedLabel, onValueChange = { collapsedLabel = it }, label = "Collapsed description")
        },
    )
}

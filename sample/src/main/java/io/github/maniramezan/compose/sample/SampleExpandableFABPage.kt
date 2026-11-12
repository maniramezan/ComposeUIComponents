package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
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
    var selectedLabel by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedIconIndex by rememberSaveable { mutableIntStateOf(0) }
    var showSelectedAction by rememberSaveable { mutableStateOf(false) }
    var showTriggerLabel by rememberSaveable { mutableStateOf(false) }
    var showActionLabels by rememberSaveable { mutableStateOf(true) }
    var rightToLeft by rememberSaveable { mutableStateOf(false) }
    val direction = FABExpansionDirection.entries[directionIndex]
    val actions =
        List(count) { index ->
            val label = if (longLabels) "Action ${index + 1} with a longer label" else "Action ${index + 1}"
            FABAction(
                id = index.toString(),
                label = label,
                icon = FloatingActionIcon.entries[index % FloatingActionIcon.entries.size].token,
                enabled = actionsEnabled,
                onClick = {
                    selectedLabel = label
                    selectedIconIndex = index % FloatingActionIcon.entries.size
                },
            )
        }
    val usesSelection = showSelectedAction && selectedLabel != null
    val displayedLabel = if (usesSelection) selectedLabel.orEmpty() else triggerLabel
    val displayedIcon = FloatingActionIcon.entries[if (usesSelection) selectedIconIndex else iconIndex].token
    val alignment =
        when (direction) {
            FABExpansionDirection.Up, FABExpansionDirection.Start -> Alignment.BottomEnd
            FABExpansionDirection.Down -> Alignment.TopEnd
            FABExpansionDirection.End -> Alignment.BottomStart
        }
    SamplePage(
        preview = {
            ControlSwitch("Show selected item on FAB", showSelectedAction) { showSelectedAction = it }
            Text(
                if (showSelectedAction) {
                    "Choose a menu item to show its title and icon on the FAB."
                } else {
                    "Tap the trigger to open the menu, then select an action."
                },
            )
            CompositionLocalProvider(LocalLayoutDirection provides if (rightToLeft) LayoutDirection.Rtl else LayoutDirection.Ltr) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(AppTheme.spacing.x9 * 4),
                    contentAlignment = alignment,
                ) {
                    ExpandableFAB(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        actions = actions,
                        icon = displayedIcon,
                        contentDescription = displayedLabel,
                        expandedStateDescription = expandedLabel,
                        collapsedStateDescription = collapsedLabel,
                        direction = direction,
                        collapseOnAction = collapseOnAction,
                        showActionLabels = showActionLabels,
                        triggerLabel = displayedLabel.takeIf { showTriggerLabel || usesSelection },
                    )
                }
            }
            Text("Last selection: ${selectedLabel ?: "None"}")
        },
        controls = {
            ControlSwitch("Expanded", expanded) { expanded = it }
            ControlSegmented("Direction", FABExpansionDirection.entries.map { it.name }, directionIndex) { directionIndex = it }
            ControlSlider("Menu item count: $count", count.toFloat(), { count = it.toInt() }, 0f..8f)
            Text("Sets the number of demo menu buttons. At zero, only the trigger remains.")
            ControlSwitch("Show trigger title", showTriggerLabel) { showTriggerLabel = it }
            ControlSwitch("Show menu item titles", showActionLabels) { showActionLabels = it }
            ControlSwitch("Actions enabled", actionsEnabled) { actionsEnabled = it }
            ControlSwitch("Collapse on action", collapseOnAction) { collapseOnAction = it }
            ControlSwitch("Long labels", longLabels) { longLabels = it }
            ControlSwitch("Right-to-left layout", rightToLeft) { rightToLeft = it }
            FloatingActionIconPicker("Trigger icon", iconIndex) { iconIndex = it }
            TextField(value = triggerLabel, onValueChange = { triggerLabel = it }, label = "Trigger title")
            TextField(value = expandedLabel, onValueChange = { expandedLabel = it }, label = "Expanded description")
            TextField(value = collapsedLabel, onValueChange = { collapsedLabel = it }, label = "Collapsed description")
        },
    )
}

package io.github.maniramezan.compose.sample

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.MenuPicker

private val OPTION_COUNTS = listOf(3, 8, 20)
private val SELECTION_OPTIONS = listOf("None", "Chosen")

@Composable
internal fun MenuPickerPage() {
    var optionCountIndex by remember { mutableIntStateOf(0) }
    var selectedIndex by remember { mutableIntStateOf(1) }
    var showLabel by remember { mutableStateOf(true) }
    var showPlaceholder by remember { mutableStateOf(true) }
    var showSupporting by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var showStateDescription by remember { mutableStateOf(false) }
    var acceptSelections by remember { mutableStateOf(true) }
    var rejectedCount by remember { mutableIntStateOf(0) }

    val options = List(OPTION_COUNTS[optionCountIndex]) { "Option ${it + 1}" }
    val selected = options.getOrNull(selectedIndex)

    SamplePage(
        preview = {
            MenuPicker(
                options = options,
                selectedOption = selected,
                onOptionSelected = { option ->
                    if (acceptSelections) {
                        selectedIndex = options.indexOf(option)
                    } else {
                        rejectedCount++
                    }
                },
                optionLabel = { it },
                label = if (showLabel) "Size" else "",
                placeholder = if (showPlaceholder) "Choose an option" else "",
                supportingText = if (showSupporting) "Supporting text" else null,
                enabled = enabled,
                isError = isError,
                stateDescription =
                    if (showStateDescription && selected != null) {
                        "$selected, ${selectedIndex + 1} of ${options.size}"
                    } else {
                        null
                    },
            )
            Text("Selected: ${selected ?: "nothing"}")
            Text("Ignored selections: $rejectedCount")
        },
        controls = {
            ControlSegmented(
                label = "Option count",
                options = OPTION_COUNTS.map { it.toString() },
                selectedIndex = optionCountIndex,
                onOptionSelected = {
                    optionCountIndex = it
                    selectedIndex = selectedIndex.coerceAtMost(OPTION_COUNTS[it] - 1)
                },
            )
            ControlSegmented(
                label = "Selection",
                options = SELECTION_OPTIONS,
                selectedIndex = if (selected == null) 0 else 1,
                onOptionSelected = { selectedIndex = if (it == 0) -1 else 1 },
            )
            ControlSwitch(label = "Label", checked = showLabel, onCheckedChange = { showLabel = it })
            ControlSwitch(label = "Placeholder", checked = showPlaceholder, onCheckedChange = { showPlaceholder = it })
            ControlSwitch(
                label = "Supporting text",
                checked = showSupporting,
                onCheckedChange = { showSupporting = it },
            )
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ControlSwitch(label = "Error state", checked = isError, onCheckedChange = { isError = it })
            ControlSwitch(
                label = "State description",
                checked = showStateDescription,
                onCheckedChange = { showStateDescription = it },
            )
            ControlSwitch(
                label = "Accept selections",
                checked = acceptSelections,
                onCheckedChange = { acceptSelections = it },
            )
        },
    )
}

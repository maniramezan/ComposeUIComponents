package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.Checkbox
import io.github.maniramezan.compose.components.RadioGroup
import io.github.maniramezan.compose.components.SelectionListContent
import io.github.maniramezan.compose.components.SelectionListNode
import io.github.maniramezan.compose.components.Slider
import io.github.maniramezan.compose.components.Switch
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Controls
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun CheckboxPage() {
    var checked by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Checkbox(checked = checked, onCheckedChange = { checked = it }, label = "Email updates", enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        ControlSwitch(label = "Checked", checked = checked, onCheckedChange = { checked = it })
    }
}

@Composable
internal fun RadioGroupPage() {
    val options = listOf("Compact", "Comfortable", "Spacious")
    var selectedIndex by remember { mutableIntStateOf(1) }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        RadioGroup(
            options = options,
            selectedIndex = selectedIndex,
            onOptionSelected = { selectedIndex = it },
            enabled = enabled,
        )
        Text(text = "Selected: ${options[selectedIndex]}")
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
    }
}

@Composable
internal fun SwitchPage() {
    var checked by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Switch(checked = checked, onCheckedChange = { checked = it }, label = "Notifications", enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        ControlSwitch(label = "Checked", checked = checked, onCheckedChange = { checked = it })
    }
}

@Composable
internal fun SliderPage() {
    var value by remember { mutableFloatStateOf(0.45f) }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Text("Volume: ${(value * 100).toInt()}%")
        Slider(value = value, onValueChange = { value = it }, enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Selection
// ─────────────────────────────────────────────────────────────────────────────

private val sampleSelectionListNodes: List<SelectionListNode<String>> =
    listOf(
        SelectionListNode(
            id = "fruit",
            title = "Fruit",
            leadingGlyph = "🍎",
            children =
                listOf(
                    SelectionListNode(id = "apple", title = "Apple", subtitle = "Red or green"),
                    SelectionListNode(id = "banana", title = "Banana", subtitle = "Tropical"),
                    SelectionListNode(id = "mango", title = "Mango", subtitle = "Stone fruit"),
                    SelectionListNode(id = "orange", title = "Orange", subtitle = "Citrus"),
                    SelectionListNode(id = "grape", title = "Grape", subtitle = "Vine fruit"),
                ),
        ),
        SelectionListNode(
            id = "vegetable",
            title = "Vegetable",
            leadingGlyph = "🥦",
            children =
                listOf(
                    SelectionListNode(id = "carrot", title = "Carrot"),
                    SelectionListNode(id = "spinach", title = "Spinach"),
                    SelectionListNode(id = "broccoli", title = "Broccoli"),
                    SelectionListNode(id = "cucumber", title = "Cucumber"),
                    SelectionListNode(id = "tomato", title = "Tomato"),
                ),
        ),
        SelectionListNode(
            id = "grain",
            title = "Grain",
            leadingGlyph = "🌾",
            children =
                listOf(
                    SelectionListNode(id = "rice", title = "Rice"),
                    SelectionListNode(id = "pasta", title = "Pasta"),
                    SelectionListNode(id = "bread", title = "Bread"),
                    SelectionListNode(id = "oats", title = "Oats"),
                ),
        ),
        SelectionListNode(
            id = "dairy",
            title = "Dairy",
            leadingGlyph = "🧀",
            children =
                listOf(
                    SelectionListNode(id = "milk", title = "Milk"),
                    SelectionListNode(id = "cheese", title = "Cheese"),
                    SelectionListNode(id = "yogurt", title = "Yogurt"),
                ),
        ),
        SelectionListNode(id = "water", title = "Water", leadingGlyph = "💧", subtitle = "Zero calories"),
        SelectionListNode(id = "juice", title = "Juice", leadingGlyph = "🍹"),
    )

@Composable
internal fun SelectionListPage() {
    val modeOptions = listOf("Single", "Multiple")
    var modeIndex by remember { mutableIntStateOf(0) }
    var searchable by remember { mutableStateOf(true) }
    var category by remember { mutableStateOf("banana") }
    var tags by remember { mutableStateOf(setOf("apple", "spinach", "rice")) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        if (modeIndex == 0) {
            SelectionListContent(
                title = "Category",
                nodes = sampleSelectionListNodes,
                selectedIds = setOf(category),
                onSelect = { category = it },
                isSearchable = searchable,
                searchPlaceholder = "Search",
                noResultsText = "No results",
                modifier = Modifier.height(450.dp),
            )
        } else {
            SelectionListContent(
                title = "Categories",
                nodes = sampleSelectionListNodes,
                selectedIds = tags,
                onSelect = { id -> tags = if (id in tags) tags - id else tags + id },
                isSearchable = searchable,
                searchPlaceholder = "Search",
                noResultsText = "No results",
                confirmButton = { TextButton(text = "${tags.size} selected", onClick = {}) },
                modifier = Modifier.height(450.dp),
            )
        }
        ControlsDivider()
        ControlSegmented(
            label = "Mode",
            options = modeOptions,
            selectedIndex = modeIndex,
            onOptionSelected = { modeIndex = it },
        )
        ControlSwitch(label = "Searchable", checked = searchable, onCheckedChange = { searchable = it })
    }
}

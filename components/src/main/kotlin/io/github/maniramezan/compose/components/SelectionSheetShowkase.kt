package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Selection Sheet — single choice", group = "Selection")
@Composable
public fun SelectionSheetSingleChoiceShowkase(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        SelectionSheetContent(
            title = "Category",
            nodes = selectionSheetSampleNodes,
            selectedIds = setOf("banana"),
            onSelect = {},
            isSearchable = true,
        )
    }

@ShowkaseComposable(name = "Selection Sheet — multiple choice", group = "Selection")
@Composable
public fun SelectionSheetMultipleChoiceShowkase(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        SelectionSheetContent(
            title = "Categories",
            nodes = selectionSheetSampleNodes,
            selectedIds = setOf("apple", "spinach"),
            onSelect = {},
            confirmButton = { TextButton(text = "Done", onClick = {}) },
        )
    }

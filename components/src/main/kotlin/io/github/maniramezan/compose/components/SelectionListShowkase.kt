package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Selection List — single choice", group = "Selection")
@Composable
public fun SelectionListSingleChoiceShowkase(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        SelectionListContent(
            title = "Category",
            nodes = selectionListSampleNodes,
            selectedIds = setOf("banana"),
            onSelect = {},
            isSearchable = true,
            searchPlaceholder = "Search",
            noResultsText = "No results",
        )
    }

@ShowkaseComposable(name = "Selection List — multiple choice", group = "Selection")
@Composable
public fun SelectionListMultipleChoiceShowkase(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        SelectionListContent(
            title = "Categories",
            nodes = selectionListSampleNodes,
            selectedIds = setOf("apple", "spinach"),
            onSelect = {},
            confirmButton = { TextButton(text = "Done", onClick = {}) },
        )
    }

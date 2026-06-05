package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

internal val selectionListSampleNodes: List<SelectionListNode<String>> =
    listOf(
        SelectionListNode(
            id = "fruit",
            title = "Fruit",
            leadingGlyph = "🍎",
            children =
                listOf(
                    SelectionListNode(id = "apple", title = "Apple"),
                    SelectionListNode(id = "banana", title = "Banana"),
                    SelectionListNode(id = "mango", title = "Mango"),
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
                ),
        ),
        SelectionListNode(id = "water", title = "Water", leadingGlyph = "💧"),
    )

@PreviewLightDark
@PreviewFontScale
@Preview(name = "SelectionList — single choice", group = "Selection")
@Composable
public fun SelectionListSingleChoicePreview(): Unit =
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

@Preview(name = "SelectionList — multiple choice", group = "Selection")
@Composable
public fun SelectionListMultipleChoicePreview(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        SelectionListContent(
            title = "Categories",
            nodes = selectionListSampleNodes,
            selectedIds = setOf("apple", "spinach"),
            onSelect = {},
            confirmButton = { TextButton(text = "Done", onClick = {}) },
        )
    }

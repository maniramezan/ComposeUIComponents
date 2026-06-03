package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

internal val selectionSheetSampleNodes: List<SelectionSheetNode<String>> =
    listOf(
        SelectionSheetNode(
            id = "fruit",
            title = "Fruit",
            leadingGlyph = "🍎",
            children =
                listOf(
                    SelectionSheetNode(id = "apple", title = "Apple"),
                    SelectionSheetNode(id = "banana", title = "Banana"),
                    SelectionSheetNode(id = "mango", title = "Mango"),
                ),
        ),
        SelectionSheetNode(
            id = "vegetable",
            title = "Vegetable",
            leadingGlyph = "🥦",
            children =
                listOf(
                    SelectionSheetNode(id = "carrot", title = "Carrot"),
                    SelectionSheetNode(id = "spinach", title = "Spinach"),
                ),
        ),
        SelectionSheetNode(id = "water", title = "Water", leadingGlyph = "💧"),
    )

@PreviewLightDark
@PreviewFontScale
@Preview(name = "SelectionSheet — single choice", group = "Selection")
@Composable
public fun SelectionSheetSingleChoicePreview(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        SelectionSheetContent(
            title = "Category",
            nodes = selectionSheetSampleNodes,
            selectedIds = setOf("banana"),
            onSelect = {},
            isSearchable = true,
        )
    }

@Preview(name = "SelectionSheet — multiple choice", group = "Selection")
@Composable
public fun SelectionSheetMultipleChoicePreview(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        SelectionSheetContent(
            title = "Categories",
            nodes = selectionSheetSampleNodes,
            selectedIds = setOf("apple", "spinach"),
            onSelect = {},
            confirmButton = { TextButton(text = "Done", onClick = {}) },
        )
    }

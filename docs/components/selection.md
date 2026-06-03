# Selection

`SelectionSheet` presents a single- or multiple-choice list inside a modal bottom
sheet, with optional search and inline two-level disclosure. Rows come from a
`SelectionSheetNode` tree: a node with no children is a **leaf** that selects on tap, while
a node with children is an **expandable parent** that reveals its children inline.

The sheet is controlled — it reflects the selection you pass and reports taps through a
callback; it never mutates the selection or dismisses itself. Selected rows show a
check (from `AppTheme.icons.check`), and a collapsed parent lists its selected children
as its subtitle. The disclosure chevron uses `AppTheme.icons.expand`, so provide real
icons (for example via `defaultAppIcons()`) when constructing your theme.

## Components

- `SelectionSheet` — single-choice (`selectedId`) and multiple-choice (`selectedIds`) overloads.
- `SelectionSheetNode` — an immutable tree node: `id`, `title`, optional `subtitle`/`leadingGlyph`, and `children`.

## Single choice

```kotlin
if (showSheet) {
    SelectionSheet(
        title = "Category",
        nodes = nodes,
        selectedId = choice,
        isSearchable = true,
        onSelect = { choice = it; showSheet = false }, // replace and dismiss
        onDismissRequest = { showSheet = false },
    )
}
```

## Multiple choice

```kotlin
if (showSheet) {
    SelectionSheet(
        title = "Categories",
        nodes = nodes,
        selectedIds = choices,
        onSelect = { id -> // toggle membership; the sheet stays open
            choices = if (id in choices) choices - id else choices + id
        },
        onDismissRequest = { showSheet = false },
        confirmButton = { TextButton(text = doneLabel, onClick = { showSheet = false }) },
    )
}
```

## Confirm button slot

`confirmButton` is an optional composable slot shown at the trailing edge of the
header. You supply the content and wire its `onClick`, so it can be a localized text
button, an icon, or an image:

```kotlin
confirmButton = { TextButton(text = stringResource(R.string.done), onClick = { showSheet = false }) }
// or an icon / image:
confirmButton = { IconButton(icon = AppTheme.icons.check, contentDescription = doneLabel, onClick = { showSheet = false }) }
```

It is available on **both** the single- and multiple-choice overloads. Multiple choice
should provide it (rows don't dismiss on tap); single choice usually omits it and
dismisses on selection, but may include it when you want an explicit confirm step.

## Building the node tree

```kotlin
val nodes = listOf(
    SelectionSheetNode(id = "water", title = "Water"),            // leaf
    SelectionSheetNode(
        id = "fruit",
        title = "Fruit",
        leadingGlyph = "🍎",
        children = listOf(                                   // expands inline
            SelectionSheetNode(id = "apple", title = "Apple"),
            SelectionSheetNode(id = "banana", title = "Banana"),
        ),
    ),
)
```

Identifiers must be unique across the whole tree; they are used as the selection
values reported by the sheet. When `isSearchable` is `true`, the search field filters
across both levels (case-insensitive) and force-expands matching parents.

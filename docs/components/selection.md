# Selection

`SelectionList` presents a single- or multiple-choice list inside a modal bottom
sheet, with optional search and inline two-level disclosure. Rows come from a
`SelectionListNode` tree: a node with no children is a **leaf** that selects on tap, while
a node with children is an **expandable parent** that reveals its children inline.

The list is controlled — it reflects the selection you pass and reports taps through a
callback; it never mutates the selection or dismisses itself. Selected rows show a
check (from `AppTheme.icons.check`), and a collapsed parent lists its selected children
as its subtitle. The disclosure chevron uses `AppTheme.icons.expand`, so provide real
icons (for example via `defaultAppIcons()`) when constructing your theme.

## Components

- `SelectionList` — single-choice (`selectedId`) and multiple-choice (`selectedIds`) overloads, presented as a modal bottom sheet.
- `SelectionListContent` — the embeddable list body without the surrounding sheet, for inline use.
- `SelectionListNode` — an immutable tree node: `id`, `title`, optional `subtitle`/`leadingGlyph`, and `children`.

![Selection list](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/selection-sheet.png)

## Single choice

With no `confirmButton`, tapping an item updates the selection **and** dismisses the
sheet — just update your state in `onSelect`:

```kotlin
if (showSheet) {
    SelectionList(
        title = "Category",
        nodes = nodes,
        selectedId = choice,
        isSearchable = true,
        onSelect = { choice = it }, // sheet dismisses on tap
        onDismissRequest = { showSheet = false },
    )
}
```

Pass a `confirmButton` to keep the sheet open while the user revises their choice;
then only the confirm control dismisses:

```kotlin
SelectionList(
    title = "Category",
    nodes = nodes,
    selectedId = choice,
    onSelect = { choice = it }, // stays open
    onDismissRequest = { showSheet = false },
    confirmButton = { TextButton(text = doneLabel, onClick = { showSheet = false }) },
)
```

## Multiple choice

```kotlin
if (showSheet) {
    SelectionList(
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

## Inline embedding

Use `SelectionListContent` to embed the list directly in a screen without a sheet:

```kotlin
SelectionListContent(
    title = "Category",
    nodes = nodes,
    selectedIds = setOf(choice),
    onSelect = { choice = it },
)
```

## Building the node tree

```kotlin
val nodes = listOf(
    SelectionListNode(id = "water", title = "Water"),            // leaf
    SelectionListNode(
        id = "fruit",
        title = "Fruit",
        leadingGlyph = "🍎",
        children = listOf(                                   // expands inline
            SelectionListNode(id = "apple", title = "Apple"),
            SelectionListNode(id = "banana", title = "Banana"),
        ),
    ),
)
```

Identifiers must be unique across the whole tree; they are used as the selection
values reported by the list. When `isSearchable` is `true`, the search field filters
across both levels (case-insensitive) and force-expands matching parents.

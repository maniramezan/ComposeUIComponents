# Layout

Layout components compose other components into higher-level, scrollable structures.

## Components

- `ShowcaseFeed`
- `ShowcaseRow`
- `ShowcaseItemWidth`

## ShowcaseFeed

`ShowcaseFeed` is a two-directional, App Store–style feed: a vertically-scrolling
column of sections where **each section scrolls horizontally on its own**. Sections are
declared with a `LazyColumn`-style DSL, so every section can use its own data type and
its own item card — a generic data source across heterogeneous content.

```kotlin
AppTheme {
    ShowcaseFeed {
        section(
            title = "Top Apps",
            items = apps,
            actionLabel = "See all",
            onAction = { /* navigate */ },
        ) { app -> AppCard(app) }

        section(title = "New Games", items = games) { game ->
            GameCard(game)
        }

        // A fully custom section renders anything you like.
        customSection {
            HeroBanner()
        }
    }
}
```

Each `section` renders a [`SectionHeader`](navigation.md) (title with an optional
trailing action) above a horizontally-scrolling `ShowcaseRow`. `customSection` drops
arbitrary content straight into the vertical list.

## ShowcaseRow

`ShowcaseRow` is the reusable peeking carousel behind each section, and works on its
own for a single row. Item sizing is driven by `itemWidth`:

| `ShowcaseItemWidth` | Behavior |
| --- | --- |
| `Peek(visibleFraction = 0.85f)` | Each item takes a fraction of the viewport so the **next item peeks** at the trailing edge (default). |
| `Fixed(width)` | Every item is exactly `width` wide. |
| `Wrap` | Each item uses its own intrinsic width. |

```kotlin
// Standalone peeking carousel.
ShowcaseRow(
    items = apps,
    itemWidth = ShowcaseItemWidth.Peek(0.8f),
) { app -> AppCard(app) }
```

### Multi-row (grid) sections

Set `rows` greater than `1` (with a `rowHeight`) to lay items into stacked rows that
scroll horizontally together — the "Top Charts" look. Items fill column-major.

```kotlin
section(
    title = "Top Charts",
    items = charts,
    itemWidth = ShowcaseItemWidth.Fixed(220.dp),
    rows = 2,
    rowHeight = 64.dp,
) { entry -> ChartRow(entry) }
```

### Accessibility

- Section titles carry `heading()` semantics via `SectionHeader`.
- Pass `rowContentDescription` on a `section` to give the scrolling row a spoken label;
  it is caller-supplied with no English default, per
  [ADR 0004](../adr/0004-localization-and-a11y-string-contract.md). When omitted, TalkBack
  traverses the items directly.

### Parameters

`ShowcaseFeed`:

| Parameter | Type | Description |
| --- | --- | --- |
| `content` | `ShowcaseFeedScope.() -> Unit` | Section declarations. |
| `state` | `LazyListState` | Hoisted vertical scroll state. |
| `contentPadding` | `PaddingValues` | Padding around the vertical list. |
| `verticalItemSpacing` | `Dp` | Gap between sections. |

`ShowcaseRow`:

| Parameter | Type | Description |
| --- | --- | --- |
| `items` | `List<T>` | Backing data source. |
| `itemWidth` | `ShowcaseItemWidth` | Item sizing / peek strategy. |
| `rows` | `Int` | `1` = single row; `> 1` = grid (needs `rowHeight`). |
| `rowHeight` | `Dp?` | Height of one row; required when `rows > 1`. |
| `contentPadding` | `PaddingValues` | Padding around the scrolling content. |
| `itemSpacing` | `Dp` | Gap between items (and grid rows). |
| `key` | `((index: Int, item: T) -> Any)?` | Stable item identity. |
| `itemContent` | `@Composable (item: T) -> Unit` | Renders one item. |

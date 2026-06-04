# Navigation

Navigation components provide themed Material 3 wrappers for common app structure.

## Components

- `TopAppBar`
- `MediumTopAppBar`
- `LargeTopAppBar`
- `BottomBar`
- `TabRow`
- `NavRail`
- `AdaptiveNavScaffold`
- `PaginatedContent`
- `SegmentedContent`

![Navigation components](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/navigation-components.png)

## Example

```kotlin
AppTheme {
    BottomBar(
        items = items,
        selectedIndex = selectedIndex,
        onItemSelected = onItemSelected,
    )
}
```

Navigation state is owned by the caller. Pass callbacks instead of navigation controllers to keep components reusable.

## PaginatedContent

A horizontally-paged container with a clickable title row and optional page indicator. Similar to Play Store app sections.

```kotlin
AppTheme {
    PaginatedContent(
        pages = listOf(
            PaginationPage(title = "Popular"),
            PaginationPage(title = "New"),
            PaginationPage(title = "Top Rated"),
        ),
    ) { pageIndex, page ->
        // Your page content here
        Text(text = page.title)
    }
}
```

Custom title slots are supported via the `pageTitle` parameter. For external page control, pass a caller-owned `PagerState`.

On first composition `PaginatedContent` plays a brief peek-and-return animation to signal that the content is horizontally scrollable. Suppress it with `showScrollHint = false` when scrollability is already obvious:

```kotlin
PaginatedContent(
    pages = pages,
    showScrollHint = false,
) { _, page ->
    Text(text = page.title)
}
```

Inactive page titles are dimmed using the theme `outline` color to match the de-emphasis level of the dot indicators, and the title row mirrors correctly under right-to-left layout direction.

## SegmentedContent

A tap-driven segmented picker paired with a client-supplied content slot. Tapping a segment crossfades to that segment's content. When segment titles fit the available width they distribute evenly; when they overflow, the bar becomes horizontally scrollable and shows a soft edge fade on whichever side has more to reveal. Scrolling the selection into view also keeps a sliver of the adjacent segment peeking, so it stays obvious there's more to either side.

Reach for `SegmentedContent` when selection should be discrete and tap-driven (settings sections, filter modes). Reach for `PaginatedContent` when the user should be able to swipe between full-width pages of content.

State-managed overload (the component owns the selection):

```kotlin
AppTheme {
    SegmentedContent(
        items = listOf(
            SegmentedItem(title = "Overview"),
            SegmentedItem(title = "Activity"),
            SegmentedItem(title = "Settings"),
        ),
    ) { _, item ->
        Text(text = "${item.title} content")
    }
}
```

State-hoisted overload with a custom title slot:

```kotlin
var selectedIndex by remember { mutableIntStateOf(0) }
SegmentedContent(
    items = items,
    selectedIndex = selectedIndex,
    onSelectionChanged = { selectedIndex = it },
    indicator = SegmentSelectionIndicator.Underline,
    fitMode = SegmentFitMode.Intrinsic,
    segmentTitle = { _, item, isSelected ->
        // any composable — icons, badges, multi-line, etc.
    },
) { _, item ->
    SectionContent(item)
}
```

Use `SegmentSelectionIndicator` to choose between `Pill` (filled background, iOS-segmented feel), `Underline` (Material-tab feel), or `None` (style entirely through the title slot). Use `SegmentFitMode.EvenWhenFits` for distribute-evenly-or-scroll behavior, or `SegmentFitMode.Intrinsic` to always size segments to their content.

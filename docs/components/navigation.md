# Navigation

Navigation components provide themed Material 3 wrappers for common app structure.

## Components

- `TopAppBar`
- `BottomBar`
- `TabRow`
- `NavRail`
- `PaginatedContent`

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

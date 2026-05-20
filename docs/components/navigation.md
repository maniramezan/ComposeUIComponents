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

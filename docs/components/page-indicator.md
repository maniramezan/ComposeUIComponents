# PageIndicator

`PageIndicator` is the dot row used by `PaginatedContent`'s `Dots` footer, available on its
own for carousels, onboarding flows, or any `HorizontalPager`. Pass explicit
`pageCount`/`currentPage`, or bind it to a `PagerState` so page reads stay scoped to the
indicator:

```kotlin
val pagerState = rememberPagerState(pageCount = { pages.size })

HorizontalPager(state = pagerState) { page -> PageContent(pages[page]) }
PageIndicator(
    pagerState = pagerState,
    pagePositionDescription = { index, count -> "Page ${index + 1} of $count" },
)
```

The dots are color-only, so they stay out of the accessibility tree unless you supply
`pagePositionDescription`; the indicator then reads as a single node and announces page
changes politely. Supply a localized description; omit the formatter or return blank to leave the dots
decorative. `currentPage` is clamped into range, and non-positive page counts render nothing.
Use `activeColor` and `inactiveColor` to override the theme colors.

![Page indicator](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/page-indicator.png)

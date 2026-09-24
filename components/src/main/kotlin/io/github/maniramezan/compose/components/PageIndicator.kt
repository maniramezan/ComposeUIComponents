package io.github.maniramezan.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import io.github.maniramezan.compose.theme.AppTheme

/**
 * A row of dots marking the current position within [pageCount] pages — for carousels,
 * onboarding flows, and any pager. [PaginatedContent] uses it for its `Dots` footer.
 *
 * The dots are color-only, so they are hidden from the accessibility tree unless you supply
 * [pagePositionDescription]; the indicator then reads as one spoken node (e.g. "Page 2 of 5")
 * and announces changes politely.
 *
 * @param pageCount Total number of pages. Renders nothing when `pageCount < 1`.
 * @param currentPage Zero-based index of the active page; clamped into range.
 * @param modifier Modifier applied to the dot row.
 * @param pagePositionDescription Builds the caller-localized announcement for the
 *   position, e.g. `{ index, count -> "Page ${index + 1} of $count" }`.
 * @param activeColor Color of the dot for [currentPage].
 * @param inactiveColor Color of every other dot.
 */
@Composable
public fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    pagePositionDescription: ((pageIndex: Int, pageCount: Int) -> String)? = null,
    activeColor: Color = AppTheme.colors.primary,
    inactiveColor: Color = AppTheme.colors.onSurfaceVariant.copy(alpha = DISABLED_CONTENT_ALPHA),
) {
    if (pageCount < 1) return
    val activePage = currentPage.coerceIn(0, pageCount - 1)
    val positionDescription = pagePositionDescription?.invoke(activePage, pageCount)
    Row(
        modifier =
            modifier.clearAndSetSemantics {
                if (positionDescription != null) {
                    contentDescription = positionDescription
                    liveRegion = LiveRegionMode.Polite
                }
            },
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val color by animateColorAsState(
                targetValue = if (index == activePage) activeColor else inactiveColor,
                label = "PageIndicatorDotColor",
            )
            Box(
                modifier =
                    Modifier
                        .size(AppTheme.spacing.x1)
                        .pillSurface(color),
            )
        }
    }
}

/**
 * [PageIndicator] bound to a [PagerState]. The active dot follows
 * [PagerState.currentPage], and the page reads are scoped to this composable, so the
 * caller does not recompose as the pager moves.
 */
@Composable
public fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    pagePositionDescription: ((pageIndex: Int, pageCount: Int) -> String)? = null,
    activeColor: Color = AppTheme.colors.primary,
    inactiveColor: Color = AppTheme.colors.onSurfaceVariant.copy(alpha = DISABLED_CONTENT_ALPHA),
) {
    PageIndicator(
        pageCount = pagerState.pageCount,
        currentPage = pagerState.currentPage,
        modifier = modifier,
        pagePositionDescription = pagePositionDescription,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
    )
}

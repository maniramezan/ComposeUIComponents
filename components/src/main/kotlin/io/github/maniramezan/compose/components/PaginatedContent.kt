package io.github.maniramezan.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import io.github.maniramezan.compose.theme.AppTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Describes a single page in a [PaginatedContent] component.
 *
 * @param title A short label displayed in the title row above the page content.
 */
@Immutable
public data class PaginationPage(
    public val title: String,
)

/**
 * Controls how the title aligns within the title row and whether adjacent
 * page titles peek into view.
 */
public enum class PageTitleAlignment {
    /**
     * Current title anchored at the leading edge. The next page's title peeks
     * at the trailing edge.
     */
    Leading,

    /**
     * Current title anchored at the trailing edge. The previous page's title
     * peeks at the leading edge only when [PageDirection.Bidirectional].
     */
    Trailing,

    /**
     * Current title centered. No adjacent titles peek.
     */
    Center,
}

/**
 * Whether the pager supports swiping in both directions or only forward.
 */
public enum class PageDirection {
    /** User can swipe forward and backward. */
    Bidirectional,

    /** User can only swipe forward (leading → trailing). */
    Unidirectional,
}

/**
 * Style of the page position footer.
 */
public enum class PageFooterStyle {
    /** Dot indicators. */
    Dots,

    /** Linear progress bar showing position within all pages. */
    Progress,

    /** No footer. */
    None,
}

/**
 * A horizontally-paged container whose title row scrolls in sync with the
 * content pager. Adjacent page titles peek at the edges based on
 * [titleAlignment] and [direction].
 *
 * This overload manages [PagerState] internally.
 *
 * @param onPageChanged Called when the settled page changes. Use this to react
 *   to page transitions (analytics, loading data, etc.).
 */
@Composable
public fun PaginatedContent(
    pages: List<PaginationPage>,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    titleAlignment: PageTitleAlignment = PageTitleAlignment.Leading,
    direction: PageDirection = PageDirection.Bidirectional,
    footerStyle: PageFooterStyle = PageFooterStyle.Dots,
    onPageChanged: ((Int) -> Unit)? = null,
    pageTitle: @Composable (pageIndex: Int, page: PaginationPage, progress: Float) -> Unit =
        { _, page, progress -> DefaultPageTitle(title = page.title, progress = progress) },
    pageContent: @Composable PagerScope.(pageIndex: Int, page: PaginationPage) -> Unit,
) {
    if (pages.isEmpty()) return

    val clampedInitial = initialPage.coerceIn(0, pages.lastIndex)
    val pagerState = rememberPagerState(initialPage = clampedInitial, pageCount = { pages.size })

    PaginatedContent(
        pages = pages,
        pagerState = pagerState,
        modifier = modifier,
        titleAlignment = titleAlignment,
        direction = direction,
        footerStyle = footerStyle,
        onPageChanged = onPageChanged,
        pageTitle = pageTitle,
        pageContent = pageContent,
    )
}

/**
 * A horizontally-paged container whose title row scrolls in sync with the
 * content pager. This overload gives the caller full control over [PagerState].
 *
 * @param onPageChanged Called when the settled page changes. Use this to react
 *   to page transitions (analytics, loading data, etc.).
 */
@Composable
public fun PaginatedContent(
    pages: List<PaginationPage>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    titleAlignment: PageTitleAlignment = PageTitleAlignment.Leading,
    direction: PageDirection = PageDirection.Bidirectional,
    footerStyle: PageFooterStyle = PageFooterStyle.Dots,
    onPageChanged: ((Int) -> Unit)? = null,
    pageTitle: @Composable (pageIndex: Int, page: PaginationPage, progress: Float) -> Unit =
        { _, page, progress -> DefaultPageTitle(title = page.title, progress = progress) },
    pageContent: @Composable PagerScope.(pageIndex: Int, page: PaginationPage) -> Unit,
) {
    if (pages.isEmpty()) return

    // Notify caller when the settled page changes
    if (onPageChanged != null) {
        LaunchedEffect(pagerState, onPageChanged) {
            snapshotFlow { pagerState.settledPage }
                .distinctUntilChanged()
                .collect { page -> onPageChanged(page) }
        }
    }

    // For unidirectional: intercept backward scroll gestures
    val nestedScrollConnection =
        if (direction == PageDirection.Unidirectional) {
            remember { ForwardOnlyNestedScrollConnection() }
        } else {
            null
        }

    Column(modifier = modifier.fillMaxWidth()) {
        // Scrolling title row
        ScrollingTitleRow(
            pages = pages,
            pagerState = pagerState,
            titleAlignment = titleAlignment,
            direction = direction,
            pageTitle = pageTitle,
            modifier = Modifier.fillMaxWidth(),
        )

        // Page content — wrap with nestedScroll for unidirectional
        val pagerModifier =
            Modifier.fillMaxWidth().let { mod ->
                if (nestedScrollConnection != null) mod.nestedScroll(nestedScrollConnection) else mod
            }
        HorizontalPager(
            state = pagerState,
            modifier = pagerModifier,
        ) { pageIndex ->
            pageContent(pageIndex, pages[pageIndex])
        }

        // Footer
        when (footerStyle) {
            PageFooterStyle.Dots -> {
                if (pages.size > 1) {
                    PageDotIndicator(
                        pageCount = pages.size,
                        currentPage = pagerState.currentPage,
                        modifier =
                            Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = AppTheme.spacing.x1),
                    )
                }
            }
            PageFooterStyle.Progress -> {
                if (pages.size > 1) {
                    PageProgressFooter(
                        pageCount = pages.size,
                        currentPage = pagerState.currentPage,
                        offsetFraction = pagerState.currentPageOffsetFraction,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = AppTheme.spacing.x1,
                                    start = AppTheme.spacing.x2,
                                    end = AppTheme.spacing.x2,
                                ),
                    )
                }
            }
            PageFooterStyle.None -> { /* no footer */ }
        }
    }
}

/**
 * A [NestedScrollConnection] that consumes all backward (right-to-left in LTR)
 * scroll so the pager cannot go to a previous page.
 *
 * In LTR, swiping left (scrolling right / positive delta.x) goes to next page
 * — that's allowed. Swiping right (negative delta.x in scroll terms) goes back
 * — that's consumed.
 *
 * Note: HorizontalPager uses negative x offset for "next" (because content
 * moves left). So positive available.x means user is dragging right = going back.
 */
private class ForwardOnlyNestedScrollConnection : NestedScrollConnection {
    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        // Consume backward scroll (positive x = dragging content right = going back)
        return if (available.x > 0f) {
            Offset(available.x, 0f) // consume it, don't let pager see it
        } else {
            Offset.Zero // let forward scroll through
        }
    }
}

/**
 * Title row that scrolls titles in sync with the pager.
 *
 * Strategy: We always measure and place three titles (prev, current, next)
 * when they exist. All three are offset by the same `continuousOffset` which
 * represents the total scroll position = (currentPage + offsetFraction) * containerWidth.
 * Each title has a "home" position at `pageIndex * containerWidth + anchorOffset`.
 * The visible x = homeX - scrollOffset, where scrollOffset = continuousPage * containerWidth.
 * This simplifies to: visibleX = (pageIndex - continuousPage) * containerWidth + anchorOffset.
 */
@Composable
private fun ScrollingTitleRow(
    pages: List<PaginationPage>,
    pagerState: PagerState,
    titleAlignment: PageTitleAlignment,
    direction: PageDirection,
    pageTitle: @Composable (pageIndex: Int, page: PaginationPage, progress: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    SubcomposeLayout(
        modifier =
            modifier
                .clipToBounds()
                .padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.x1),
    ) { constraints ->
        val currentPage = pagerState.currentPage
        val fraction = pagerState.currentPageOffsetFraction
        val containerWidth = constraints.maxWidth

        // Continuous page position (e.g., 1.3 means 30% between page 1 and 2)
        val continuousPage = currentPage + fraction

        // Which pages could be visible: current, and the two adjacent
        val visibleIndices =
            buildList {
                if (currentPage - 1 >= 0) add(currentPage - 1)
                add(currentPage)
                if (currentPage + 1 <= pages.lastIndex) add(currentPage + 1)
            }

        // Measure all visible titles
        val measured =
            visibleIndices.associateWith { index ->
                val distFromCurrent = abs(index - continuousPage)
                val titleProgress = (1f - distFromCurrent).coerceIn(0f, 1f)
                subcompose("title_$index") {
                    pageTitle(index, pages[index], titleProgress)
                }.first().measure(constraints.copy(minWidth = 0))
            }

        val totalHeight = measured.values.maxOfOrNull { it.height } ?: 0

        layout(containerWidth, totalHeight) {
            val rtlMul = if (isRtl) -1f else 1f

            for ((index, placeable) in measured) {
                // Each title's anchor within its own "slot"
                val anchorInSlot =
                    when (titleAlignment) {
                        PageTitleAlignment.Leading -> 0
                        PageTitleAlignment.Trailing -> containerWidth - placeable.width
                        PageTitleAlignment.Center -> (containerWidth - placeable.width) / 2
                    }

                // The title's position relative to the viewport:
                // It lives at slot `index`, viewport is scrolled to `continuousPage`.
                // Visible X = (index - continuousPage) * containerWidth + anchorInSlot
                val pageOffset = index - continuousPage
                val visibleX = (pageOffset * containerWidth * rtlMul).roundToInt() + anchorInSlot

                // For Center alignment: only show current page title (no peek)
                if (titleAlignment == PageTitleAlignment.Center && index != currentPage) {
                    // Only show during active scroll (within half a page)
                    if (abs(pageOffset) > 0.5f) continue
                }

                // For Trailing + Unidirectional: don't show previous
                if (titleAlignment == PageTitleAlignment.Trailing &&
                    direction == PageDirection.Unidirectional &&
                    index < currentPage
                ) {
                    continue
                }

                placeable.placeRelative(
                    x = visibleX,
                    y = (totalHeight - placeable.height) / 2,
                )
            }
        }
    }
}

/**
 * Default title rendering. [progress] is 1.0 when fully selected, 0.0 when
 * fully off-screen, animating between during swipe.
 */
@Composable
internal fun DefaultPageTitle(
    title: String,
    progress: Float,
) {
    val color by animateColorAsState(
        targetValue = if (progress > 0.5f) AppTheme.colors.primary else AppTheme.colors.onSurfaceVariant,
        label = "pageTitleColor",
    )
    Text(
        text = title,
        style = AppTheme.typography.titleSmall,
        color = color,
    )
}

@Composable
private fun PageDotIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val color by animateColorAsState(
                targetValue =
                    if (index == currentPage) {
                        AppTheme.colors.primary
                    } else {
                        AppTheme.colors.onSurfaceVariant.copy(alpha = 0.38f)
                    },
                label = "indicatorColor",
            )
            Box(
                modifier =
                    Modifier
                        .size(AppTheme.spacing.x1)
                        .clip(CircleShape)
                        .background(color),
            )
        }
    }
}

@Composable
private fun PageProgressFooter(
    pageCount: Int,
    currentPage: Int,
    offsetFraction: Float,
    modifier: Modifier = Modifier,
) {
    val progress =
        if (pageCount <= 1) {
            1f
        } else {
            ((currentPage + offsetFraction) / (pageCount - 1)).coerceIn(0f, 1f)
        }
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier.clip(CircleShape),
        color = AppTheme.colors.primary,
        trackColor = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.12f),
    )
}

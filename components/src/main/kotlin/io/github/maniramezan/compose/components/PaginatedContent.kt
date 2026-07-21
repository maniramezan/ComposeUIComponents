package io.github.maniramezan.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Velocity
import io.github.maniramezan.compose.theme.AppTheme
import kotlinx.coroutines.delay
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
     * Current title centered. Adjacent titles are leading-aligned and slide
     * into center as they become the current page; the outgoing title slides
     * from center back to leading. Cancelling a swipe snaps the adjacent
     * title back to its leading position.
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
 * @param showScrollHint When `true`, plays a brief peek-and-return animation on
 *   first composition to signal that the content is horizontally scrollable.
 *   Set to `false` to suppress the hint (e.g. when onboarding is already clear).
 * @param pagePositionDescription Builds the accessibility announcement for the
 *   footer (e.g. `{ index, count -> "Page ${index + 1} of $count" }`). Supply a
 *   localized string from the caller; when `null` the footer stays decorative
 *   and is not announced. Ignored when [footerStyle] is [PageFooterStyle.None].
 */
@Composable
public fun PaginatedContent(
    pages: List<PaginationPage>,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    titleAlignment: PageTitleAlignment = PageTitleAlignment.Leading,
    direction: PageDirection = PageDirection.Bidirectional,
    footerStyle: PageFooterStyle = PageFooterStyle.Dots,
    showScrollHint: Boolean = true,
    onPageChanged: ((Int) -> Unit)? = null,
    pagePositionDescription: ((pageIndex: Int, pageCount: Int) -> String)? = null,
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
        showScrollHint = showScrollHint,
        onPageChanged = onPageChanged,
        pagePositionDescription = pagePositionDescription,
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
 * @param showScrollHint When `true`, plays a brief peek-and-return animation on
 *   first composition to signal that the content is horizontally scrollable.
 *   Set to `false` to suppress the hint (e.g. when onboarding is already clear).
 * @param pagePositionDescription Builds the accessibility announcement for the
 *   footer (e.g. `{ index, count -> "Page ${index + 1} of $count" }`). Supply a
 *   localized string from the caller; when `null` the footer stays decorative
 *   and is not announced. Ignored when [footerStyle] is [PageFooterStyle.None].
 */
@Composable
public fun PaginatedContent(
    pages: List<PaginationPage>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    titleAlignment: PageTitleAlignment = PageTitleAlignment.Leading,
    direction: PageDirection = PageDirection.Bidirectional,
    footerStyle: PageFooterStyle = PageFooterStyle.Dots,
    showScrollHint: Boolean = true,
    onPageChanged: ((Int) -> Unit)? = null,
    pagePositionDescription: ((pageIndex: Int, pageCount: Int) -> String)? = null,
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

    // Peek-and-return animation hinting the content is scrollable
    if (showScrollHint && pages.size > 1) {
        LaunchedEffect(pagerState) {
            delay(600L)
            if (pagerState.currentPage < pages.lastIndex) {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage,
                    pageOffsetFraction = 0.25f,
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                )
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage,
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                )
            }
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

        // Footer — pass pagerState directly so state reads are scoped to each
        // footer composable, not to PaginatedContent's recomposition scope.
        when (footerStyle) {
            PageFooterStyle.Dots -> {
                if (pages.size > 1) {
                    PageDotIndicator(
                        pageCount = pages.size,
                        pagerState = pagerState,
                        pagePositionDescription = pagePositionDescription,
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
                        pagerState = pagerState,
                        pagePositionDescription = pagePositionDescription,
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
        // Only block user-initiated backward scroll (drag); allow the pager's
        // internal settle/snap animations through so titles animate smoothly
        // back to their resting position when a swipe doesn't cross threshold.
        return if (available.x > 0f && source == NestedScrollSource.UserInput) {
            Offset(available.x, 0f)
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        // Consume backward fling velocity so the pager can't fling to prev page
        return if (available.x > 0f) {
            Velocity(available.x, 0f)
        } else {
            Velocity.Zero
        }
    }
}

/**
 * Title row that scrolls titles in sync with the pager.
 *
 * **Leading / Trailing alignment** — standard slot-based layout:
 * Each title lives in a slot of width `containerWidth`. The viewport scrolls
 * by `continuousPage * containerWidth`, so:
 *   `visibleX = (index - continuousPage) * containerWidth + anchorInSlot`
 *
 * **Center alignment** — animated anchor layout:
 * Adjacent titles are leading-aligned (anchor = 0) and the current title is
 * center-aligned (anchor = (containerWidth - width) / 2). During a swipe the
 * anchor interpolates continuously with `progress = 1 - |index - continuousPage|`,
 * so the incoming title slides from leading to center while the outgoing title
 * slides from center to leading. Titles still travel a full `containerWidth`
 * per page so their scroll stays perfectly in sync with the pager content.
 *   `visibleX = (index - continuousPage) * containerWidth + lerp(0, centerAnchor, progress)`
 */
@Composable
private fun ScrollingTitleRow(
    pages: List<PaginationPage>,
    pagerState: PagerState,
    titleAlignment: PageTitleAlignment,
    pageTitle: @Composable (pageIndex: Int, page: PaginationPage, progress: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    // One stable MutableFloatState per page index so the subcompose lambda
    // captures a stable reference instead of a raw Float that changes every
    // frame. Updating the state value during the layout phase is safe here
    // because the owning SubcomposeLayout re-measures before the next draw,
    // preventing composition → layout → composition loops.
    val progressStates = remember { mutableMapOf<Int, MutableFloatState>() }

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

                // Write progress into a stable state so the subcompose lambda
                // below does not capture the raw Float. Without this, Compose
                // sees a new lambda every frame and re-composes each title slot
                // on every scroll frame even when the visual output is unchanged.
                val progressState =
                    progressStates.getOrPut(index) { mutableFloatStateOf(titleProgress) }
                progressState.floatValue = titleProgress

                subcompose("title_$index") {
                    val progress by progressState
                    pageTitle(index, pages[index], progress)
                }.first().measure(constraints.copy(minWidth = 0))
            }

        val totalHeight = measured.values.maxOfOrNull { it.height } ?: 0

        layout(containerWidth, totalHeight) {
            for ((index, placeable) in measured) {
                val pageOffset = index - continuousPage

                // placeRelative() mirrors x automatically in RTL — no manual
                // rtlMul needed; applying one here would double-invert the axis.
                val visibleX: Int =
                    when (titleAlignment) {
                        PageTitleAlignment.Leading -> {
                            (pageOffset * containerWidth).roundToInt()
                        }
                        PageTitleAlignment.Trailing -> {
                            val anchor = containerWidth - placeable.width
                            (pageOffset * containerWidth).roundToInt() + anchor
                        }
                        PageTitleAlignment.Center -> {
                            // progress = 1 at current page, 0 at adjacent pages.
                            // anchor interpolates between leading (0) and center.
                            val progress = (1f - abs(pageOffset)).coerceIn(0f, 1f)
                            val centerAnchor = (containerWidth - placeable.width) / 2f
                            val anchor = centerAnchor * progress
                            (pageOffset * containerWidth + anchor).roundToInt()
                        }
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
    val containerColor by animateColorAsState(
        targetValue = if (progress > 0.5f) AppTheme.colors.primary else Color.Transparent,
        label = "pageTitleContainerColor",
    )
    val textColor by animateColorAsState(
        targetValue = if (progress > 0.5f) AppTheme.colors.onPrimary else AppTheme.colors.outline,
        label = "pageTitleColor",
    )
    Box(
        modifier =
            Modifier
                .clip(AppTheme.shapes.pill)
                .background(containerColor)
                .padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.half),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = AppTheme.typography.titleSmall,
            color = textColor,
        )
    }
}

@Composable
private fun PageDotIndicator(
    pageCount: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    pagePositionDescription: ((Int, Int) -> String)? = null,
) {
    // Read currentPage here so recomposition is scoped to PageDotIndicator,
    // not to PaginatedContent. currentPage changes only when the page settles.
    val currentPage = pagerState.currentPage
    // The dots themselves are color-only; expose the position as a single
    // spoken node (and announce changes) when the caller supplies a formatter.
    val positionDescription = pagePositionDescription?.invoke(currentPage, pageCount)
    Row(
        modifier =
            if (positionDescription != null) {
                modifier.semantics {
                    contentDescription = positionDescription
                    liveRegion = LiveRegionMode.Polite
                }
            } else {
                modifier
            },
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
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    pagePositionDescription: ((Int, Int) -> String)? = null,
) {
    // Read the settled page here so per-frame recomposition during scroll is
    // scoped to PageProgressFooter alone, not to PaginatedContent. The
    // frequently-changing offset fraction is read lazily inside the progress
    // lambda instead, so it drives draw-phase updates rather than recomposition.
    val currentPage = pagerState.currentPage
    // The bar is a visual position cue; expose the settled page as a spoken
    // node (and announce changes) when the caller supplies a formatter.
    val positionDescription = pagePositionDescription?.invoke(currentPage, pageCount)
    LinearProgressIndicator(
        progress = {
            if (pageCount <= 1) {
                1f
            } else {
                ((currentPage + pagerState.currentPageOffsetFraction) / (pageCount - 1)).coerceIn(0f, 1f)
            }
        },
        modifier =
            if (positionDescription != null) {
                modifier.clip(CircleShape).semantics {
                    contentDescription = positionDescription
                    liveRegion = LiveRegionMode.Polite
                }
            } else {
                modifier.clip(CircleShape)
            },
        color = AppTheme.colors.primary,
        trackColor = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.12f),
    )
}

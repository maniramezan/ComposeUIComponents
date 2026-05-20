package io.github.maniramezan.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight
import kotlinx.coroutines.launch

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
 * A horizontally-paged container with a clickable title row and optional
 * dot-indicator. Similar to Play Store app sections but with per-page titles
 * that can be either a default text label or a fully custom composable.
 *
 * This overload manages [PagerState] internally. Use the overload that accepts
 * a [PagerState] parameter if you need external control over the current page.
 *
 * @param pages The list of pages. If empty, nothing is rendered.
 * @param modifier Modifier applied to the root container.
 * @param initialPage Zero-based index of the page shown first.
 * @param showPageIndicator Whether to render dot indicators below the pager.
 * @param pageTitle Composable slot for each title in the title row.
 * @param pageContent Composable slot for each page's body.
 */
@Composable
public fun PaginatedContent(
    pages: List<PaginationPage>,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    showPageIndicator: Boolean = true,
    pageTitle: @Composable (pageIndex: Int, page: PaginationPage, selected: Boolean) -> Unit =
        { _, page, selected -> DefaultPageTitle(title = page.title, selected = selected) },
    pageContent: @Composable PagerScope.(pageIndex: Int, page: PaginationPage) -> Unit,
) {
    if (pages.isEmpty()) return

    val clampedInitial = initialPage.coerceIn(0, pages.lastIndex)
    val pagerState = rememberPagerState(initialPage = clampedInitial, pageCount = { pages.size })

    PaginatedContent(
        pages = pages,
        pagerState = pagerState,
        modifier = modifier,
        showPageIndicator = showPageIndicator,
        pageTitle = pageTitle,
        pageContent = pageContent,
    )
}

/**
 * A horizontally-paged container with a clickable title row and optional
 * dot-indicator. This overload gives the caller full control over [PagerState].
 *
 * @param pages The list of pages. If empty, nothing is rendered.
 * @param pagerState Caller-owned state for page control and observation.
 * @param modifier Modifier applied to the root container.
 * @param showPageIndicator Whether to render dot indicators below the pager.
 * @param pageTitle Composable slot for each title in the title row.
 * @param pageContent Composable slot for each page's body.
 */
@Composable
public fun PaginatedContent(
    pages: List<PaginationPage>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    showPageIndicator: Boolean = true,
    pageTitle: @Composable (pageIndex: Int, page: PaginationPage, selected: Boolean) -> Unit =
        { _, page, selected -> DefaultPageTitle(title = page.title, selected = selected) },
    pageContent: @Composable PagerScope.(pageIndex: Int, page: PaginationPage) -> Unit,
) {
    if (pages.isEmpty()) return

    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {
        // Title row
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.x2),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
        ) {
            pages.forEachIndexed { index, page ->
                val selected = pagerState.currentPage == index
                Box(
                    modifier =
                        Modifier
                            .minimumTouchTargetHeight(minimumTouchTargetSize())
                            .clip(AppTheme.shapes.small)
                            .clickable {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            }.padding(vertical = AppTheme.spacing.x1)
                            .semantics { role = Role.Tab },
                    contentAlignment = Alignment.CenterStart,
                ) {
                    pageTitle(index, page, selected)
                }
            }
        }

        // Page content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { pageIndex ->
            pageContent(pageIndex, pages[pageIndex])
        }

        // Dot indicator
        if (showPageIndicator && pages.size > 1) {
            PageIndicator(
                pageCount = pages.size,
                currentPage = pagerState.currentPage,
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = AppTheme.spacing.x1),
            )
        }
    }
}

@Composable
internal fun DefaultPageTitle(
    title: String,
    selected: Boolean,
) {
    val color by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.primary else AppTheme.colors.onSurfaceVariant,
        label = "pageTitleColor",
    )
    Text(
        text = title,
        style = AppTheme.typography.titleSmall,
        color = color,
    )
}

@Composable
private fun PageIndicator(
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

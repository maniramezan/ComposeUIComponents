package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.compose.theme.AppTheme
import androidx.compose.foundation.lazy.grid.itemsIndexed as gridItemsIndexed

private const val DEFAULT_PEEK_FRACTION = 0.85f

/**
 * Strategy that controls how wide each item is inside a [ShowcaseRow], which in
 * turn drives how much of the neighbouring item peeks at the trailing edge.
 */
public sealed interface ShowcaseItemWidth {
    /**
     * Each item takes [visibleFraction] of the row's usable width so the next
     * item's edge peeks into view — the App Store carousel look. A fraction of
     * `1f` fills the viewport (no peek); smaller fractions reveal more of the
     * neighbour.
     */
    public data class Peek(
        public val visibleFraction: Float = DEFAULT_PEEK_FRACTION,
    ) : ShowcaseItemWidth

    /** Each item is exactly [width] wide, regardless of the viewport size. */
    public data class Fixed(
        public val width: Dp,
    ) : ShowcaseItemWidth

    /** Each item uses its own intrinsic width; nothing is imposed by the row. */
    public data object Wrap : ShowcaseItemWidth
}

/**
 * A horizontally-scrolling row of items whose sizing is driven by [itemWidth].
 * With the default [ShowcaseItemWidth.Peek] the next item peeks at the trailing
 * edge, signalling that the row scrolls.
 *
 * This is the reusable building block behind [ShowcaseFeed] sections, but it is
 * also usable on its own for a single carousel.
 *
 * @param items Backing data source; rendered lazily via [itemContent].
 * @param itemWidth How each item is sized. See [ShowcaseItemWidth].
 * @param rows Number of stacked rows. `1` renders a single [LazyRow]; a value
 *   greater than `1` renders a column-major [LazyHorizontalGrid] (App Store
 *   "Top Charts" look) and requires [rowHeight]. If [rowHeight] is `null`, the
 *   row falls back to a single row.
 * @param rowHeight Height of one row; required when [rows] is greater than `1`.
 * @param contentPadding Padding around the scrolling content. The horizontal
 *   padding is factored into the peek width so the first and last items align
 *   with the row edges.
 * @param itemSpacing Gap between adjacent items (and between grid rows).
 * @param key Stable key for each item, forwarded to the lazy list for correct
 *   item identity across data changes.
 * @param itemContent Renders a single item. Width/height constraints are
 *   applied by the row; the content fills them.
 */
@Composable
public fun <T> ShowcaseRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    itemWidth: ShowcaseItemWidth = ShowcaseItemWidth.Peek(),
    rows: Int = 1,
    rowHeight: Dp? = null,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(horizontal = AppTheme.spacing.x2),
    itemSpacing: Dp = AppTheme.spacing.x1_5,
    key: ((index: Int, item: T) -> Any)? = null,
    itemContent: @Composable (item: T) -> Unit,
) {
    if (items.isEmpty()) return

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val layoutDirection = LocalLayoutDirection.current
        val horizontalPadding =
            contentPadding.calculateStartPadding(layoutDirection) +
                contentPadding.calculateEndPadding(layoutDirection)

        val itemWidthDp: Dp? =
            when (itemWidth) {
                is ShowcaseItemWidth.Peek ->
                    ((maxWidth - horizontalPadding - itemSpacing) * itemWidth.visibleFraction)
                        .coerceAtLeast(Dp.Hairline)
                is ShowcaseItemWidth.Fixed -> itemWidth.width
                ShowcaseItemWidth.Wrap -> null
            }
        val widthModifier = if (itemWidthDp != null) Modifier.width(itemWidthDp) else Modifier

        val useGrid = rows > 1 && rowHeight != null
        if (useGrid) {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(rows),
                modifier = Modifier.height(rowHeight * rows + itemSpacing * (rows - 1)),
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                verticalArrangement = Arrangement.spacedBy(itemSpacing),
            ) {
                gridItemsIndexed(items = items, key = key) { _, item ->
                    Box(modifier = widthModifier.fillMaxHeight()) { itemContent(item) }
                }
            }
        } else {
            LazyRow(
                state = state,
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(itemSpacing),
            ) {
                itemsIndexed(items = items, key = key) { _, item ->
                    Box(modifier = widthModifier) { itemContent(item) }
                }
            }
        }
    }
}

/**
 * Builder scope for [ShowcaseFeed], mirroring the `LazyColumn` DSL: call
 * [section] once per category, and [customSection] for a section that renders
 * arbitrary content instead of the default header + row.
 */
public interface ShowcaseFeedScope {
    /**
     * A standard section: a [SectionHeader] (title with an optional trailing
     * action) above a horizontally-scrolling [ShowcaseRow]. Each call may use a
     * different item type, so a feed can mix heterogeneous data sources.
     *
     * @param title Section heading; caller-supplied and localised.
     * @param items Backing data source for this section's row.
     * @param key Stable key for the whole section within the vertical list.
     * @param actionLabel Optional trailing action label (e.g. "See all"). Shown
     *   only when both it and [onAction] are non-null.
     * @param onAction Invoked when the trailing action is tapped.
     * @param itemWidth How each item is sized. See [ShowcaseItemWidth].
     * @param rows Number of stacked rows; greater than `1` needs [rowHeight].
     * @param rowHeight Height of one row; required when [rows] is greater than `1`.
     * @param rowContentDescription Optional accessibility label announced for
     *   the scrolling row. Caller-supplied; when `null` the row stays silent
     *   and TalkBack traverses the items directly.
     * @param itemKey Stable key for each item in the row.
     * @param itemContent Renders a single item.
     */
    public fun <T> section(
        title: String,
        items: List<T>,
        key: Any? = null,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        itemWidth: ShowcaseItemWidth = ShowcaseItemWidth.Peek(),
        rows: Int = 1,
        rowHeight: Dp? = null,
        rowContentDescription: String? = null,
        itemKey: ((index: Int, item: T) -> Any)? = null,
        itemContent: @Composable (item: T) -> Unit,
    )

    /**
     * A fully custom section: [content] is placed directly into the vertical
     * list, so callers can render a hero banner, promo, grid, or anything else.
     *
     * @param key Stable key for the section within the vertical list.
     */
    public fun customSection(
        key: Any? = null,
        content: @Composable () -> Unit,
    )
}

/**
 * A two-directional, App Store–style feed: a vertically-scrolling column of
 * sections where each section scrolls horizontally on its own. Sections are
 * declared with a `LazyColumn`-style DSL via [content]; use
 * [ShowcaseFeedScope.section] for the default header + peeking row, or
 * [ShowcaseFeedScope.customSection] for bespoke content.
 *
 * @param state Hoisted vertical scroll state.
 * @param contentPadding Padding around the whole vertical list.
 * @param verticalItemSpacing Gap between sections.
 * @param content Section declarations.
 */
@Composable
public fun ShowcaseFeed(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(vertical = AppTheme.spacing.x2),
    verticalItemSpacing: Dp = AppTheme.spacing.x3,
    content: ShowcaseFeedScope.() -> Unit,
) {
    val scope = ShowcaseFeedScopeImpl()
    scope.content()

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(verticalItemSpacing),
    ) {
        scope.entries.forEach { entry -> entry() }
    }
}

/**
 * Collects [ShowcaseFeedScope] calls into a list of `LazyListScope` producers so
 * each section can capture its own item type before being replayed into the
 * feed's [LazyColumn]. Kept private — callers only see [ShowcaseFeedScope].
 */
private class ShowcaseFeedScopeImpl : ShowcaseFeedScope {
    val entries: MutableList<androidx.compose.foundation.lazy.LazyListScope.() -> Unit> =
        mutableListOf()

    override fun <T> section(
        title: String,
        items: List<T>,
        key: Any?,
        actionLabel: String?,
        onAction: (() -> Unit)?,
        itemWidth: ShowcaseItemWidth,
        rows: Int,
        rowHeight: Dp?,
        rowContentDescription: String?,
        itemKey: ((index: Int, item: T) -> Any)?,
        itemContent: @Composable (item: T) -> Unit,
    ) {
        entries += {
            item(key = key) {
                Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
                    SectionHeader(
                        title = title,
                        actionLabel = actionLabel,
                        onAction = onAction,
                    )
                    val rowModifier =
                        if (rowContentDescription != null) {
                            Modifier.semantics { contentDescription = rowContentDescription }
                        } else {
                            Modifier
                        }
                    ShowcaseRow(
                        items = items,
                        modifier = rowModifier,
                        itemWidth = itemWidth,
                        rows = rows,
                        rowHeight = rowHeight,
                        key = itemKey,
                        itemContent = itemContent,
                    )
                }
            }
        }
    }

    override fun customSection(
        key: Any?,
        content: @Composable () -> Unit,
    ) {
        entries += {
            item(key = key) { content() }
        }
    }
}

package io.github.maniramezan.compose.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTarget

/**
 * Represents a single segment in [SegmentedContent].
 *
 * The [title] backs the default [DefaultSegmentTitle] label; custom title slots
 * are free to render anything (icons, badges, multi-line text) and may ignore
 * the title string entirely.
 */
@Immutable
public data class SegmentedItem(
    public val title: String,
)

/**
 * Visual style of the selection chrome shown on the active segment.
 */
public enum class SegmentSelectionIndicator {
    /** Filled pill behind the selected segment, on a tinted bar background. iOS-segmented feel. */
    Pill,

    /** Underline beneath the selected segment, Material-tab style. Bar background is transparent. */
    Underline,

    /** No chrome; styling is left entirely to the title slot. */
    None,
}

/**
 * Whether segments are evenly distributed when they fit on one row, or
 * always sized to their intrinsic width.
 */
public enum class SegmentFitMode {
    /** Distribute evenly when titles fit; fall back to horizontal scroll when they don't. */
    EvenWhenFits,

    /** Always size segments to their intrinsic width. Scrolls horizontally when they overflow. */
    Intrinsic,
}

/**
 * Controls whether the segment bar expands to fill the available width or
 * wraps to the natural width of its segments.
 */
public enum class SegmentWidthMode {
    /** Expand to fill available width (default). */
    Fill,

    /** Wrap to the natural width of the segments; scrolls when they overflow. */
    Fit,
}

/**
 * Controls the vertical density (height) of the segment bar.
 */
public enum class SegmentDensity {
    /** Standard internal padding with a 48dp minimum touch target (default). */
    Regular,

    /**
     * Reduced internal padding matching a native compact segmented control,
     * while preserving the same 48dp minimum touch target as [Regular].
     */
    Compact,
}

/**
 * A tab-style picker paired with a client-supplied content slot. Tapping a
 * segment swaps the content with a short crossfade. When all segment titles
 * fit on one row, they distribute evenly (in [SegmentFitMode.EvenWhenFits]);
 * when they overflow, the bar becomes horizontally scrollable and shows a
 * soft edge fade on whichever side has more content.
 *
 * This overload owns selection state. Use the hoisted overload when the
 * caller needs to drive selection externally.
 */
@Composable
public fun SegmentedContent(
    items: List<SegmentedItem>,
    modifier: Modifier = Modifier,
    initialSelectedIndex: Int = 0,
    indicator: SegmentSelectionIndicator = SegmentSelectionIndicator.Pill,
    fitMode: SegmentFitMode = SegmentFitMode.EvenWhenFits,
    widthMode: SegmentWidthMode = SegmentWidthMode.Fill,
    density: SegmentDensity = SegmentDensity.Regular,
    onSelectionChanged: ((Int) -> Unit)? = null,
    segmentTitle: @Composable (index: Int, item: SegmentedItem, isSelected: Boolean) -> Unit =
        { _, item, isSelected -> DefaultSegmentTitle(item.title, isSelected, indicator) },
    segmentContent: @Composable (index: Int, item: SegmentedItem) -> Unit,
) {
    if (items.isEmpty()) return
    val clampedInitial = initialSelectedIndex.coerceIn(0, items.lastIndex)
    var selectedIndex by rememberSaveable { mutableIntStateOf(clampedInitial) }
    SegmentedContent(
        items = items,
        selectedIndex = selectedIndex,
        onSelectionChanged = { index ->
            selectedIndex = index
            onSelectionChanged?.invoke(index)
        },
        modifier = modifier,
        indicator = indicator,
        fitMode = fitMode,
        widthMode = widthMode,
        density = density,
        segmentTitle = segmentTitle,
        segmentContent = segmentContent,
    )
}

/**
 * State-hoisted overload of [SegmentedContent]. The caller drives selection
 * via [selectedIndex] and [onSelectionChanged].
 */
@Composable
public fun SegmentedContent(
    items: List<SegmentedItem>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    indicator: SegmentSelectionIndicator = SegmentSelectionIndicator.Pill,
    fitMode: SegmentFitMode = SegmentFitMode.EvenWhenFits,
    widthMode: SegmentWidthMode = SegmentWidthMode.Fill,
    density: SegmentDensity = SegmentDensity.Regular,
    segmentTitle: @Composable (index: Int, item: SegmentedItem, isSelected: Boolean) -> Unit =
        { _, item, isSelected -> DefaultSegmentTitle(item.title, isSelected, indicator) },
    segmentContent: @Composable (index: Int, item: SegmentedItem) -> Unit,
) {
    if (items.isEmpty()) return
    val safeIndex = selectedIndex.coerceIn(0, items.lastIndex)

    val shortMillis = AppTheme.motion.shortMillis
    val fillModifier = if (widthMode == SegmentWidthMode.Fill) Modifier.fillMaxWidth() else Modifier
    Column(modifier = modifier.then(fillModifier)) {
        SegmentBar(
            items = items,
            selectedIndex = safeIndex,
            onSelectionChanged = onSelectionChanged,
            indicator = indicator,
            fitMode = fitMode,
            widthMode = widthMode,
            density = density,
            segmentTitle = segmentTitle,
        )
        AnimatedContent(
            targetState = safeIndex,
            transitionSpec = {
                fadeIn(animationSpec = tween(shortMillis)) togetherWith
                    fadeOut(animationSpec = tween(shortMillis))
            },
            label = "SegmentedContent",
            modifier = fillModifier,
        ) { idx ->
            segmentContent(idx, items[idx])
        }
    }
}

/**
 * Default rendering for a segment label. Color depends on selection and
 * [indicator] (the Pill indicator fills behind the title and needs onPrimary
 * contrast; other indicators leave the surface visible).
 */
@Composable
internal fun DefaultSegmentTitle(
    title: String,
    isSelected: Boolean,
    indicator: SegmentSelectionIndicator,
) {
    val selectedColor =
        if (indicator == SegmentSelectionIndicator.Pill) {
            AppTheme.colors.onPrimary
        } else {
            AppTheme.colors.primary
        }
    val color by animateColorAsState(
        targetValue = if (isSelected) selectedColor else AppTheme.colors.onSurfaceVariant,
        label = "segmentTitleColor",
    )
    Text(
        text = title,
        style = AppTheme.typography.labelLarge,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun SegmentBar(
    items: List<SegmentedItem>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    indicator: SegmentSelectionIndicator,
    fitMode: SegmentFitMode,
    widthMode: SegmentWidthMode,
    density: SegmentDensity,
    segmentTitle: @Composable (Int, SegmentedItem, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemSpacingPx = with(LocalDensity.current) { AppTheme.spacing.half.roundToPx() }
    val barShape =
        if (indicator == SegmentSelectionIndicator.Pill) AppTheme.shapes.pill else RectangleShape
    val barBg =
        if (indicator == SegmentSelectionIndicator.Pill) AppTheme.colors.surfaceContainer else Color.Transparent
    val fillWidth = widthMode == SegmentWidthMode.Fill
    val outerModifier =
        if (indicator == SegmentSelectionIndicator.Pill) {
            (if (fillWidth) Modifier.fillMaxWidth() else Modifier)
                .background(barBg, barShape)
                .clip(barShape)
                .padding(AppTheme.spacing.half)
        } else {
            if (fillWidth) Modifier.fillMaxWidth() else Modifier
        }

    SubcomposeLayout(
        modifier = modifier.then(outerModifier),
    ) { constraints ->
        // Measure intrinsic title widths (with tap padding) to decide between
        // even distribution and horizontal scroll. This pass is never placed,
        // and its semantics are cleared so the throwaway titles don't appear
        // twice to accessibility services or UI tests.
        val intrinsicPlaceables =
            subcompose("intrinsic") {
                items.forEachIndexed { i, item ->
                    Box(
                        modifier =
                            Modifier
                                .clearAndSetSemantics {}
                                .padding(
                                    horizontal = AppTheme.spacing.x2,
                                    vertical = AppTheme.spacing.x1,
                                ),
                    ) {
                        segmentTitle(i, item, i == selectedIndex)
                    }
                }
            }.map { it.measure(constraints.copy(minWidth = 0)) }

        val gapCount = (items.size - 1).coerceAtLeast(0)
        val totalIntrinsic = intrinsicPlaceables.sumOf { it.width } + itemSpacingPx * gapCount
        val fits = totalIntrinsic <= constraints.maxWidth
        // Even distribution only when explicitly requested and the bar is filling available width.
        val useEven = fitMode == SegmentFitMode.EvenWhenFits && fillWidth && fits
        // Scroll when overflowing, or when Fill+Intrinsic (always scroll regardless of fit).
        val useScrollable = !useEven && (fillWidth || !fits)

        val mainPlaceables =
            subcompose("main") {
                when {
                    useEven ->
                        EvenSegmentRow(
                            items = items,
                            selectedIndex = selectedIndex,
                            onSelectionChanged = onSelectionChanged,
                            indicator = indicator,
                            density = density,
                            segmentTitle = segmentTitle,
                        )
                    useScrollable ->
                        ScrollableSegmentRow(
                            items = items,
                            selectedIndex = selectedIndex,
                            onSelectionChanged = onSelectionChanged,
                            indicator = indicator,
                            density = density,
                            segmentTitle = segmentTitle,
                        )
                    else ->
                        IntrinsicSegmentRow(
                            items = items,
                            selectedIndex = selectedIndex,
                            onSelectionChanged = onSelectionChanged,
                            indicator = indicator,
                            density = density,
                            segmentTitle = segmentTitle,
                        )
                }
            }.map { it.measure(constraints) }

        val width =
            if (fillWidth) {
                constraints.maxWidth
            } else {
                totalIntrinsic.coerceIn(constraints.minWidth, constraints.maxWidth)
            }
        val height = mainPlaceables.maxOfOrNull { it.height } ?: 0
        layout(width, height) {
            mainPlaceables.forEach { it.placeRelative(0, 0) }
        }
    }
}

@Composable
private fun EvenSegmentRow(
    items: List<SegmentedItem>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    indicator: SegmentSelectionIndicator,
    density: SegmentDensity,
    segmentTitle: @Composable (Int, SegmentedItem, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
    ) {
        items.forEachIndexed { i, item ->
            SegmentSlot(
                index = i,
                item = item,
                isSelected = i == selectedIndex,
                indicator = indicator,
                onClick = { onSelectionChanged(i) },
                density = density,
                segmentTitle = segmentTitle,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun IntrinsicSegmentRow(
    items: List<SegmentedItem>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    indicator: SegmentSelectionIndicator,
    density: SegmentDensity,
    segmentTitle: @Composable (Int, SegmentedItem, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
    ) {
        items.forEachIndexed { i, item ->
            SegmentSlot(
                index = i,
                item = item,
                isSelected = i == selectedIndex,
                indicator = indicator,
                onClick = { onSelectionChanged(i) },
                density = density,
                segmentTitle = segmentTitle,
            )
        }
    }
}

@Composable
private fun ScrollableSegmentRow(
    items: List<SegmentedItem>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    indicator: SegmentSelectionIndicator,
    density: SegmentDensity,
    segmentTitle: @Composable (Int, SegmentedItem, Boolean) -> Unit,
) {
    val state = rememberLazyListState()
    // Leave a sliver of the adjacent segment visible when scrolling the
    // selection into view, so it stays obvious there's more to either side.
    // The negative offset positions the selected segment slightly after the
    // viewport start (peeking the previous one); it clamps naturally at the
    // first/last item, where the opposite neighbour peeks instead.
    val peekPx = with(LocalDensity.current) { AppTheme.spacing.x4.roundToPx() }
    LaunchedEffect(selectedIndex) {
        state.animateScrollToItem(selectedIndex, scrollOffset = -peekPx)
    }
    val showLeading by remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 0 || state.firstVisibleItemScrollOffset > 0
        }
    }
    val showTrailing by remember {
        derivedStateOf {
            val last = state.layoutInfo.visibleItemsInfo.lastOrNull()
            last != null &&
                (last.index < items.lastIndex || (last.offset + last.size) > state.layoutInfo.viewportEndOffset)
        }
    }
    val leadingAlpha by animateFloatAsState(if (showLeading) 1f else 0f, label = "leadingFade")
    val trailingAlpha by animateFloatAsState(if (showTrailing) 1f else 0f, label = "trailingFade")

    val fadeColor =
        if (indicator == SegmentSelectionIndicator.Pill) {
            AppTheme.colors.surfaceContainer
        } else {
            AppTheme.colors.surface
        }
    val fadeWidthPx = with(LocalDensity.current) { AppTheme.spacing.x3.toPx() }
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    LazyRow(
        state = state,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.half),
        modifier =
            Modifier
                .fillMaxWidth()
                .selectableGroup()
                .edgeFade(
                    leadingAlpha = leadingAlpha,
                    trailingAlpha = trailingAlpha,
                    color = fadeColor,
                    fadeWidthPx = fadeWidthPx,
                    isRtl = isRtl,
                ),
    ) {
        itemsIndexed(items, key = { i, _ -> i }) { i, item ->
            SegmentSlot(
                index = i,
                item = item,
                isSelected = i == selectedIndex,
                indicator = indicator,
                onClick = { onSelectionChanged(i) },
                density = density,
                segmentTitle = segmentTitle,
            )
        }
    }
}

/**
 * Overlays a soft fade-to-[color] gradient on the leading and/or trailing edge,
 * painted within the element's own bounds so the fade always matches the row
 * height. [leadingAlpha] / [trailingAlpha] are layout-direction-aware; the draw
 * happens in raw left/right pixel space so the mapping is swapped under RTL.
 */
private fun Modifier.edgeFade(
    leadingAlpha: Float,
    trailingAlpha: Float,
    color: Color,
    fadeWidthPx: Float,
    isRtl: Boolean,
): Modifier =
    drawWithContent {
        drawContent()
        val leftAlpha = if (isRtl) trailingAlpha else leadingAlpha
        val rightAlpha = if (isRtl) leadingAlpha else trailingAlpha
        val w = fadeWidthPx.coerceAtMost(size.width)
        if (leftAlpha > 0f) {
            drawRect(
                brush =
                    Brush.horizontalGradient(
                        colors = listOf(color, Color.Transparent),
                        startX = 0f,
                        endX = w,
                    ),
                size = Size(w, size.height),
                alpha = leftAlpha,
            )
        }
        if (rightAlpha > 0f) {
            drawRect(
                brush =
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, color),
                        startX = size.width - w,
                        endX = size.width,
                    ),
                topLeft = Offset(size.width - w, 0f),
                size = Size(w, size.height),
                alpha = rightAlpha,
            )
        }
    }

@Composable
private fun SegmentSlot(
    index: Int,
    item: SegmentedItem,
    isSelected: Boolean,
    indicator: SegmentSelectionIndicator,
    onClick: () -> Unit,
    density: SegmentDensity,
    segmentTitle: @Composable (Int, SegmentedItem, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pillBg by animateColorAsState(
        targetValue =
            if (indicator == SegmentSelectionIndicator.Pill && isSelected) {
                AppTheme.colors.primary
            } else {
                Color.Transparent
            },
        label = "segmentBg",
    )
    val underlineColor by animateColorAsState(
        targetValue =
            if (indicator == SegmentSelectionIndicator.Underline && isSelected) {
                AppTheme.colors.primary
            } else {
                Color.Transparent
            },
        label = "segmentUnderline",
    )
    val slotShape =
        if (indicator == SegmentSelectionIndicator.Pill) AppTheme.shapes.pill else RectangleShape
    val titleVerticalPadding =
        if (density == SegmentDensity.Compact) AppTheme.spacing.half else AppTheme.spacing.x1

    Box(
        modifier =
            modifier
                .clip(slotShape)
                .background(pillBg)
                .selectable(selected = isSelected, role = Role.Tab, onClick = onClick)
                .minimumTouchTarget(minimumTouchTargetSize()),
        contentAlignment = Alignment.Center,
    ) {
        // Title centred both axes within the (≥48dp) slot.
        Box(
            modifier =
                Modifier.padding(horizontal = AppTheme.spacing.x2, vertical = titleVerticalPadding),
            contentAlignment = Alignment.Center,
        ) {
            segmentTitle(index, item, isSelected)
        }
        // Tab-style underline pinned to the bottom edge of the slot.
        if (indicator == SegmentSelectionIndicator.Underline) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(AppTheme.spacing.strokeThick)
                        .background(underlineColor),
            )
        }
    }
}

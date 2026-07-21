package io.github.maniramezan.compose.sample

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.PageDirection
import io.github.maniramezan.compose.components.PageFooterStyle
import io.github.maniramezan.compose.components.PageTitleAlignment
import io.github.maniramezan.compose.components.PaginatedContent
import io.github.maniramezan.compose.components.PaginationPage
import io.github.maniramezan.compose.components.PrimaryButton
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.SegmentDensity
import io.github.maniramezan.compose.components.SegmentFitMode
import io.github.maniramezan.compose.components.SegmentSelectionIndicator
import io.github.maniramezan.compose.components.SegmentedContent
import io.github.maniramezan.compose.components.SegmentedItem
import io.github.maniramezan.compose.components.TabBar
import io.github.maniramezan.compose.components.TabBarItemData
import io.github.maniramezan.compose.theme.AppTheme
import kotlin.math.roundToInt

// ─────────────────────────────────────────────────────────────────────────────
// Pagination
// ─────────────────────────────────────────────────────────────────────────────

private val allDemoPaginationPages =
    listOf(
        PaginationPage(title = "Popular"),
        PaginationPage(title = "New Releases"),
        PaginationPage(title = "Top Rated"),
        PaginationPage(title = "Trending"),
        PaginationPage(title = "Staff Picks"),
        PaginationPage(title = "Coming Soon"),
    )

@Composable
private fun PagerPlaceholder(title: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(AppTheme.spacing.x2),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "$title content")
    }
}

@Composable
internal fun PaginatedContentPage() {
    val alignments = listOf("Leading", "Center", "Trailing")
    var alignmentIndex by remember { mutableIntStateOf(0) }
    val titleAlignment =
        when (alignmentIndex) {
            1 -> PageTitleAlignment.Center
            2 -> PageTitleAlignment.Trailing
            else -> PageTitleAlignment.Leading
        }

    val directions = listOf("Bidirectional", "Unidirectional")
    var directionIndex by remember { mutableIntStateOf(0) }
    val direction = if (directionIndex == 0) PageDirection.Bidirectional else PageDirection.Unidirectional

    val footers = listOf("Dots", "Progress", "None")
    var footerIndex by remember { mutableIntStateOf(0) }
    val footerStyle =
        when (footerIndex) {
            1 -> PageFooterStyle.Progress
            2 -> PageFooterStyle.None
            else -> PageFooterStyle.Dots
        }

    val minPages = 2
    val maxPages = allDemoPaginationPages.size
    var pageCountSlider by remember { mutableFloatStateOf(3f) }
    val pageCount = pageCountSlider.roundToInt().coerceIn(minPages, maxPages)
    val pages = allDemoPaginationPages.take(pageCount)

    var lastPage by remember { mutableIntStateOf(0) }
    var hintResetKey by remember { mutableIntStateOf(0) }
    var rtlMode by remember { mutableStateOf(false) }
    val layoutDirection = if (rtlMode) LayoutDirection.Rtl else LayoutDirection.Ltr

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Text(text = "Current page: $lastPage")
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            key(hintResetKey) {
                PaginatedContent(
                    pages = pages,
                    titleAlignment = titleAlignment,
                    direction = direction,
                    footerStyle = footerStyle,
                    showScrollHint = true,
                    onPageChanged = { lastPage = it },
                ) { _, page -> PagerPlaceholder(page.title) }
            }
        }
        ControlsDivider()
        SecondaryButton(
            text = "Replay scroll hint",
            onClick = { hintResetKey++ },
            modifier = Modifier.fillMaxWidth(),
        )
        ControlSwitch(
            label = "RTL layout",
            checked = rtlMode,
            onCheckedChange = { rtlMode = it },
        )
        ControlSlider(
            label = "Pages: $pageCount",
            value = pageCountSlider,
            onValueChange = { pageCountSlider = it },
            valueRange = minPages.toFloat()..maxPages.toFloat(),
        )
        ControlSegmented(
            label = "Title alignment",
            options = alignments,
            selectedIndex = alignmentIndex,
            onOptionSelected = { alignmentIndex = it },
        )
        ControlSegmented(
            label = "Direction",
            options = directions,
            selectedIndex = directionIndex,
            onOptionSelected = { directionIndex = it },
        )
        ControlSegmented(
            label = "Footer",
            options = footers,
            selectedIndex = footerIndex,
            onOptionSelected = { footerIndex = it },
        )
    }
}

private val allDemoSegmentedItems =
    listOf(
        SegmentedItem(title = "Overview"),
        SegmentedItem(title = "Activity"),
        SegmentedItem(title = "Settings"),
        SegmentedItem(title = "Notifications"),
        SegmentedItem(title = "Permissions"),
        SegmentedItem(title = "Integrations"),
        SegmentedItem(title = "Billing"),
    )

@Composable
private fun SegmentPlaceholder(title: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(AppTheme.spacing.x2),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$title content",
            style = AppTheme.typography.titleSmall,
        )
    }
}

@Composable
private fun PlainSegmentTitle(
    title: String,
    isSelected: Boolean,
    indicator: SegmentSelectionIndicator,
) {
    val color =
        when {
            isSelected && indicator == SegmentSelectionIndicator.Pill -> AppTheme.colors.onPrimary
            isSelected -> AppTheme.colors.primary
            else -> AppTheme.colors.onSurfaceVariant
        }
    Text(
        text = title,
        style = AppTheme.typography.labelLarge,
        color = color,
        maxLines = 1,
    )
}

@Composable
private fun IconSegmentTitle(
    title: String,
    isSelected: Boolean,
    indicator: SegmentSelectionIndicator,
) {
    val color =
        if (isSelected) {
            if (indicator == SegmentSelectionIndicator.Pill) AppTheme.colors.onPrimary else AppTheme.colors.primary
        } else {
            AppTheme.colors.onSurfaceVariant
        }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
    ) {
        Icon(
            imageVector = AppTheme.icons.check.imageVector,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(AppTheme.spacing.x2),
        )
        Text(
            text = title,
            style = AppTheme.typography.labelLarge,
            color = color,
            maxLines = 1,
        )
    }
}

@Composable
internal fun SegmentedContentPage() {
    val indicatorOptions = listOf("Pill", "Underline", "None")
    var indicatorIndex by remember { mutableIntStateOf(0) }
    val indicator =
        when (indicatorIndex) {
            1 -> SegmentSelectionIndicator.Underline
            2 -> SegmentSelectionIndicator.None
            else -> SegmentSelectionIndicator.Pill
        }

    val fitOptions = listOf("EvenWhenFits", "Intrinsic")
    var fitIndex by remember { mutableIntStateOf(0) }
    val fitMode = if (fitIndex == 0) SegmentFitMode.EvenWhenFits else SegmentFitMode.Intrinsic

    val densityOptions = listOf("Regular", "Compact")
    var densityIndex by remember { mutableIntStateOf(0) }
    val density = if (densityIndex == 0) SegmentDensity.Regular else SegmentDensity.Compact

    val minSegments = 2
    val maxSegments = allDemoSegmentedItems.size
    var segmentCountSlider by remember { mutableFloatStateOf(3f) }
    val segmentCount = segmentCountSlider.roundToInt().coerceIn(minSegments, maxSegments)
    val items = allDemoSegmentedItems.take(segmentCount)

    var useIconTitle by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    // Keep the selection valid when the segment count shrinks below it.
    val safeSelectedIndex = selectedIndex.coerceIn(0, items.lastIndex)

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Text(text = "Selected: ${items[safeSelectedIndex].title}")
        SegmentedContent(
            items = items,
            selectedIndex = safeSelectedIndex,
            onSelectionChanged = { selectedIndex = it },
            indicator = indicator,
            fitMode = fitMode,
            density = density,
            segmentTitle = { _, item, isSelected ->
                if (useIconTitle) {
                    IconSegmentTitle(item.title, isSelected, indicator)
                } else {
                    PlainSegmentTitle(item.title, isSelected, indicator)
                }
            },
        ) { _, item -> SegmentPlaceholder(item.title) }
        ControlsDivider()
        ControlSlider(
            label = "Segments: $segmentCount",
            value = segmentCountSlider,
            onValueChange = { segmentCountSlider = it },
            valueRange = minSegments.toFloat()..maxSegments.toFloat(),
        )
        ControlSegmented(
            label = "Indicator",
            options = indicatorOptions,
            selectedIndex = indicatorIndex,
            onOptionSelected = { indicatorIndex = it },
        )
        ControlSegmented(
            label = "Fit mode",
            options = fitOptions,
            selectedIndex = fitIndex,
            onOptionSelected = { fitIndex = it },
        )
        ControlSegmented(
            label = "Density",
            options = densityOptions,
            selectedIndex = densityIndex,
            onOptionSelected = { densityIndex = it },
        )
        ControlSwitch(
            label = "Custom title slot (icon + label)",
            checked = useIconTitle,
            onCheckedChange = { useIconTitle = it },
        )
    }
}

@Composable
private fun TabBarViewPlaceholder(title: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(96.dp)
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.surfaceContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "$title view", style = AppTheme.typography.titleSmall)
    }
}

@Composable
internal fun TabBarPage() {
    var selection by remember { mutableIntStateOf(0) }
    var showBadge by remember { mutableStateOf(true) }
    var rtlMode by remember { mutableStateOf(false) }
    val layoutDirection = if (rtlMode) LayoutDirection.Rtl else LayoutDirection.Ltr
    val labels = listOf("Home", "Tasks", "Alerts")

    val items =
        listOf(
            TabBarItemData(
                value = 0,
                icon = { Icon(imageVector = AppTheme.icons.check.imageVector, contentDescription = null) },
                label = { Text(labels[0]) },
            ),
            TabBarItemData(
                value = 1,
                icon = { Icon(imageVector = AppTheme.icons.check.imageVector, contentDescription = null) },
                label = { Text(labels[1]) },
            ),
            TabBarItemData(
                value = 2,
                icon = { Icon(imageVector = AppTheme.icons.close.imageVector, contentDescription = null) },
                label = { Text(labels[2]) },
                // Reuses Material's Badge composable — BadgedBox anchors it to the icon's
                // top-end corner, which mirrors to top-start automatically under RTL.
                badge = if (showBadge) ({ Badge { Text("5") } }) else null,
            ),
        )

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                // Tapping a tab swaps the view shown above the bar, mimicking real navigation
                // between top-level destinations.
                TabBarViewPlaceholder(title = labels[selection])
                TabBar(items = items, selection = selection, onSelectionChange = { selection = it })
            }
        }
        ControlsDivider()
        ControlSwitch(label = "Show badge", checked = showBadge, onCheckedChange = { showBadge = it })
        ControlSwitch(
            label = "RTL layout",
            checked = rtlMode,
            onCheckedChange = { rtlMode = it },
        )
        ControlsDivider()
        val context = LocalContext.current
        PrimaryButton(
            text = "Open full-screen demo",
            onClick = {
                context.startActivity(Intent(context, TabBarFullScreenDemoActivity::class.java))
            },
        )
    }
}

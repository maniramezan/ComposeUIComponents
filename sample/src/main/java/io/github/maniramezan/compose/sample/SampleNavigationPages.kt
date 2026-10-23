package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.AdaptiveNavScaffold
import io.github.maniramezan.compose.components.IconButton
import io.github.maniramezan.compose.components.LargeTopAppBar
import io.github.maniramezan.compose.components.MediumTopAppBar
import io.github.maniramezan.compose.components.NavRail
import io.github.maniramezan.compose.components.PageIndicator
import io.github.maniramezan.compose.components.SectionHeader
import io.github.maniramezan.compose.components.TabBar
import io.github.maniramezan.compose.components.TabBarItem
import io.github.maniramezan.compose.components.TabBarItemData
import io.github.maniramezan.compose.components.TabRow
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.components.TopAppBar
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Navigation
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun TopAppBarPage() {
    var showNavIcon by remember { mutableStateOf(false) }
    var showAction by remember { mutableStateOf(false) }
    val variants = listOf("Small", "Medium", "Large")
    var variantIndex by remember { mutableIntStateOf(0) }

    SamplePage(
        preview = {
            val navigationIcon: @Composable () -> Unit = {
                if (showNavIcon) {
                    IconButton(icon = AppTheme.icons.close, contentDescription = "Back", onClick = {})
                }
            }
            val actions: @Composable () -> Unit = {
                if (showAction) {
                    IconButton(icon = AppTheme.icons.check, contentDescription = "Save", onClick = {})
                }
            }
            when (variantIndex) {
                0 -> TopAppBar(title = "Screen Title", navigationIcon = navigationIcon, actions = actions)
                1 -> MediumTopAppBar(title = "Screen Title", navigationIcon = navigationIcon, actions = actions)
                else -> LargeTopAppBar(title = "Screen Title", navigationIcon = navigationIcon, actions = actions)
            }
        },
        controls = {
            ControlSegmented(
                label = "Variant",
                options = variants,
                selectedIndex = variantIndex,
                onOptionSelected = { variantIndex = it },
            )
            ControlSwitch(
                label = "Navigation icon",
                checked = showNavIcon,
                onCheckedChange = { showNavIcon = it },
            )
            ControlSwitch(label = "Action icon", checked = showAction, onCheckedChange = { showAction = it })
        },
    )
}

@Composable
internal fun TabRowPage() {
    var index by remember { mutableIntStateOf(0) }
    val tabs = listOf("General", "Billing", "Security")

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        TabRow(tabs = tabs, selectedIndex = index, onItemSelected = { index = it })
        Text("Selected: ${tabs[index]}")
    }
}

@Composable
internal fun NavRailPage() {
    var index by remember { mutableIntStateOf(0) }
    var showBadge by remember { mutableStateOf(true) }
    val labels = listOf("Home", "Tasks", "Close")
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
                badge = if (showBadge) ({ Text("3") }) else null,
            ),
            TabBarItemData(
                value = 2,
                icon = { Icon(imageVector = AppTheme.icons.close.imageVector, contentDescription = null) },
                label = { Text(labels[2]) },
            ),
        )

    SamplePage(
        preview = {
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                NavRail(items = items, selection = index, onSelectionChange = { index = it })
                Column {
                    SectionHeader(title = "NavRail")
                    Text("Selected: ${labels[index]}")
                }
            }
        },
        controls = {
            ControlSwitch(label = "Show badge", checked = showBadge, onCheckedChange = { showBadge = it })
        },
    )
}

@Composable
internal fun TabBarItemPage() {
    var selected by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }
    var showLabel by remember { mutableStateOf(true) }
    var showBadge by remember { mutableStateOf(true) }

    SamplePage(
        preview = {
            TabBar {
                TabBarItem(
                    value = true,
                    selection = selected,
                    onSelectionChange = { selected = it },
                    icon = { Icon(imageVector = AppTheme.icons.check.imageVector, contentDescription = null) },
                    enabled = enabled,
                    label = if (showLabel) ({ Text("Home") }) else null,
                    badge = if (showBadge) ({ Text("3") }) else null,
                    contentDescription = if (showLabel) "" else "Home",
                )
            }
        },
        controls = {
            ControlSwitch(label = "Selected", checked = selected, onCheckedChange = { selected = it })
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ControlSwitch(label = "Label", checked = showLabel, onCheckedChange = { showLabel = it })
            ControlSwitch(label = "Badge", checked = showBadge, onCheckedChange = { showBadge = it })
        },
    )
}

@Composable
internal fun AdaptiveNavScaffoldPage() {
    val layouts = listOf("Auto (width)", "Force tab bar", "Force rail")
    var layoutIndex by remember { mutableIntStateOf(2) }
    var showTopBar by remember { mutableStateOf(true) }
    var destinationCount by remember { mutableIntStateOf(3) }
    var selection by remember { mutableIntStateOf(0) }
    val destinations = listOf("Home", "Search", "Library", "Profile").take(destinationCount)

    SamplePage(
        preview = {
            val spacing = AppTheme.spacing
            // The sample pane is phone-width, so the breakpoint is overridden to show both layouts.
            val breakpoint =
                when (layoutIndex) {
                    1 -> SAMPLE_UNREACHABLE_BREAKPOINT
                    2 -> 0.dp
                    else -> spacing.expandedNavigationBreakpoint
                }
            AppTheme(spacing = spacing.copy(expandedNavigationBreakpoint = breakpoint)) {
                AdaptiveNavScaffold(
                    items =
                        destinations.mapIndexed { index, name ->
                            TabBarItemData(
                                value = index,
                                icon = { Icon(AppTheme.icons.check.imageVector, contentDescription = null) },
                                label = { Text(name) },
                            )
                        },
                    selection = selection.coerceAtMost(destinations.lastIndex),
                    onSelectionChange = { selection = it },
                    modifier = Modifier.height(SAMPLE_SCAFFOLD_HEIGHT),
                    topBar = { if (showTopBar) TopAppBar(title = destinations[selection.coerceAtMost(destinations.lastIndex)]) },
                ) { padding ->
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("${destinations[selection.coerceAtMost(destinations.lastIndex)]} content")
                    }
                }
            }
        },
        controls = {
            ControlSegmented(
                label = "Layout",
                options = layouts,
                selectedIndex = layoutIndex,
                onOptionSelected = { layoutIndex = it },
            )
            ControlSegmented(
                label = "Destinations",
                options = listOf("2", "3", "4"),
                selectedIndex = destinationCount - 2,
                onOptionSelected = { destinationCount = it + 2 },
            )
            ControlSwitch(label = "Top bar", checked = showTopBar, onCheckedChange = { showTopBar = it })
        },
    )
}

private val SAMPLE_SCAFFOLD_HEIGHT = 360.dp
private val SAMPLE_UNREACHABLE_BREAKPOINT = 10_000.dp

@Composable
internal fun PageIndicatorPage() {
    var pageCount by remember { mutableIntStateOf(5) }
    var currentPage by remember { mutableIntStateOf(0) }
    var announce by remember { mutableStateOf(true) }
    var prominent by remember { mutableStateOf(false) }
    val safePage = currentPage.coerceAtMost(pageCount - 1)

    SamplePage(
        preview = {
            PageIndicator(
                pageCount = pageCount,
                currentPage = safePage,
                activeColor = if (prominent) AppTheme.colors.error else AppTheme.colors.primary,
                inactiveColor = if (prominent) AppTheme.colors.onSurfaceVariant else AppTheme.colors.surfaceVariant,
                pagePositionDescription =
                    if (announce) {
                        { index, count -> "Page ${index + 1} of $count" }
                    } else {
                        null
                    },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
                TextButton(text = "Previous", onClick = { currentPage = safePage - 1 }, enabled = safePage > 0)
                TextButton(text = "Next", onClick = { currentPage = safePage + 1 }, enabled = safePage < pageCount - 1)
            }
        },
        controls = {
            ControlSegmented(
                label = "Pages",
                options = listOf("3", "5", "7"),
                selectedIndex = (pageCount - 3) / 2,
                onOptionSelected = { pageCount = it * 2 + 3 },
            )
            ControlSwitch(label = "Announce position", checked = announce, onCheckedChange = { announce = it })
            ControlSwitch(label = "Alternate colors", checked = prominent, onCheckedChange = { prominent = it })
        },
    )
}

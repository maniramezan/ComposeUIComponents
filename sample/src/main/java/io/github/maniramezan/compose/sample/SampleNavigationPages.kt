package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import io.github.maniramezan.compose.components.Badge
import io.github.maniramezan.compose.components.IconButton
import io.github.maniramezan.compose.components.LargeTopAppBar
import io.github.maniramezan.compose.components.MediumTopAppBar
import io.github.maniramezan.compose.components.NavRail
import io.github.maniramezan.compose.components.PageIndicator
import io.github.maniramezan.compose.components.SectionHeader
import io.github.maniramezan.compose.components.TabBarItemData
import io.github.maniramezan.compose.components.TabRow
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.components.TopAppBar
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Navigation
// ─────────────────────────────────────────────────────────────────────────────

/** Fixed height for demos that host a Scaffold inside the scrolling detail pane. */
private val SCAFFOLD_DEMO_HEIGHT = 360.dp

@Composable
internal fun TopAppBarPage() {
    var size by remember { mutableIntStateOf(0) }
    var showNavIcon by remember { mutableStateOf(false) }
    var showAction by remember { mutableStateOf(false) }
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

    SamplePage(
        preview = {
            when (size) {
                0 -> TopAppBar(title = "Screen Title", navigationIcon = navigationIcon, actions = actions)
                1 -> MediumTopAppBar(title = "Screen Title", navigationIcon = navigationIcon, actions = actions)
                else -> LargeTopAppBar(title = "Screen Title", navigationIcon = navigationIcon, actions = actions)
            }
        },
        controls = {
            ControlSegmented(
                label = "Size",
                options = listOf("Small", "Medium", "Large"),
                selectedIndex = size,
                onOptionSelected = { size = it },
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
    val allTabs = listOf("General", "Billing", "Security", "Advanced")
    var tabCount by remember { mutableIntStateOf(3) }
    var index by remember { mutableIntStateOf(0) }
    val tabs = allTabs.take(tabCount)
    val safeIndex = index.coerceAtMost(tabs.lastIndex)

    SamplePage(
        preview = {
            TabRow(tabs = tabs, selectedIndex = safeIndex, onItemSelected = { index = it })
            Text("Selected: ${tabs[safeIndex]}")
        },
        controls = {
            ControlSegmented(
                label = "Tabs",
                options = listOf("2", "3", "4"),
                selectedIndex = tabCount - 2,
                onOptionSelected = { tabCount = it + 2 },
            )
        },
    )
}

@Composable
internal fun NavRailPage() {
    var index by remember { mutableIntStateOf(0) }
    var showBadge by remember { mutableStateOf(true) }
    val labels = listOf("Home", "Tasks", "Close")

    SamplePage(
        preview = {
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                NavRail(
                    items = sampleDestinations(labels = labels, badgeCount = if (showBadge) 3 else null),
                    selection = index,
                    onSelectionChange = { index = it },
                )
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
internal fun AdaptiveNavScaffoldPage() {
    var selection by remember { mutableIntStateOf(0) }
    var forceRail by remember { mutableStateOf(false) }
    var showBadge by remember { mutableStateOf(true) }
    val labels = listOf("Home", "Tasks", "Close")
    val spacing = AppTheme.spacing

    SamplePage(
        preview = {
            // The scaffold switches on the theme's expandedNavigationBreakpoint. Re-provide the
            // current tokens with a zero breakpoint to preview the rail layout on a phone.
            AppTheme(
                lightColors = AppTheme.colors,
                darkColors = AppTheme.colors,
                spacing = if (forceRail) spacing.copy(expandedNavigationBreakpoint = 0.dp) else spacing,
                motion = AppTheme.motion,
                icons = AppTheme.icons,
                typography = AppTheme.typography,
                shapes = AppTheme.shapes,
            ) {
                AdaptiveNavScaffold(
                    items = sampleDestinations(labels = labels, badgeCount = if (showBadge) 3 else null),
                    selection = selection,
                    onSelectionChange = { selection = it },
                    modifier = Modifier.fillMaxWidth().height(SCAFFOLD_DEMO_HEIGHT),
                    topBar = { TopAppBar(title = labels[selection]) },
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "${labels[selection]} content")
                    }
                }
            }
        },
        controls = {
            ControlSwitch(label = "Force rail layout", checked = forceRail, onCheckedChange = { forceRail = it })
            ControlSwitch(label = "Show badge", checked = showBadge, onCheckedChange = { showBadge = it })
        },
    )
}

@Composable
internal fun PageIndicatorPage() {
    var pageCount by remember { mutableIntStateOf(5) }
    var currentPage by remember { mutableIntStateOf(0) }
    var announce by remember { mutableStateOf(true) }
    val safePage = currentPage.coerceAtMost(pageCount - 1)

    SamplePage(
        preview = {
            PageIndicator(
                pageCount = pageCount,
                currentPage = safePage,
                pagePositionDescription =
                    if (announce) {
                        { index, count -> "Page ${index + 1} of $count" }
                    } else {
                        null
                    },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
                TextButton(text = "Previous", onClick = { currentPage = (safePage - 1).coerceAtLeast(0) })
                TextButton(text = "Next", onClick = { currentPage = (safePage + 1).coerceAtMost(pageCount - 1) })
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
        },
    )
}

@Composable
internal fun SectionHeaderPage() {
    var longTitle by remember { mutableStateOf(false) }
    var showAction by remember { mutableStateOf(true) }
    var actionCount by remember { mutableIntStateOf(0) }

    SamplePage(
        preview = {
            SectionHeader(
                title = if (longTitle) "A section title long enough to wrap onto a second line" else "Recent",
                actionLabel = if (showAction) "See all" else null,
                onAction = if (showAction) ({ actionCount += 1 }) else null,
            )
            Text(text = "Action taps: $actionCount")
        },
        controls = {
            ControlSwitch(label = "Long title", checked = longTitle, onCheckedChange = { longTitle = it })
            ControlSwitch(label = "Show action", checked = showAction, onCheckedChange = { showAction = it })
        },
    )
}

/** Three generic destinations shared by the NavRail and AdaptiveNavScaffold demos. */
private fun sampleDestinations(
    labels: List<String>,
    badgeCount: Int?,
): List<TabBarItemData<Int>> =
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
            badge = if (badgeCount != null) ({ Badge(count = badgeCount) }) else null,
        ),
        TabBarItemData(
            value = 2,
            icon = { Icon(imageVector = AppTheme.icons.close.imageVector, contentDescription = null) },
            label = { Text(labels[2]) },
        ),
    )

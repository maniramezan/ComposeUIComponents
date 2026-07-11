package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.IconToken
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight
import androidx.compose.material3.LargeTopAppBar as MaterialLargeTopAppBar
import androidx.compose.material3.MediumTopAppBar as MaterialMediumTopAppBar
import androidx.compose.material3.TopAppBar as MaterialTopAppBar

/** A destination entry shared by [BottomBar], [NavRail], and [AdaptiveNavScaffold]. */
@Immutable
public data class NavigationItem(
    public val label: String,
    public val icon: IconToken,
    /**
     * Accessibility label for icon-only or custom navigation presentations. The
     * built-in bottom bar and rail render text labels, so their icons are
     * decorative and this value is not read there.
     */
    public val contentDescription: String = label,
    public val badge: String? = null,
    /**
     * Spoken description for the [badge], so screen readers announce something
     * meaningful (e.g. `"5 unread"`) instead of the bare badge text. Supply a
     * localized string; when `null` the badge text itself is read.
     */
    public val badgeContentDescription: String? = null,
)

/** A themed top app bar with a [title] and optional [navigationIcon]/[actions] slots. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    MaterialTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = { actions() },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.colors.surface,
                titleContentColor = AppTheme.colors.onSurface,
                navigationIconContentColor = AppTheme.colors.primary,
                actionIconContentColor = AppTheme.colors.primary,
            ),
    )
}

/** A bottom navigation bar for compact-width screens; see [AdaptiveNavScaffold] for width-aware switching. */
@Composable
public fun BottomBar(
    items: List<NavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                icon = { NavigationItemIcon(item) },
                label = { Text(text = item.label) },
                modifier = Modifier.minimumTouchTargetHeight(minimumTouchTargetSize()),
            )
        }
    }
}

/** A primary tab row for switching between [tabs] at the same navigation level. */
@Composable
public fun TabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                modifier = Modifier.minimumTouchTargetHeight(minimumTouchTargetSize()),
                text = { Text(text = tab) },
            )
        }
    }
}

/** A side navigation rail for medium/expanded-width screens; see [AdaptiveNavScaffold] for width-aware switching. */
@Composable
public fun NavRail(
    items: List<NavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        items.forEachIndexed { index, item ->
            NavigationRailItem(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                icon = { NavigationItemIcon(item) },
                label = { Text(text = item.label) },
                modifier = Modifier.minimumTouchTargetHeight(minimumTouchTargetSize()),
            )
        }
    }
}

/** A themed medium (two-line) top app bar with a [title] and optional [navigationIcon]/[actions] slots. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MediumTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    MaterialMediumTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = { actions() },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.colors.surface,
                titleContentColor = AppTheme.colors.onSurface,
                navigationIconContentColor = AppTheme.colors.primary,
                actionIconContentColor = AppTheme.colors.primary,
            ),
    )
}

/** A themed large (three-line) top app bar with a [title] and optional [navigationIcon]/[actions] slots. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LargeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    MaterialLargeTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = { actions() },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.colors.surface,
                titleContentColor = AppTheme.colors.onSurface,
                navigationIconContentColor = AppTheme.colors.primary,
                actionIconContentColor = AppTheme.colors.primary,
            ),
    )
}

/**
 * An adaptive navigation scaffold that shows a [BottomBar] on compact screens
 * (< 600 dp wide) and a [NavRail] on medium/expanded screens. Use the [topBar]
 * slot for a [TopAppBar], [MediumTopAppBar], or [LargeTopAppBar].
 */
@Composable
public fun AdaptiveNavScaffold(
    items: List<NavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val isExpandedWidth = LocalConfiguration.current.screenWidthDp >= 600
    if (isExpandedWidth) {
        Scaffold(topBar = topBar, modifier = modifier) { innerPadding ->
            Row(modifier = Modifier.padding(innerPadding)) {
                NavRail(
                    items = items,
                    selectedIndex = selectedIndex,
                    onItemSelected = onItemSelected,
                )
                Box(modifier = Modifier.weight(1f)) {
                    content(PaddingValues())
                }
            }
        }
    } else {
        Scaffold(
            modifier = modifier,
            topBar = topBar,
            bottomBar = {
                BottomBar(
                    items = items,
                    selectedIndex = selectedIndex,
                    onItemSelected = onItemSelected,
                )
            },
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

@Composable
private fun NavigationItemIcon(item: NavigationItem) {
    if (item.badge != null) {
        val badgeDescription = item.badgeContentDescription
        BadgedBox(
            badge = {
                Badge(
                    modifier =
                        if (badgeDescription != null) {
                            Modifier.clearAndSetSemantics { contentDescription = badgeDescription }
                        } else {
                            Modifier
                        },
                ) {
                    Text(text = item.badge)
                }
            },
        ) {
            Icon(
                imageVector = item.icon.imageVector,
                contentDescription = null, // @check:suppress — decorative; the visible label names the navigation item
            )
        }
    } else {
        Icon(
            imageVector = item.icon.imageVector,
            contentDescription = null, // @check:suppress — decorative; the visible label names the navigation item
        )
    }
}

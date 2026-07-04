package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Navigation", group = "Navigation")
@Composable
public fun NavigationComponentsShowkase(): Unit = NavigationComponentsPreview()

@ShowkaseComposable(name = "Top App Bar", group = "Navigation")
@Composable
public fun TopAppBarShowkase(): Unit =
    AppTheme {
        TopAppBar(title = "Dashboard")
    }

@ShowkaseComposable(name = "Bottom Bar", group = "Navigation")
@Composable
public fun BottomBarShowkase(): Unit =
    AppTheme {
        BottomBar(
            items = showkaseNavigationItems(),
            selectedIndex = 0,
            onItemSelected = {},
        )
    }

@ShowkaseComposable(name = "Tab Row", group = "Navigation")
@Composable
public fun TabRowShowkase(): Unit =
    AppTheme {
        TabRow(
            tabs = listOf("Overview", "Activity", "Settings"),
            selectedIndex = 0,
            onTabSelected = {},
        )
    }

@ShowkaseComposable(name = "Nav Rail", group = "Navigation")
@Composable
public fun NavRailShowkase(): Unit = NavRailPreview()

@ShowkaseComposable(name = "Medium Top App Bar", group = "Navigation")
@Composable
public fun MediumTopAppBarShowkase(): Unit = MediumTopAppBarPreview()

@ShowkaseComposable(name = "Large Top App Bar", group = "Navigation")
@Composable
public fun LargeTopAppBarShowkase(): Unit = LargeTopAppBarPreview()

@ShowkaseComposable(name = "Adaptive Nav Scaffold", group = "Navigation")
@Composable
public fun AdaptiveNavScaffoldShowkase(): Unit = AdaptiveNavScaffoldPreview()

@Composable
private fun showkaseNavigationItems(): List<NavigationItem> =
    listOf(
        NavigationItem(label = "Home", icon = AppTheme.icons.check),
        NavigationItem(label = "Tasks", icon = AppTheme.icons.check, badge = "3"),
        NavigationItem(label = "Close", icon = AppTheme.icons.close),
    )

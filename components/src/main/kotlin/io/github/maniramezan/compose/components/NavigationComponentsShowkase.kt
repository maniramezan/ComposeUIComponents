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

@ShowkaseComposable(name = "Tab Bar", group = "Navigation")
@Composable
public fun TabBarShowkase(): Unit = NavigationComponentsPreview()

@ShowkaseComposable(name = "Tab Bar Centered", group = "Navigation")
@Composable
public fun TabBarCenteredShowkase(): Unit = TabBarCenteredPreview()

@ShowkaseComposable(name = "Tab Bar Disabled Item", group = "Navigation")
@Composable
public fun TabBarDisabledItemShowkase(): Unit = TabBarDisabledItemPreview()

@ShowkaseComposable(name = "Tab Row", group = "Navigation")
@Composable
public fun TabRowShowkase(): Unit =
    AppTheme {
        TabRow(
            tabs = listOf("Overview", "Activity", "Settings"),
            selectedIndex = 0,
            onItemSelected = {},
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

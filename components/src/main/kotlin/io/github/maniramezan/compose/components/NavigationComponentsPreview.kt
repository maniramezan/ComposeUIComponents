package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Navigation Components", group = "Navigation")
@Composable
public fun NavigationComponentsPreview(): Unit =
    AppTheme {
        val items = previewNavigationItems()
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            TopAppBar(title = "Dashboard")
            TabRow(
                tabs = listOf("Overview", "Activity", "Settings"),
                selectedIndex = 0,
                onTabSelected = {},
            )
            BottomBar(
                items = items,
                selectedIndex = 0,
                onItemSelected = {},
            )
        }
    }

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Nav Rail", group = "Navigation")
@Composable
public fun NavRailPreview(): Unit =
    AppTheme {
        NavRail(
            items = previewNavigationItems(),
            selectedIndex = 1,
            onItemSelected = {},
        )
    }

@PreviewLightDark
@Preview(name = "Medium Top App Bar", group = "Navigation")
@Composable
public fun MediumTopAppBarPreview(): Unit =
    AppTheme {
        MediumTopAppBar(title = "Dashboard")
    }

@PreviewLightDark
@Preview(name = "Large Top App Bar", group = "Navigation")
@Composable
public fun LargeTopAppBarPreview(): Unit =
    AppTheme {
        LargeTopAppBar(title = "Dashboard")
    }

@Composable
private fun previewNavigationItems(): List<NavigationItem> =
    listOf(
        NavigationItem(label = "Home", icon = AppTheme.icons.check),
        NavigationItem(label = "Tasks", icon = AppTheme.icons.check, badge = "3"),
        NavigationItem(label = "Close", icon = AppTheme.icons.close),
    )

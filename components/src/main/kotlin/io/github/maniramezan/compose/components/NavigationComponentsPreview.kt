package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
        var selection by remember { mutableIntStateOf(0) }
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            TopAppBar(title = "Dashboard")
            TabRow(
                tabs = listOf("Overview", "Activity", "Settings"),
                selectedIndex = 0,
                onItemSelected = {},
            )
            TabBar(
                items = previewTabBarItems(),
                selection = selection,
                onSelectionChange = { selection = it },
            )
        }
    }

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Tab Bar (Centered)", group = "Navigation")
@Composable
public fun TabBarCenteredPreview(): Unit =
    AppTheme {
        var selection by remember { mutableIntStateOf(1) }
        TabBar(
            items = previewTabBarItems(),
            selection = selection,
            onSelectionChange = { selection = it },
            arrangement = TabBarArrangement.Centered,
        )
    }

@PreviewLightDark
@Preview(name = "Tab Bar (Disabled Item)", group = "Navigation")
@Composable
public fun TabBarDisabledItemPreview(): Unit =
    AppTheme {
        var selection by remember { mutableIntStateOf(0) }
        TabBar(
            items =
                previewTabBarItems().mapIndexed { index, item ->
                    if (index == 2) item.copy(enabled = false) else item
                },
            selection = selection,
            onSelectionChange = { selection = it },
        )
    }

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Nav Rail", group = "Navigation")
@Composable
public fun NavRailPreview(): Unit =
    AppTheme {
        var selection by remember { mutableIntStateOf(1) }
        NavRail(
            items = previewTabBarItems(),
            selection = selection,
            onSelectionChange = { selection = it },
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

@PreviewLightDark
@Preview(name = "Adaptive Nav Scaffold", group = "Navigation")
@Composable
public fun AdaptiveNavScaffoldPreview(): Unit =
    AppTheme {
        var selection by remember { mutableIntStateOf(0) }
        AdaptiveNavScaffold(
            items = previewTabBarItems(),
            selection = selection,
            onSelectionChange = { selection = it },
            topBar = { TopAppBar(title = "Dashboard") },
        ) { innerPadding ->
            Text(
                text = "Content",
                modifier = Modifier.padding(innerPadding),
            )
        }
    }

@Composable
private fun previewTabBarItems(): List<TabBarItemData<Int>> =
    listOf(
        TabBarItemData(
            value = 0,
            icon = { Icon(imageVector = AppTheme.icons.check.imageVector, contentDescription = null) },
            label = { Text(text = "Home") },
        ),
        TabBarItemData(
            value = 1,
            icon = { Icon(imageVector = AppTheme.icons.expand.imageVector, contentDescription = null) },
            label = { Text(text = "Tasks") },
            badge = { Text(text = "3") },
        ),
        TabBarItemData(
            value = 2,
            icon = { Icon(imageVector = AppTheme.icons.close.imageVector, contentDescription = null) },
            label = { Text(text = "Account") },
        ),
    )

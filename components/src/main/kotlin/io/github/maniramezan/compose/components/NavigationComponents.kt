package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.IconToken
import androidx.compose.material3.TopAppBar as MaterialTopAppBar

@Immutable
public data class NavigationItem(
    public val label: String,
    public val icon: IconToken,
    public val contentDescription: String? = label,
    public val badge: String? = null,
)

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
                modifier = Modifier.defaultMinSize(minHeight = 48.dp),
            )
        }
    }
}

@Composable
public fun TabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onTabSelected(index) },
                modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                text = { Text(text = tab) },
            )
        }
    }
}

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
                modifier = Modifier.defaultMinSize(minHeight = 48.dp),
            )
        }
    }
}

@Composable
private fun NavigationItemIcon(item: NavigationItem) {
    if (item.badge != null) {
        BadgedBox(badge = { Badge { Text(text = item.badge) } }) {
            Icon(
                imageVector = item.icon.imageVector,
                contentDescription = item.contentDescription,
            )
        }
    } else {
        Icon(
            imageVector = item.icon.imageVector,
            contentDescription = item.contentDescription,
        )
    }
}

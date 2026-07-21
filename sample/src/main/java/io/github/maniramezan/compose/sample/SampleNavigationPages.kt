package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.IconButton
import io.github.maniramezan.compose.components.NavRail
import io.github.maniramezan.compose.components.SectionHeader
import io.github.maniramezan.compose.components.TabBarItemData
import io.github.maniramezan.compose.components.TabRow
import io.github.maniramezan.compose.components.TopAppBar
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Navigation
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun TopAppBarPage() {
    var showNavIcon by remember { mutableStateOf(false) }
    var showAction by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        TopAppBar(
            title = "Screen Title",
            navigationIcon = {
                if (showNavIcon) {
                    IconButton(icon = AppTheme.icons.close, contentDescription = "Back", onClick = {})
                }
            },
            actions = {
                if (showAction) {
                    IconButton(icon = AppTheme.icons.check, contentDescription = "Save", onClick = {})
                }
            },
        )
        ControlsDivider()
        ControlSwitch(
            label = "Navigation icon",
            checked = showNavIcon,
            onCheckedChange = { showNavIcon = it },
        )
        ControlSwitch(label = "Action icon", checked = showAction, onCheckedChange = { showAction = it })
    }
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

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            NavRail(items = items, selection = index, onSelectionChange = { index = it })
            Column {
                SectionHeader(title = "NavRail")
                Text("Selected: ${labels[index]}")
            }
        }
        ControlsDivider()
        ControlSwitch(label = "Show badge", checked = showBadge, onCheckedChange = { showBadge = it })
    }
}

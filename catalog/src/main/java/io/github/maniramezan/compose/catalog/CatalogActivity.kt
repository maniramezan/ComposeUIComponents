package io.github.maniramezan.compose.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.components.FAB
import io.github.maniramezan.compose.components.Card
import io.github.maniramezan.compose.components.Checkbox
import io.github.maniramezan.compose.components.IconButton
import io.github.maniramezan.compose.components.EmptyState
import io.github.maniramezan.compose.components.ListItem
import io.github.maniramezan.compose.components.BottomBar
import io.github.maniramezan.compose.components.NavigationItem
import io.github.maniramezan.compose.components.PasswordField
import io.github.maniramezan.compose.components.PrimaryButton
import io.github.maniramezan.compose.components.ProgressIndicator
import io.github.maniramezan.compose.components.RadioGroup
import io.github.maniramezan.compose.components.SearchField
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.Section
import io.github.maniramezan.compose.components.SegmentedControl
import io.github.maniramezan.compose.components.Slider
import io.github.maniramezan.compose.components.Snackbar
import io.github.maniramezan.compose.components.Skeleton
import io.github.maniramezan.compose.components.Switch
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.components.TopAppBar
import io.github.maniramezan.compose.components.TabRow
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme

public class CatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(icons = defaultAppIcons()) {
                MaterialTheme {
                    Scaffold { innerPadding ->
                        Surface(modifier = Modifier.padding(innerPadding)) {
                            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                                Text("Compose UI Catalog")
                                PrimaryButton(text = "Primary", onClick = {})
                                SecondaryButton(text = "Secondary", onClick = {})
                                TextButton(text = "Text", onClick = {})
                                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                                    IconButton(
                                        icon = AppTheme.icons.check,
                                        contentDescription = "Confirm",
                                        onClick = {},
                                    )
                                    FAB(
                                        icon = AppTheme.icons.check,
                                        contentDescription = "Create",
                                        onClick = {},
                                    )
                                }
                                SegmentedControl(
                                    options = listOf("Day", "Week", "Month"),
                                    selectedIndex = 1,
                                    onOptionSelected = {},
                                )
                                Text("Inputs")
                                TextField(value = "Mani", onValueChange = {}, label = "Name")
                                PasswordField(value = "secret", onValueChange = {}, label = "Password")
                                SearchField(value = "compose", onValueChange = {})
                                Checkbox(checked = true, onCheckedChange = {}, label = "Email updates")
                                RadioGroup(
                                    options = listOf("Small", "Medium", "Large"),
                                    selectedIndex = 1,
                                    onOptionSelected = {},
                                )
                                Switch(checked = true, onCheckedChange = {}, label = "Notifications")
                                Slider(value = 0.65f, onValueChange = {})
                                Text("Containers")
                                Section(title = "Account") {
                                    Card {
                                        Text("Plan")
                                        Text("Compose Pro")
                                    }
                                }
                                Snackbar(
                                    message = "Catalog saved",
                                    actionLabel = "Undo",
                                    onAction = {},
                                )
                                Text("Lists")
                                ListItem(
                                    headline = "Compose Pro",
                                    supportingText = "Active subscription",
                                    trailingContent = { Text("Active") },
                                )
                                EmptyState(
                                    title = "No projects",
                                    message = "Create your first project to get started.",
                                )
                                Text("Navigation")
                                TopAppBar(title = "Dashboard")
                                TabRow(
                                    tabs = listOf("Overview", "Activity", "Settings"),
                                    selectedIndex = 0,
                                    onTabSelected = {},
                                )
                                BottomBar(
                                    items = listOf(
                                        NavigationItem("Home", AppTheme.icons.check),
                                        NavigationItem("Tasks", AppTheme.icons.check, badge = "3"),
                                        NavigationItem("Close", AppTheme.icons.close),
                                    ),
                                    selectedIndex = 0,
                                    onItemSelected = {},
                                )
                                Text("Feedback")
                                ProgressIndicator(progress = 0.65f, label = "Syncing")
                                Skeleton()
                            }
                        }
                    }
                }
            }
        }
    }
}

package io.github.maniramezan.compose.sample

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
import io.github.maniramezan.compose.components.Card
import io.github.maniramezan.compose.components.Checkbox
import io.github.maniramezan.compose.components.EmptyState
import io.github.maniramezan.compose.components.FAB
import io.github.maniramezan.compose.components.IconButton
import io.github.maniramezan.compose.components.ListItem
import io.github.maniramezan.compose.components.PasswordField
import io.github.maniramezan.compose.components.PrimaryButton
import io.github.maniramezan.compose.components.RadioGroup
import io.github.maniramezan.compose.components.SearchField
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.Section
import io.github.maniramezan.compose.components.SegmentedControl
import io.github.maniramezan.compose.components.Slider
import io.github.maniramezan.compose.components.Snackbar
import io.github.maniramezan.compose.components.Switch
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme

public class SampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(icons = defaultAppIcons()) {
                MaterialTheme {
                    Scaffold { innerPadding ->
                        Surface(modifier = Modifier.padding(innerPadding)) {
                            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                                Text("Compose UI Sample")
                                PrimaryButton(text = "Continue", onClick = {})
                                SecondaryButton(text = "Back", onClick = {})
                                TextButton(text = "Skip", onClick = {})
                                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                                    IconButton(
                                        icon = AppTheme.icons.close,
                                        contentDescription = "Dismiss",
                                        onClick = {},
                                    )
                                    FAB(
                                        icon = AppTheme.icons.check,
                                        contentDescription = "Save",
                                        onClick = {},
                                    )
                                }
                                SegmentedControl(
                                    options = listOf("Free", "Plus", "Pro"),
                                    selectedIndex = 0,
                                    onOptionSelected = {},
                                )
                                Text("Profile")
                                TextField(value = "Mani", onValueChange = {}, label = "Name")
                                PasswordField(value = "secret", onValueChange = {}, label = "Password")
                                SearchField(value = "settings", onValueChange = {})
                                Checkbox(checked = true, onCheckedChange = {}, label = "Email updates")
                                RadioGroup(
                                    options = listOf("Compact", "Comfortable", "Spacious"),
                                    selectedIndex = 1,
                                    onOptionSelected = {},
                                )
                                Switch(checked = true, onCheckedChange = {}, label = "Notifications")
                                Slider(value = 0.45f, onValueChange = {})
                                Section(title = "Subscription") {
                                    Card {
                                        Text("Current plan")
                                        Text("Free")
                                    }
                                }
                                Snackbar(message = "Profile saved")
                                ListItem(
                                    headline = "Workspace",
                                    supportingText = "Personal",
                                    trailingContent = { Text("Open") },
                                )
                                EmptyState(
                                    title = "No recent files",
                                    message = "Recent projects will appear here.",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

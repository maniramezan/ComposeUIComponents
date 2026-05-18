package io.github.maniramezan.compose.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.components.AppText
import io.github.maniramezan.compose.components.AppTextStyle
import io.github.maniramezan.compose.components.BottomBar
import io.github.maniramezan.compose.components.Card
import io.github.maniramezan.compose.components.Checkbox
import io.github.maniramezan.compose.components.EmptyState
import io.github.maniramezan.compose.components.FAB
import io.github.maniramezan.compose.components.IconButton
import io.github.maniramezan.compose.components.ListItem
import io.github.maniramezan.compose.components.NavigationItem
import io.github.maniramezan.compose.components.PasswordField
import io.github.maniramezan.compose.components.PrimaryButton
import io.github.maniramezan.compose.components.ProgressIndicator
import io.github.maniramezan.compose.components.RadioGroup
import io.github.maniramezan.compose.components.SearchField
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.Section
import io.github.maniramezan.compose.components.SegmentedControl
import io.github.maniramezan.compose.components.Skeleton
import io.github.maniramezan.compose.components.Slider
import io.github.maniramezan.compose.components.Snackbar
import io.github.maniramezan.compose.components.Switch
import io.github.maniramezan.compose.components.TabRow
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.components.TopAppBar
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme

public class SampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
                icons = defaultAppIcons(),
                dynamicColor = true,
            ) {
                Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { innerPadding ->
                    Surface(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .consumeWindowInsets(innerPadding)
                                .imePadding()
                                .verticalScroll(rememberScrollState()),
                    ) {
                        Column(
                            modifier = Modifier.padding(AppTheme.spacing.lg),
                            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
                        ) {
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
                            TopAppBar(title = "Profile")
                            TabRow(
                                tabs = listOf("General", "Billing", "Security"),
                                selectedIndex = 0,
                                onTabSelected = {},
                            )
                            BottomBar(
                                items =
                                    listOf(
                                        NavigationItem("Home", AppTheme.icons.check),
                                        NavigationItem("Settings", AppTheme.icons.check),
                                        NavigationItem("Close", AppTheme.icons.close),
                                    ),
                                selectedIndex = 1,
                                onItemSelected = {},
                            )
                            ProgressIndicator(progress = 0.45f, label = "Storage used")
                            Skeleton()
                            AppText(text = "Account summary", style = AppTextStyle.Title)
                            AppText(text = "Your workspace is ready.")
                        }
                    }
                }
            }
        }
    }
}

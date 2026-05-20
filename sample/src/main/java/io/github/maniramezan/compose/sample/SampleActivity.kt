package io.github.maniramezan.compose.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.AppText
import io.github.maniramezan.compose.components.AppTextStyle
import io.github.maniramezan.compose.components.BottomBar
import io.github.maniramezan.compose.components.Card
import io.github.maniramezan.compose.components.Checkbox
import io.github.maniramezan.compose.components.ContentRow
import io.github.maniramezan.compose.components.EmptyState
import io.github.maniramezan.compose.components.ErrorState
import io.github.maniramezan.compose.components.FAB
import io.github.maniramezan.compose.components.IconButton
import io.github.maniramezan.compose.components.LevelBadge
import io.github.maniramezan.compose.components.ListItem
import io.github.maniramezan.compose.components.LoadingState
import io.github.maniramezan.compose.components.NavigationItem
import io.github.maniramezan.compose.components.OverlayCard
import io.github.maniramezan.compose.components.PaginatedContent
import io.github.maniramezan.compose.components.PaginationPage
import io.github.maniramezan.compose.components.PasswordField
import io.github.maniramezan.compose.components.PillChip
import io.github.maniramezan.compose.components.PrimaryButton
import io.github.maniramezan.compose.components.ProgressIndicator
import io.github.maniramezan.compose.components.RadioGroup
import io.github.maniramezan.compose.components.SearchField
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.SectionHeader
import io.github.maniramezan.compose.components.SegmentedControl
import io.github.maniramezan.compose.components.Skeleton
import io.github.maniramezan.compose.components.SkeletonBlock
import io.github.maniramezan.compose.components.Slider
import io.github.maniramezan.compose.components.Snackbar
import io.github.maniramezan.compose.components.Switch
import io.github.maniramezan.compose.components.TabRow
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.components.Toast
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
                            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
                        ) {
                            AppText(text = "Compose UI Sample", style = AppTextStyle.Display)

                            ButtonsSection()
                            SectionDivider()
                            InputFieldsSection()
                            SectionDivider()
                            ControlsSection()
                            SectionDivider()
                            NavigationSection()
                            SectionDivider()
                            PaginatedContentSection()
                            SectionDivider()
                            ListsSection()
                            SectionDivider()
                            ContainersSection()
                            SectionDivider()
                            FeedbackSection()
                            SectionDivider()
                            TypographySection()
                            SectionDivider()
                            ChipsAndBadgesSection()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = AppTheme.spacing.x1))
}

// region Buttons

@Composable
private fun ButtonsSection() {
    SectionHeader(title = "Buttons")
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        PrimaryButton(text = "Primary Button", onClick = {})
        PrimaryButton(text = "Primary (disabled)", onClick = {}, enabled = false)
        SecondaryButton(text = "Secondary Button", onClick = {})
        SecondaryButton(text = "Secondary (disabled)", onClick = {}, enabled = false)
        TextButton(text = "Text Button", onClick = {})
        TextButton(text = "Text (disabled)", onClick = {}, enabled = false)
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

        var segmentIndex by remember { mutableIntStateOf(0) }
        Text("Segmented Control")
        SegmentedControl(
            options = listOf("Free", "Plus", "Pro"),
            selectedIndex = segmentIndex,
            onOptionSelected = { segmentIndex = it },
        )
        SegmentedControl(
            options = listOf("A", "B"),
            selectedIndex = 0,
            onOptionSelected = {},
            enabled = false,
        )
    }
}

// endregion

// region Input Fields

@Composable
private fun InputFieldsSection() {
    SectionHeader(title = "Input Fields")
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        // Normal text field
        var name by remember { mutableStateOf("Mani") }
        TextField(value = name, onValueChange = { name = it }, label = "Name")

        // Error state
        var errorField by remember { mutableStateOf("") }
        TextField(
            value = errorField,
            onValueChange = { errorField = it },
            label = "Required Field",
            isError = errorField.isEmpty(),
            supportingText = if (errorField.isEmpty()) "This field is required" else null,
        )

        // Disabled
        TextField(value = "Read-only", onValueChange = {}, label = "Disabled", enabled = false)

        // Password field
        var password by remember { mutableStateOf("secret123") }
        PasswordField(value = password, onValueChange = { password = it }, label = "Password")
        PasswordField(
            value = "revealed",
            onValueChange = {},
            label = "Password (revealed)",
            revealPassword = true,
        )

        // Search field
        var searchQuery by remember { mutableStateOf("") }
        SearchField(value = searchQuery, onValueChange = { searchQuery = it })
        SearchField(value = "", onValueChange = {}, enabled = false, placeholder = "Disabled search")
    }
}

// endregion

// region Controls

@Composable
private fun ControlsSection() {
    SectionHeader(title = "Controls")
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        // Checkbox
        var checked1 by remember { mutableStateOf(true) }
        var checked2 by remember { mutableStateOf(false) }
        Checkbox(checked = checked1, onCheckedChange = { checked1 = it }, label = "Email updates")
        Checkbox(checked = checked2, onCheckedChange = { checked2 = it }, label = "Push notifications")
        Checkbox(checked = true, onCheckedChange = {}, label = "Disabled (checked)", enabled = false)

        // Radio group
        var radioIndex by remember { mutableIntStateOf(1) }
        Text("Layout density")
        RadioGroup(
            options = listOf("Compact", "Comfortable", "Spacious"),
            selectedIndex = radioIndex,
            onOptionSelected = { radioIndex = it },
        )
        Text("Disabled radio group")
        RadioGroup(
            options = listOf("On", "Off"),
            selectedIndex = 0,
            onOptionSelected = {},
            enabled = false,
        )

        // Switch
        var switch1 by remember { mutableStateOf(true) }
        var switch2 by remember { mutableStateOf(false) }
        Switch(checked = switch1, onCheckedChange = { switch1 = it }, label = "Notifications")
        Switch(checked = switch2, onCheckedChange = { switch2 = it }, label = "Dark mode")
        Switch(checked = false, onCheckedChange = {}, label = "Disabled switch", enabled = false)

        // Slider
        var sliderValue by remember { mutableFloatStateOf(0.45f) }
        Text("Volume: ${(sliderValue * 100).toInt()}%")
        Slider(value = sliderValue, onValueChange = { sliderValue = it })
        Slider(value = 0.7f, onValueChange = {}, enabled = false)
    }
}

// endregion

// region Navigation

@Composable
private fun NavigationSection() {
    SectionHeader(title = "Navigation")
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        TopAppBar(title = "Profile")

        var tabIndex by remember { mutableIntStateOf(0) }
        TabRow(
            tabs = listOf("General", "Billing", "Security"),
            selectedIndex = tabIndex,
            onTabSelected = { tabIndex = it },
        )

        var bottomBarIndex by remember { mutableIntStateOf(0) }
        BottomBar(
            items =
                listOf(
                    NavigationItem("Home", AppTheme.icons.check),
                    NavigationItem("Tasks", AppTheme.icons.check, badge = "5"),
                    NavigationItem("Close", AppTheme.icons.close),
                ),
            selectedIndex = bottomBarIndex,
            onItemSelected = { bottomBarIndex = it },
        )
    }
}

// endregion

// region Paginated Content

@Composable
private fun PaginatedContentSection() {
    SectionHeader(title = "Paginated Content")
    PaginatedContent(
        pages =
            listOf(
                PaginationPage(title = "Popular"),
                PaginationPage(title = "New"),
                PaginationPage(title = "Top Rated"),
            ),
    ) { _, page ->
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(AppTheme.spacing.x2),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "${page.title} apps go here")
        }
    }

    // Without indicator
    PaginatedContent(
        pages =
            listOf(
                PaginationPage(title = "Tab A"),
                PaginationPage(title = "Tab B"),
            ),
        showPageIndicator = false,
    ) { _, page ->
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(80.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = page.title)
        }
    }
}

// endregion

// region Lists

@Composable
private fun ListsSection() {
    SectionHeader(title = "Lists")
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        ListItem(
            headline = "Workspace",
            supportingText = "Personal",
            trailingContent = { Text("Open") },
        )
        ListItem(headline = "Settings", supportingText = "App preferences")
        ListItem(headline = "Simple item")

        ContentRow(
            title = "ephemeral",
            secondaryText = "/əˈfemərəl/",
            supportingText = "Lasting for a very short time.",
            onClick = {},
            trailingContent = {
                LevelBadge(label = "C1", tier = AppTheme.colors.levels.tier(2))
            },
        )
        ContentRow(title = "Read-only row", supportingText = "No click handler")

        EmptyState(
            title = "No recent files",
            message = "Recent projects will appear here.",
        )
        LoadingState(label = "Loading projects…")
        ErrorState(
            title = "Could not load",
            message = "Check your connection and retry.",
            action = { TextButton(text = "Retry", onClick = {}) },
        )
    }
}

// endregion

// region Containers

@Composable
private fun ContainersSection() {
    SectionHeader(title = "Containers")
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Card {
            Text("Card content")
            Text("With multiple lines of information")
        }

        OverlayCard {
            Text("Overlay card — sits on media")
        }

        Snackbar(message = "Profile saved")
        Snackbar(message = "Item deleted", actionLabel = "Undo", onAction = {})
        Toast(message = "Toast message")
        Toast(message = "Toast with action", actionLabel = "View", onAction = {})
    }
}

// endregion

// region Feedback

@Composable
private fun FeedbackSection() {
    SectionHeader(title = "Feedback")
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        ProgressIndicator(progress = 0.45f, label = "Storage: 45%")
        ProgressIndicator(progress = 1.0f, label = "Complete")
        ProgressIndicator(label = "Indeterminate")
        Skeleton()
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            SkeletonBlock(height = 40.dp, width = 80.dp)
            SkeletonBlock(height = 40.dp, width = 120.dp)
        }
    }
}

// endregion

// region Typography

@Composable
private fun TypographySection() {
    SectionHeader(title = "Typography")
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
        AppText(text = "Display style", style = AppTextStyle.Display)
        AppText(text = "Title style", style = AppTextStyle.Title)
        AppText(text = "Body style", style = AppTextStyle.Body)
        AppText(text = "Label style", style = AppTextStyle.Label)
    }
}

// endregion

// region Chips & Badges

@Composable
private fun ChipsAndBadgesSection() {
    SectionHeader(title = "Chips & Badges")
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        var selectedChip by remember { mutableIntStateOf(0) }
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            listOf("All", "Beginner", "Advanced").forEachIndexed { index, label ->
                PillChip(
                    label = label,
                    isSelected = index == selectedChip,
                    onClick = { selectedChip = index },
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            LevelBadge(label = "A1", tier = AppTheme.colors.levels.tier(0))
            LevelBadge(label = "B2", tier = AppTheme.colors.levels.tier(1))
            LevelBadge(label = "C1", tier = AppTheme.colors.levels.tier(2))
        }
    }
}

// endregion

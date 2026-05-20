package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import io.github.maniramezan.compose.components.NavRail
import io.github.maniramezan.compose.components.NavigationItem
import io.github.maniramezan.compose.components.OverlayCard
import io.github.maniramezan.compose.components.PageDirection
import io.github.maniramezan.compose.components.PageFooterStyle
import io.github.maniramezan.compose.components.PageTitleAlignment
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
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Actions
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun PrimaryButtonPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Variants")
        PrimaryButton(text = "Primary Button", onClick = {})
        PrimaryButton(text = "Primary (disabled)", onClick = {}, enabled = false)
    }
}

@Composable
internal fun SecondaryButtonPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Variants")
        SecondaryButton(text = "Secondary Button", onClick = {})
        SecondaryButton(text = "Secondary (disabled)", onClick = {}, enabled = false)
    }
}

@Composable
internal fun TextButtonPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Variants")
        TextButton(text = "Text Button", onClick = {})
        TextButton(text = "Text (disabled)", onClick = {}, enabled = false)
    }
}

@Composable
internal fun IconButtonPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Variants")
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            IconButton(icon = AppTheme.icons.check, contentDescription = "Confirm", onClick = {})
            IconButton(icon = AppTheme.icons.close, contentDescription = "Dismiss", onClick = {})
            IconButton(
                icon = AppTheme.icons.check,
                contentDescription = "Disabled",
                onClick = {},
                enabled = false,
            )
        }
    }
}

@Composable
internal fun FabPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Variants")
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            FAB(icon = AppTheme.icons.check, contentDescription = "Save", onClick = {})
            FAB(icon = AppTheme.icons.close, contentDescription = "Cancel", onClick = {})
        }
    }
}

@Composable
internal fun SegmentedControlPage() {
    var selected by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Interactive")
        SegmentedControl(
            options = listOf("Free", "Plus", "Pro"),
            selectedIndex = selected,
            onOptionSelected = { selected = it },
        )
        SectionHeader(title = "Disabled")
        SegmentedControl(
            options = listOf("A", "B", "C"),
            selectedIndex = 0,
            onOptionSelected = {},
            enabled = false,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Inputs
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun TextFieldPage() {
    var name by remember { mutableStateOf("Mani") }
    var required by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Normal")
        TextField(value = name, onValueChange = { name = it }, label = "Name")
        SectionHeader(title = "Error state")
        TextField(
            value = required,
            onValueChange = { required = it },
            label = "Required Field",
            isError = required.isEmpty(),
            supportingText = if (required.isEmpty()) "This field is required" else null,
        )
        SectionHeader(title = "Disabled")
        TextField(value = "Read-only", onValueChange = {}, label = "Disabled", enabled = false)
    }
}

@Composable
internal fun PasswordFieldPage() {
    var password by remember { mutableStateOf("secret123") }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Hidden")
        PasswordField(value = password, onValueChange = { password = it }, label = "Password")
        SectionHeader(title = "Revealed")
        PasswordField(
            value = "revealed",
            onValueChange = {},
            label = "Password (revealed)",
            revealPassword = true,
        )
    }
}

@Composable
internal fun SearchFieldPage() {
    var query by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Interactive")
        SearchField(value = query, onValueChange = { query = it })
        SectionHeader(title = "Disabled")
        SearchField(
            value = "",
            onValueChange = {},
            enabled = false,
            placeholder = "Disabled search",
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Controls
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun CheckboxPage() {
    var a by remember { mutableStateOf(true) }
    var b by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Interactive")
        Checkbox(checked = a, onCheckedChange = { a = it }, label = "Email updates")
        Checkbox(checked = b, onCheckedChange = { b = it }, label = "Push notifications")
        SectionHeader(title = "Disabled")
        Checkbox(checked = true, onCheckedChange = {}, label = "Disabled (checked)", enabled = false)
        Checkbox(checked = false, onCheckedChange = {}, label = "Disabled (unchecked)", enabled = false)
    }
}

@Composable
internal fun RadioGroupPage() {
    var index by remember { mutableIntStateOf(1) }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Interactive")
        RadioGroup(
            options = listOf("Compact", "Comfortable", "Spacious"),
            selectedIndex = index,
            onOptionSelected = { index = it },
        )
        SectionHeader(title = "Disabled")
        RadioGroup(
            options = listOf("On", "Off"),
            selectedIndex = 0,
            onOptionSelected = {},
            enabled = false,
        )
    }
}

@Composable
internal fun SwitchPage() {
    var a by remember { mutableStateOf(true) }
    var b by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Interactive")
        Switch(checked = a, onCheckedChange = { a = it }, label = "Notifications")
        Switch(checked = b, onCheckedChange = { b = it }, label = "Dark mode")
        SectionHeader(title = "Disabled")
        Switch(checked = false, onCheckedChange = {}, label = "Disabled switch", enabled = false)
    }
}

@Composable
internal fun SliderPage() {
    var value by remember { mutableFloatStateOf(0.45f) }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Interactive")
        Text("Volume: ${(value * 100).toInt()}%")
        Slider(value = value, onValueChange = { value = it })
        SectionHeader(title = "Disabled")
        Slider(value = 0.7f, onValueChange = {}, enabled = false)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Navigation
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun TopAppBarPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Title only")
        TopAppBar(title = "Profile")
        SectionHeader(title = "With nav icon")
        TopAppBar(
            title = "Settings",
            navigationIcon = {
                IconButton(
                    icon = AppTheme.icons.close,
                    contentDescription = "Back",
                    onClick = {},
                )
            },
        )
        SectionHeader(title = "With actions")
        TopAppBar(
            title = "Dashboard",
            actions = {
                IconButton(
                    icon = AppTheme.icons.check,
                    contentDescription = "Save",
                    onClick = {},
                )
            },
        )
    }
}

@Composable
internal fun TabRowPage() {
    var index by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Interactive")
        TabRow(
            tabs = listOf("General", "Billing", "Security"),
            selectedIndex = index,
            onTabSelected = { index = it },
        )
        Text("Selected: ${listOf("General", "Billing", "Security")[index]}")
    }
}

@Composable
internal fun BottomBarPage() {
    var index by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Interactive")
        BottomBar(
            items = listOf(
                NavigationItem("Home", AppTheme.icons.check),
                NavigationItem("Tasks", AppTheme.icons.check, badge = "5"),
                NavigationItem("Close", AppTheme.icons.close),
            ),
            selectedIndex = index,
            onItemSelected = { index = it },
        )
        Text("Selected: ${listOf("Home", "Tasks", "Close")[index]}")
    }
}

@Composable
internal fun NavRailPage() {
    var index by remember { mutableIntStateOf(0) }
    Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        NavRail(
            items = listOf(
                NavigationItem("Home", AppTheme.icons.check),
                NavigationItem("Tasks", AppTheme.icons.check, badge = "3"),
                NavigationItem("Close", AppTheme.icons.close),
            ),
            selectedIndex = index,
            onItemSelected = { index = it },
        )
        Column {
            SectionHeader(title = "NavRail")
            Text("Selected: ${listOf("Home", "Tasks", "Close")[index]}")
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Pagination
// ─────────────────────────────────────────────────────────────────────────────

private fun demoPages() = listOf(
    PaginationPage(title = "Popular"),
    PaginationPage(title = "New Releases"),
    PaginationPage(title = "Top Rated"),
)

@Composable
private fun PagerPlaceholder(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(AppTheme.spacing.x2),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "$title content")
    }
}

@Composable
internal fun PaginatedContentPage() {
    var lastPage by remember { mutableStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2)) {
        SectionHeader(title = "Leading · Bidirectional")
        PaginatedContent(
            pages = demoPages(),
            titleAlignment = PageTitleAlignment.Leading,
            direction = PageDirection.Bidirectional,
        ) { _, page -> PagerPlaceholder(page.title) }

        SectionHeader(title = "Center · Unidirectional")
        PaginatedContent(
            pages = demoPages(),
            titleAlignment = PageTitleAlignment.Center,
            direction = PageDirection.Unidirectional,
        ) { _, page -> PagerPlaceholder(page.title) }

        SectionHeader(title = "Progress footer")
        PaginatedContent(
            pages = demoPages(),
            footerStyle = PageFooterStyle.Progress,
        ) { _, page -> PagerPlaceholder(page.title) }

        SectionHeader(title = "No footer")
        PaginatedContent(
            pages = demoPages(),
            footerStyle = PageFooterStyle.None,
        ) { _, page -> PagerPlaceholder(page.title) }

        SectionHeader(title = "onPageChanged callback")
        Text("Last page index: $lastPage")
        PaginatedContent(
            pages = demoPages(),
            onPageChanged = { lastPage = it },
        ) { _, page -> PagerPlaceholder(page.title) }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Lists
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun ListItemPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Variants")
        ListItem(
            headline = "Workspace",
            supportingText = "Personal",
            trailingContent = { Text("Open") },
        )
        ListItem(headline = "Settings", supportingText = "App preferences")
        ListItem(headline = "Simple item")
    }
}

@Composable
internal fun ContentRowPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Tappable with badge")
        ContentRow(
            title = "ephemeral",
            secondaryText = "/əˈfemərəl/",
            supportingText = "Lasting for a very short time.",
            onClick = {},
            trailingContent = {
                LevelBadge(label = "C1", tier = AppTheme.colors.levels.tier(2))
            },
        )
        SectionHeader(title = "Read-only")
        ContentRow(title = "Read-only row", supportingText = "No click handler")
    }
}

@Composable
internal fun EmptyStatePage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Without action")
        EmptyState(
            title = "No projects",
            message = "Create your first project to get started.",
        )
        SectionHeader(title = "With action")
        EmptyState(
            title = "No recent files",
            message = "Recent projects will appear here.",
            action = { TextButton(text = "Browse all", onClick = {}) },
        )
    }
}

@Composable
internal fun LoadingStatePage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "With label")
        LoadingState(label = "Loading projects…")
        SectionHeader(title = "Without label")
        LoadingState()
    }
}

@Composable
internal fun ErrorStatePage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Without action")
        ErrorState(
            title = "Could not load",
            message = "Check your connection and retry.",
        )
        SectionHeader(title = "With action")
        ErrorState(
            title = "Something went wrong",
            message = "An unexpected error occurred.",
            action = { TextButton(text = "Retry", onClick = {}) },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Containers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun CardPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Single line")
        Card { Text("Card content") }
        SectionHeader(title = "Multi-line")
        Card {
            Text("Plan")
            Text("Compose Pro")
            Text("Active since January 2024")
        }
    }
}

@Composable
internal fun OverlayCardPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Default")
        OverlayCard { Text("Overlay card — floats over media") }
        SectionHeader(title = "Multi-line")
        OverlayCard {
            Text("Title text")
            Text("Supporting description below the title")
        }
    }
}

@Composable
internal fun SnackbarPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Message only")
        Snackbar(message = "Profile saved")
        SectionHeader(title = "With action")
        Snackbar(message = "Item deleted", actionLabel = "Undo", onAction = {})
    }
}

@Composable
internal fun ToastPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Message only")
        Toast(message = "Toast message")
        SectionHeader(title = "With action")
        Toast(message = "Toast with action", actionLabel = "View", onAction = {})
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Feedback
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun ProgressIndicatorPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Determinate")
        ProgressIndicator(progress = 0.45f, label = "Storage: 45%")
        ProgressIndicator(progress = 1.0f, label = "Complete")
        SectionHeader(title = "Indeterminate")
        ProgressIndicator(label = "Syncing…")
        SectionHeader(title = "No label")
        ProgressIndicator(progress = 0.65f)
    }
}

@Composable
internal fun SkeletonPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Full-width")
        Skeleton()
        Skeleton()
        Skeleton()
    }
}

@Composable
internal fun SkeletonBlockPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Various sizes")
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            SkeletonBlock(height = 40.dp, width = 80.dp)
            SkeletonBlock(height = 40.dp, width = 120.dp)
            SkeletonBlock(height = 40.dp, width = 60.dp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            SkeletonBlock(height = 80.dp, width = 80.dp)
            SkeletonBlock(height = 80.dp, width = 80.dp)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Typography
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun AppTextPage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
        SectionHeader(title = "All styles")
        AppText(text = "Display", style = AppTextStyle.Display)
        AppText(text = "Title", style = AppTextStyle.Title)
        AppText(text = "Body — the quick brown fox jumps over the lazy dog.", style = AppTextStyle.Body)
        AppText(text = "Label", style = AppTextStyle.Label)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Chips & Badges
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun PillChipPage() {
    var selected by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Selectable row")
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            listOf("All", "Beginner", "Intermediate", "Advanced").forEachIndexed { i, label ->
                PillChip(
                    label = label,
                    isSelected = i == selected,
                    onClick = { selected = i },
                )
            }
        }
    }
}

@Composable
internal fun LevelBadgePage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SectionHeader(title = "Tier colours")
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            LevelBadge(label = "A1", tier = AppTheme.colors.levels.tier(0))
            LevelBadge(label = "A2", tier = AppTheme.colors.levels.tier(0))
            LevelBadge(label = "B1", tier = AppTheme.colors.levels.tier(1))
            LevelBadge(label = "B2", tier = AppTheme.colors.levels.tier(1))
            LevelBadge(label = "C1", tier = AppTheme.colors.levels.tier(2))
            LevelBadge(label = "C2", tier = AppTheme.colors.levels.tier(2))
        }
    }
}

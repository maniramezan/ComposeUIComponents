package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
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
import io.github.maniramezan.compose.components.FlipAxis
import io.github.maniramezan.compose.components.FlipCard
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
import io.github.maniramezan.compose.components.SegmentDensity
import io.github.maniramezan.compose.components.SegmentFitMode
import io.github.maniramezan.compose.components.SegmentSelectionIndicator
import io.github.maniramezan.compose.components.SegmentedContent
import io.github.maniramezan.compose.components.SegmentedControl
import io.github.maniramezan.compose.components.SegmentedItem
import io.github.maniramezan.compose.components.SelectionListContent
import io.github.maniramezan.compose.components.SelectionListNode
import io.github.maniramezan.compose.components.Skeleton
import io.github.maniramezan.compose.components.SkeletonBlock
import io.github.maniramezan.compose.components.Slider
import io.github.maniramezan.compose.components.Snackbar
import io.github.maniramezan.compose.components.Switch
import io.github.maniramezan.compose.components.TabRow
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.components.Toast
import io.github.maniramezan.compose.components.ToastDuration
import io.github.maniramezan.compose.components.ToastHost
import io.github.maniramezan.compose.components.ToastPosition
import io.github.maniramezan.compose.components.TopAppBar
import io.github.maniramezan.compose.components.rememberToastHostState
import io.github.maniramezan.compose.theme.AppTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// ─────────────────────────────────────────────────────────────────────────────
// Shared helpers
// ─────────────────────────────────────────────────────────────────────────────

/** Horizontal divider between the live preview and the controls panel. */
@Composable
private fun ControlsDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = AppTheme.spacing.sm))
    AppText(text = "Controls", style = AppTextStyle.Label)
}

/** A labelled toggle row used throughout the controls panels. */
@Composable
private fun ControlSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Switch(checked = checked, onCheckedChange = onCheckedChange, label = label)
}

/** A labelled segmented-control row used throughout the controls panels. */
@Composable
private fun ControlSegmented(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs)) {
        Text(text = label)
        SegmentedControl(
            options = options,
            selectedIndex = selectedIndex,
            onOptionSelected = onOptionSelected,
        )
    }
}

/** A labelled slider row used throughout the controls panels. */
@Composable
private fun ControlSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs)) {
        Text(text = label)
        Slider(value = value, onValueChange = onValueChange, valueRange = valueRange)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Actions
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun PrimaryButtonPage() {
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        PrimaryButton(text = "Primary Button", onClick = {}, enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
    }
}

@Composable
internal fun SecondaryButtonPage() {
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SecondaryButton(text = "Secondary Button", onClick = {}, enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
    }
}

@Composable
internal fun TextButtonPage() {
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        TextButton(text = "Text Button", onClick = {}, enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
    }
}

@Composable
internal fun IconButtonPage() {
    var enabled by remember { mutableStateOf(true) }
    val iconOptions = listOf("Check", "Close")
    var iconIndex by remember { mutableIntStateOf(0) }
    val icon = if (iconIndex == 0) AppTheme.icons.check else AppTheme.icons.close

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        IconButton(icon = icon, contentDescription = iconOptions[iconIndex], onClick = {}, enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        ControlSegmented(
            label = "Icon",
            options = iconOptions,
            selectedIndex = iconIndex,
            onOptionSelected = { iconIndex = it },
        )
    }
}

@Composable
internal fun FabPage() {
    val iconOptions = listOf("Check", "Close")
    var iconIndex by remember { mutableIntStateOf(0) }
    val icon = if (iconIndex == 0) AppTheme.icons.check else AppTheme.icons.close

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        FAB(icon = icon, contentDescription = iconOptions[iconIndex], onClick = {})
        ControlsDivider()
        ControlSegmented(
            label = "Icon",
            options = iconOptions,
            selectedIndex = iconIndex,
            onOptionSelected = { iconIndex = it },
        )
    }
}

@Composable
internal fun SegmentedControlPage() {
    var selected by remember { mutableIntStateOf(0) }
    var enabled by remember { mutableStateOf(true) }
    val options = listOf("Free", "Plus", "Pro")

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SegmentedControl(
            options = options,
            selectedIndex = selected,
            onOptionSelected = { selected = it },
            enabled = enabled,
        )
        Text(text = "Selected: ${options[selected]}")
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Inputs
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun TextFieldPage() {
    var value by remember { mutableStateOf("Mani") }
    var enabled by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var showSupporting by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        TextField(
            value = value,
            onValueChange = { value = it },
            label = "Name",
            enabled = enabled,
            isError = isError,
            supportingText = if (showSupporting) "Supporting text" else null,
        )
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        ControlSwitch(label = "Error state", checked = isError, onCheckedChange = { isError = it })
        ControlSwitch(
            label = "Supporting text",
            checked = showSupporting,
            onCheckedChange = { showSupporting = it },
        )
    }
}

@Composable
internal fun PasswordFieldPage() {
    var value by remember { mutableStateOf("secret123") }
    var revealed by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        PasswordField(
            value = value,
            onValueChange = { value = it },
            label = "Password",
            enabled = enabled,
            revealPassword = revealed,
        )
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        ControlSwitch(label = "Reveal password", checked = revealed, onCheckedChange = { revealed = it })
    }
}

@Composable
internal fun SearchFieldPage() {
    var query by remember { mutableStateOf("") }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SearchField(value = query, onValueChange = { query = it }, enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Controls
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun CheckboxPage() {
    var checked by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Checkbox(checked = checked, onCheckedChange = { checked = it }, label = "Email updates", enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        ControlSwitch(label = "Checked", checked = checked, onCheckedChange = { checked = it })
    }
}

@Composable
internal fun RadioGroupPage() {
    val options = listOf("Compact", "Comfortable", "Spacious")
    var selectedIndex by remember { mutableIntStateOf(1) }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        RadioGroup(
            options = options,
            selectedIndex = selectedIndex,
            onOptionSelected = { selectedIndex = it },
            enabled = enabled,
        )
        Text(text = "Selected: ${options[selectedIndex]}")
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
    }
}

@Composable
internal fun SwitchPage() {
    var checked by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Switch(checked = checked, onCheckedChange = { checked = it }, label = "Notifications", enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        ControlSwitch(label = "Checked", checked = checked, onCheckedChange = { checked = it })
    }
}

@Composable
internal fun SliderPage() {
    var value by remember { mutableFloatStateOf(0.45f) }
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Text("Volume: ${(value * 100).toInt()}%")
        Slider(value = value, onValueChange = { value = it }, enabled = enabled)
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Selection
// ─────────────────────────────────────────────────────────────────────────────

private val sampleSelectionListNodes: List<SelectionListNode<String>> =
    listOf(
        SelectionListNode(
            id = "fruit",
            title = "Fruit",
            leadingGlyph = "🍎",
            children =
                listOf(
                    SelectionListNode(id = "apple", title = "Apple", subtitle = "Red or green"),
                    SelectionListNode(id = "banana", title = "Banana", subtitle = "Tropical"),
                    SelectionListNode(id = "mango", title = "Mango", subtitle = "Stone fruit"),
                    SelectionListNode(id = "orange", title = "Orange", subtitle = "Citrus"),
                    SelectionListNode(id = "grape", title = "Grape", subtitle = "Vine fruit"),
                ),
        ),
        SelectionListNode(
            id = "vegetable",
            title = "Vegetable",
            leadingGlyph = "🥦",
            children =
                listOf(
                    SelectionListNode(id = "carrot", title = "Carrot"),
                    SelectionListNode(id = "spinach", title = "Spinach"),
                    SelectionListNode(id = "broccoli", title = "Broccoli"),
                    SelectionListNode(id = "cucumber", title = "Cucumber"),
                    SelectionListNode(id = "tomato", title = "Tomato"),
                ),
        ),
        SelectionListNode(
            id = "grain",
            title = "Grain",
            leadingGlyph = "🌾",
            children =
                listOf(
                    SelectionListNode(id = "rice", title = "Rice"),
                    SelectionListNode(id = "pasta", title = "Pasta"),
                    SelectionListNode(id = "bread", title = "Bread"),
                    SelectionListNode(id = "oats", title = "Oats"),
                ),
        ),
        SelectionListNode(
            id = "dairy",
            title = "Dairy",
            leadingGlyph = "🧀",
            children =
                listOf(
                    SelectionListNode(id = "milk", title = "Milk"),
                    SelectionListNode(id = "cheese", title = "Cheese"),
                    SelectionListNode(id = "yogurt", title = "Yogurt"),
                ),
        ),
        SelectionListNode(id = "water", title = "Water", leadingGlyph = "💧", subtitle = "Zero calories"),
        SelectionListNode(id = "juice", title = "Juice", leadingGlyph = "🍹"),
    )

@Composable
internal fun SelectionListPage() {
    val modeOptions = listOf("Single", "Multiple")
    var modeIndex by remember { mutableIntStateOf(0) }
    var searchable by remember { mutableStateOf(true) }
    var category by remember { mutableStateOf("banana") }
    var tags by remember { mutableStateOf(setOf("apple", "spinach", "rice")) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        if (modeIndex == 0) {
            SelectionListContent(
                title = "Category",
                nodes = sampleSelectionListNodes,
                selectedIds = setOf(category),
                onSelect = { category = it },
                isSearchable = searchable,
                searchPlaceholder = "Search",
                noResultsText = "No results",
                modifier = Modifier.height(450.dp),
            )
        } else {
            SelectionListContent(
                title = "Categories",
                nodes = sampleSelectionListNodes,
                selectedIds = tags,
                onSelect = { id -> tags = if (id in tags) tags - id else tags + id },
                isSearchable = searchable,
                searchPlaceholder = "Search",
                noResultsText = "No results",
                confirmButton = { TextButton(text = "${tags.size} selected", onClick = {}) },
                modifier = Modifier.height(450.dp),
            )
        }
        ControlsDivider()
        ControlSegmented(
            label = "Mode",
            options = modeOptions,
            selectedIndex = modeIndex,
            onOptionSelected = { modeIndex = it },
        )
        ControlSwitch(label = "Searchable", checked = searchable, onCheckedChange = { searchable = it })
    }
}

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
        TabRow(tabs = tabs, selectedIndex = index, onTabSelected = { index = it })
        Text("Selected: ${tabs[index]}")
    }
}

@Composable
internal fun BottomBarPage() {
    var index by remember { mutableIntStateOf(0) }
    var showBadge by remember { mutableStateOf(true) }
    val items =
        listOf(
            NavigationItem("Home", AppTheme.icons.check),
            NavigationItem("Tasks", AppTheme.icons.check, badge = if (showBadge) "5" else null),
            NavigationItem("Close", AppTheme.icons.close),
        )

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        BottomBar(items = items, selectedIndex = index, onItemSelected = { index = it })
        Text("Selected: ${items[index].label}")
        ControlsDivider()
        ControlSwitch(label = "Show badge", checked = showBadge, onCheckedChange = { showBadge = it })
    }
}

@Composable
internal fun NavRailPage() {
    var index by remember { mutableIntStateOf(0) }
    var showBadge by remember { mutableStateOf(true) }
    val items =
        listOf(
            NavigationItem("Home", AppTheme.icons.check),
            NavigationItem("Tasks", AppTheme.icons.check, badge = if (showBadge) "3" else null),
            NavigationItem("Close", AppTheme.icons.close),
        )

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            NavRail(items = items, selectedIndex = index, onItemSelected = { index = it })
            Column {
                SectionHeader(title = "NavRail")
                Text("Selected: ${items[index].label}")
            }
        }
        ControlsDivider()
        ControlSwitch(label = "Show badge", checked = showBadge, onCheckedChange = { showBadge = it })
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Pagination
// ─────────────────────────────────────────────────────────────────────────────

private val allDemoPaginationPages =
    listOf(
        PaginationPage(title = "Popular"),
        PaginationPage(title = "New Releases"),
        PaginationPage(title = "Top Rated"),
        PaginationPage(title = "Trending"),
        PaginationPage(title = "Staff Picks"),
        PaginationPage(title = "Coming Soon"),
    )

@Composable
private fun PagerPlaceholder(title: String) {
    Box(
        modifier =
            Modifier
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
    val alignments = listOf("Leading", "Center", "Trailing")
    var alignmentIndex by remember { mutableIntStateOf(0) }
    val titleAlignment =
        when (alignmentIndex) {
            1 -> PageTitleAlignment.Center
            2 -> PageTitleAlignment.Trailing
            else -> PageTitleAlignment.Leading
        }

    val directions = listOf("Bidirectional", "Unidirectional")
    var directionIndex by remember { mutableIntStateOf(0) }
    val direction = if (directionIndex == 0) PageDirection.Bidirectional else PageDirection.Unidirectional

    val footers = listOf("Dots", "Progress", "None")
    var footerIndex by remember { mutableIntStateOf(0) }
    val footerStyle =
        when (footerIndex) {
            1 -> PageFooterStyle.Progress
            2 -> PageFooterStyle.None
            else -> PageFooterStyle.Dots
        }

    val minPages = 2
    val maxPages = allDemoPaginationPages.size
    var pageCountSlider by remember { mutableFloatStateOf(3f) }
    val pageCount = pageCountSlider.roundToInt().coerceIn(minPages, maxPages)
    val pages = allDemoPaginationPages.take(pageCount)

    var lastPage by remember { mutableIntStateOf(0) }
    var hintResetKey by remember { mutableIntStateOf(0) }
    var rtlMode by remember { mutableStateOf(false) }
    val layoutDirection = if (rtlMode) LayoutDirection.Rtl else LayoutDirection.Ltr

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Text(text = "Current page: $lastPage")
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            key(hintResetKey) {
                PaginatedContent(
                    pages = pages,
                    titleAlignment = titleAlignment,
                    direction = direction,
                    footerStyle = footerStyle,
                    showScrollHint = true,
                    onPageChanged = { lastPage = it },
                ) { _, page -> PagerPlaceholder(page.title) }
            }
        }
        ControlsDivider()
        SecondaryButton(
            text = "Replay scroll hint",
            onClick = { hintResetKey++ },
            modifier = Modifier.fillMaxWidth(),
        )
        ControlSwitch(
            label = "RTL layout",
            checked = rtlMode,
            onCheckedChange = { rtlMode = it },
        )
        ControlSlider(
            label = "Pages: $pageCount",
            value = pageCountSlider,
            onValueChange = { pageCountSlider = it },
            valueRange = minPages.toFloat()..maxPages.toFloat(),
        )
        ControlSegmented(
            label = "Title alignment",
            options = alignments,
            selectedIndex = alignmentIndex,
            onOptionSelected = { alignmentIndex = it },
        )
        ControlSegmented(
            label = "Direction",
            options = directions,
            selectedIndex = directionIndex,
            onOptionSelected = { directionIndex = it },
        )
        ControlSegmented(
            label = "Footer",
            options = footers,
            selectedIndex = footerIndex,
            onOptionSelected = { footerIndex = it },
        )
    }
}

private val allDemoSegmentedItems =
    listOf(
        SegmentedItem(title = "Overview"),
        SegmentedItem(title = "Activity"),
        SegmentedItem(title = "Settings"),
        SegmentedItem(title = "Notifications"),
        SegmentedItem(title = "Permissions"),
        SegmentedItem(title = "Integrations"),
        SegmentedItem(title = "Billing"),
    )

@Composable
private fun SegmentPlaceholder(title: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(AppTheme.spacing.x2),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$title content",
            style = AppTheme.typography.titleSmall,
        )
    }
}

@Composable
private fun PlainSegmentTitle(
    title: String,
    isSelected: Boolean,
    indicator: SegmentSelectionIndicator,
) {
    val color =
        when {
            isSelected && indicator == SegmentSelectionIndicator.Pill -> AppTheme.colors.onPrimary
            isSelected -> AppTheme.colors.primary
            else -> AppTheme.colors.onSurfaceVariant
        }
    Text(
        text = title,
        style = AppTheme.typography.labelLarge,
        color = color,
        maxLines = 1,
    )
}

@Composable
private fun IconSegmentTitle(
    title: String,
    isSelected: Boolean,
    indicator: SegmentSelectionIndicator,
) {
    val color =
        if (isSelected) {
            if (indicator == SegmentSelectionIndicator.Pill) AppTheme.colors.onPrimary else AppTheme.colors.primary
        } else {
            AppTheme.colors.onSurfaceVariant
        }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
    ) {
        Icon(
            imageVector = AppTheme.icons.check.imageVector,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(AppTheme.spacing.x2),
        )
        Text(
            text = title,
            style = AppTheme.typography.labelLarge,
            color = color,
            maxLines = 1,
        )
    }
}

@Composable
internal fun SegmentedContentPage() {
    val indicatorOptions = listOf("Pill", "Underline", "None")
    var indicatorIndex by remember { mutableIntStateOf(0) }
    val indicator =
        when (indicatorIndex) {
            1 -> SegmentSelectionIndicator.Underline
            2 -> SegmentSelectionIndicator.None
            else -> SegmentSelectionIndicator.Pill
        }

    val fitOptions = listOf("EvenWhenFits", "Intrinsic")
    var fitIndex by remember { mutableIntStateOf(0) }
    val fitMode = if (fitIndex == 0) SegmentFitMode.EvenWhenFits else SegmentFitMode.Intrinsic

    val densityOptions = listOf("Regular", "Compact")
    var densityIndex by remember { mutableIntStateOf(0) }
    val density = if (densityIndex == 0) SegmentDensity.Regular else SegmentDensity.Compact

    val minSegments = 2
    val maxSegments = allDemoSegmentedItems.size
    var segmentCountSlider by remember { mutableFloatStateOf(3f) }
    val segmentCount = segmentCountSlider.roundToInt().coerceIn(minSegments, maxSegments)
    val items = allDemoSegmentedItems.take(segmentCount)

    var useIconTitle by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    // Keep the selection valid when the segment count shrinks below it.
    val safeSelectedIndex = selectedIndex.coerceIn(0, items.lastIndex)

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Text(text = "Selected: ${items[safeSelectedIndex].title}")
        SegmentedContent(
            items = items,
            selectedIndex = safeSelectedIndex,
            onSelectionChanged = { selectedIndex = it },
            indicator = indicator,
            fitMode = fitMode,
            density = density,
            segmentTitle = { _, item, isSelected ->
                if (useIconTitle) {
                    IconSegmentTitle(item.title, isSelected, indicator)
                } else {
                    PlainSegmentTitle(item.title, isSelected, indicator)
                }
            },
        ) { _, item -> SegmentPlaceholder(item.title) }
        ControlsDivider()
        ControlSlider(
            label = "Segments: $segmentCount",
            value = segmentCountSlider,
            onValueChange = { segmentCountSlider = it },
            valueRange = minSegments.toFloat()..maxSegments.toFloat(),
        )
        ControlSegmented(
            label = "Indicator",
            options = indicatorOptions,
            selectedIndex = indicatorIndex,
            onOptionSelected = { indicatorIndex = it },
        )
        ControlSegmented(
            label = "Fit mode",
            options = fitOptions,
            selectedIndex = fitIndex,
            onOptionSelected = { fitIndex = it },
        )
        ControlSegmented(
            label = "Density",
            options = densityOptions,
            selectedIndex = densityIndex,
            onOptionSelected = { densityIndex = it },
        )
        ControlSwitch(
            label = "Custom title slot (icon + label)",
            checked = useIconTitle,
            onCheckedChange = { useIconTitle = it },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Lists
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun ListItemPage() {
    var showSupporting by remember { mutableStateOf(true) }
    var showTrailing by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        ListItem(
            headline = "Workspace",
            supportingText = if (showSupporting) "Personal" else null,
            trailingContent = if (showTrailing) ({ Text("Open") }) else null,
        )
        ControlsDivider()
        ControlSwitch(
            label = "Supporting text",
            checked = showSupporting,
            onCheckedChange = { showSupporting = it },
        )
        ControlSwitch(
            label = "Trailing content",
            checked = showTrailing,
            onCheckedChange = { showTrailing = it },
        )
    }
}

@Composable
internal fun ContentRowPage() {
    var tappable by remember { mutableStateOf(true) }
    var showSecondary by remember { mutableStateOf(true) }
    var showSupporting by remember { mutableStateOf(true) }
    var showTrailing by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        ContentRow(
            title = "ephemeral",
            secondaryText = if (showSecondary) "/əˈfemərəl/" else null,
            supportingText = if (showSupporting) "Lasting for a very short time." else null,
            onClick = if (tappable) ({}) else null,
            trailingContent =
                if (showTrailing) {
                    { LevelBadge(label = "C1", tier = AppTheme.colors.levels.tier(2)) }
                } else {
                    null
                },
        )
        ControlsDivider()
        ControlSwitch(label = "Tappable", checked = tappable, onCheckedChange = { tappable = it })
        ControlSwitch(
            label = "Secondary text",
            checked = showSecondary,
            onCheckedChange = { showSecondary = it },
        )
        ControlSwitch(
            label = "Supporting text",
            checked = showSupporting,
            onCheckedChange = { showSupporting = it },
        )
        ControlSwitch(
            label = "Trailing badge",
            checked = showTrailing,
            onCheckedChange = { showTrailing = it },
        )
    }
}

@Composable
internal fun EmptyStatePage() {
    var showMessage by remember { mutableStateOf(true) }
    var showAction by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        EmptyState(
            title = "No projects",
            message = if (showMessage) "Create your first project to get started." else null,
            action = if (showAction) ({ TextButton(text = "Browse all", onClick = {}) }) else null,
        )
        ControlsDivider()
        ControlSwitch(label = "Message", checked = showMessage, onCheckedChange = { showMessage = it })
        ControlSwitch(label = "Action", checked = showAction, onCheckedChange = { showAction = it })
    }
}

@Composable
internal fun LoadingStatePage() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        LoadingState(label = "Loading projects…")
    }
}

@Composable
internal fun ErrorStatePage() {
    var showAction by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        ErrorState(
            title = "Could not load",
            message = "Check your connection and retry.",
            action = if (showAction) ({ TextButton(text = "Retry", onClick = {}) }) else null,
        )
        ControlsDivider()
        ControlSwitch(label = "Action", checked = showAction, onCheckedChange = { showAction = it })
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Containers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun CardPage() {
    var multiLine by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Card {
            Text("Plan")
            if (multiLine) {
                Text("Compose Pro")
                Text("Active since January 2024")
            }
        }
        ControlsDivider()
        ControlSwitch(
            label = "Multi-line content",
            checked = multiLine,
            onCheckedChange = { multiLine = it },
        )
    }
}

@Composable
internal fun OverlayCardPage() {
    var multiLine by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        OverlayCard {
            Text("Overlay card")
            if (multiLine) {
                Text("Supporting description below the title")
            }
        }
        ControlsDivider()
        ControlSwitch(
            label = "Multi-line content",
            checked = multiLine,
            onCheckedChange = { multiLine = it },
        )
    }
}

@Composable
internal fun FlipCardPage() {
    var flipped by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    val axisOptions = listOf("Horizontal", "Vertical")
    var axisIndex by remember { mutableIntStateOf(0) }
    val axis = if (axisIndex == 0) FlipAxis.Horizontal else FlipAxis.Vertical

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        FlipCard(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(140.dp),
            flipped = flipped,
            onFlippedChange = { flipped = it },
            axis = axis,
            enabled = enabled,
            onClickLabel = "Flip card",
            frontStateDescription = "Showing question",
            backStateDescription = "Showing answer",
            front = {
                Box(
                    modifier = Modifier.fillMaxSize().padding(AppTheme.spacing.lg),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "What is Jetpack Compose?", style = AppTheme.typography.titleSmall)
                }
            },
            back = {
                Box(
                    modifier = Modifier.fillMaxSize().padding(AppTheme.spacing.lg),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "A declarative UI toolkit for Android.",
                        style = AppTheme.typography.bodyMedium,
                    )
                }
            },
        )
        ControlsDivider()
        SecondaryButton(
            text = if (flipped) "Show front" else "Show back",
            onClick = { flipped = !flipped },
            modifier = Modifier.fillMaxWidth(),
        )
        ControlSwitch(label = "Tap to flip (enabled)", checked = enabled, onCheckedChange = { enabled = it })
        ControlSegmented(
            label = "Axis",
            options = axisOptions,
            selectedIndex = axisIndex,
            onOptionSelected = { axisIndex = it },
        )
    }
}

@Composable
internal fun SnackbarPage() {
    var showAction by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Snackbar(
            message = "Item deleted",
            actionLabel = if (showAction) "Undo" else null,
            onAction = if (showAction) ({}) else null,
        )
        ControlsDivider()
        ControlSwitch(label = "Action", checked = showAction, onCheckedChange = { showAction = it })
    }
}

@Composable
internal fun ToastPage() {
    var showAction by remember { mutableStateOf(false) }
    var showIcon by remember { mutableStateOf(false) }
    var longMessage by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Toast(
            message =
                if (longMessage) {
                    "Your changes are saved and will sync to all of your devices shortly."
                } else {
                    "Toast message"
                },
            icon = if (showIcon) AppTheme.icons.check else null,
            actionLabel = if (showAction) "View" else null,
            onAction = if (showAction) ({}) else null,
        )
        ControlsDivider()
        ControlSwitch(label = "Action", checked = showAction, onCheckedChange = { showAction = it })
        ControlSwitch(label = "Icon", checked = showIcon, onCheckedChange = { showIcon = it })
        ControlSwitch(label = "Long message", checked = longMessage, onCheckedChange = { longMessage = it })
    }
}

@Composable
internal fun ToastHostPage() {
    val hostState = rememberToastHostState()
    val scope = rememberCoroutineScope()
    var withAction by remember { mutableStateOf(true) }
    var withIcon by remember { mutableStateOf(false) }
    // Show the actual timeout so it's obvious how long each option lasts.
    val durationOptions = listOf("4s", "10s", "∞")
    var durationIndex by remember { mutableIntStateOf(0) }
    val positionOptions = listOf("Bottom", "Top")
    var positionIndex by remember { mutableIntStateOf(0) }

    val duration =
        when (durationIndex) {
            1 -> ToastDuration.Long
            2 -> ToastDuration.Indefinite
            else -> ToastDuration.Short
        }
    val position = if (positionIndex == 1) ToastPosition.Top else ToastPosition.Bottom
    // Read the icon token in composable scope; it can't be read inside launch{}.
    val checkIcon = AppTheme.icons.check

    Box(modifier = Modifier.fillMaxWidth().height(AppTheme.spacing.xl * 12)) {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            PrimaryButton(
                text = "Show toast",
                onClick = {
                    scope.launch {
                        hostState.showToast(
                            message = "Toast message",
                            icon = if (withIcon) checkIcon else null,
                            actionLabel = if (withAction) "Undo" else null,
                            duration = duration,
                        )
                    }
                },
            )
            ControlsDivider()
            ControlSwitch(label = "Action", checked = withAction, onCheckedChange = { withAction = it })
            ControlSwitch(label = "Icon", checked = withIcon, onCheckedChange = { withIcon = it })
            ControlSegmented(
                label = "Duration",
                options = durationOptions,
                selectedIndex = durationIndex,
                onOptionSelected = { durationIndex = it },
            )
            ControlSegmented(
                label = "Position",
                options = positionOptions,
                selectedIndex = positionIndex,
                onOptionSelected = { positionIndex = it },
            )
        }
        ToastHost(
            hostState = hostState,
            position = position,
            dismissContentDescription = "Dismiss",
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Feedback
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun ProgressIndicatorPage() {
    var determinate by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0.45f) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        ProgressIndicator(
            progress = if (determinate) progress else null,
            label =
                if (determinate) {
                    "Progress: ${(progress * 100).toInt()}%"
                } else {
                    "Loading…"
                },
        )
        ControlsDivider()
        ControlSwitch(
            label = "Determinate",
            checked = determinate,
            onCheckedChange = { determinate = it },
        )
        if (determinate) {
            ControlSlider(
                label = "Progress: ${(progress * 100).toInt()}%",
                value = progress,
                onValueChange = { progress = it },
            )
        }
    }
}

@Composable
internal fun SkeletonPage() {
    var count by remember { mutableIntStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        repeat(count) { Skeleton() }
        ControlsDivider()
        ControlSegmented(
            label = "Count",
            options = listOf("1", "2", "3"),
            selectedIndex = count - 1,
            onOptionSelected = { count = it + 1 },
        )
    }
}

@Composable
internal fun SkeletonBlockPage() {
    var heightFraction by remember { mutableFloatStateOf(0.5f) }
    var widthFraction by remember { mutableFloatStateOf(0.5f) }
    val height: Dp = (40 + (heightFraction * 120)).dp
    val width: Dp = (40 + (widthFraction * 200)).dp

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SkeletonBlock(height = height, width = width)
        ControlsDivider()
        ControlSlider(
            label = "Height: ${height.value.toInt()} dp",
            value = heightFraction,
            onValueChange = { heightFraction = it },
        )
        ControlSlider(
            label = "Width: ${width.value.toInt()} dp",
            value = widthFraction,
            onValueChange = { widthFraction = it },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Typography
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun AppTextPage() {
    val styles = listOf("Display", "Title", "Body", "Label")
    var styleIndex by remember { mutableIntStateOf(2) }
    val style =
        when (styleIndex) {
            0 -> AppTextStyle.Display
            1 -> AppTextStyle.Title
            3 -> AppTextStyle.Label
            else -> AppTextStyle.Body
        }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        AppText(
            text = "The quick brown fox jumps over the lazy dog.",
            style = style,
        )
        ControlsDivider()
        ControlSegmented(
            label = "Style",
            options = styles,
            selectedIndex = styleIndex,
            onOptionSelected = { styleIndex = it },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Chips & Badges
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun PillChipPage() {
    var selected by remember { mutableIntStateOf(0) }
    val options = listOf("All", "Beginner", "Intermediate", "Advanced")

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            options.forEachIndexed { i, label ->
                PillChip(label = label, isSelected = i == selected, onClick = { selected = i })
            }
        }
        Text(text = "Selected: ${options[selected]}")
    }
}

@Composable
internal fun LevelBadgePage() {
    val tiers = listOf("Tier 0", "Tier 1", "Tier 2")
    var tierIndex by remember { mutableIntStateOf(0) }
    val labels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
    var labelIndex by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        LevelBadge(
            label = labels[labelIndex],
            tier = AppTheme.colors.levels.tier(tierIndex),
        )
        ControlsDivider()
        ControlSegmented(
            label = "Tier",
            options = tiers,
            selectedIndex = tierIndex,
            onOptionSelected = { tierIndex = it },
        )
        ControlSegmented(
            label = "Label",
            options = labels,
            selectedIndex = labelIndex,
            onOptionSelected = { labelIndex = it },
        )
    }
}

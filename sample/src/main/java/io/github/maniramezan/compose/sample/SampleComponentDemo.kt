package io.github.maniramezan.compose.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

/**
 * Describes a single component demo entry in the sample browser.
 *
 * Keep all registry state private to :sample — this is never a public API.
 */
@Stable
internal data class SampleComponentDemo(
    /** Unique identifier used as stable key for navigation state. */
    val id: String,
    /** Short display name shown in the list pane. */
    val title: String,
    /** Category label used to group entries in the list pane. */
    val category: String,
    /** One-line description rendered in the detail pane header. */
    val description: String,
    /** Composable that renders the interactive demo surface. */
    val content: @Composable () -> Unit,
)

/**
 * List of all component demos. Category grouping is derived from [SampleComponentDemo.category];
 * see [groupedByCategory] for the display order (categories and entries are sorted alphabetically).
 */
internal fun sampleDemos(): List<SampleComponentDemo> =
    listOf(
        SampleComponentDemo(
            id = "action-pill",
            title = "ActionPill",
            category = "Actions",
            description = "Compact slot-based action with configurable content, colors, and padding.",
            content = { ActionPillPage() },
        ),
        SampleComponentDemo(
            id = "primary-button",
            title = "PrimaryButton",
            category = "Actions",
            description = "Filled high-emphasis button for primary actions.",
            content = { PrimaryButtonPage() },
        ),
        SampleComponentDemo(
            id = "secondary-button",
            title = "SecondaryButton",
            category = "Actions",
            description = "Outlined medium-emphasis button for secondary actions.",
            content = { SecondaryButtonPage() },
        ),
        SampleComponentDemo(
            id = "text-button",
            title = "TextButton",
            category = "Actions",
            description = "Text-only low-emphasis button for tertiary actions.",
            content = { TextButtonPage() },
        ),
        SampleComponentDemo(
            id = "icon-button",
            title = "IconButton",
            category = "Actions",
            description = "Icon-only button with a 48 dp touch target.",
            content = { IconButtonPage() },
        ),
        SampleComponentDemo(
            id = "fab",
            title = "FAB",
            category = "Actions",
            description = "Floating action button for the primary screen action.",
            content = { FabPage() },
        ),
        SampleComponentDemo(
            id = "segmented-control",
            title = "SegmentedControl",
            category = "Actions",
            description = "Horizontally arranged mutually exclusive option selector.",
            content = { SegmentedControlPage() },
        ),
        SampleComponentDemo(
            id = "text-field",
            title = "TextField",
            category = "Inputs",
            description = "Single-line outlined text field with label and error state.",
            content = { TextFieldPage() },
        ),
        SampleComponentDemo(
            id = "password-field",
            title = "PasswordField",
            category = "Inputs",
            description = "Password field with optional visibility toggle.",
            content = { PasswordFieldPage() },
        ),
        SampleComponentDemo(
            id = "search-field",
            title = "SearchField",
            category = "Inputs",
            description = "Search-optimised text field with leading icon slot.",
            content = { SearchFieldPage() },
        ),
        SampleComponentDemo(
            id = "checkbox",
            title = "Checkbox",
            category = "Controls",
            description = "Binary on/off control with a text label.",
            content = { CheckboxPage() },
        ),
        SampleComponentDemo(
            id = "radio-group",
            title = "RadioGroup",
            category = "Controls",
            description = "Mutually exclusive vertical list of radio options.",
            content = { RadioGroupPage() },
        ),
        SampleComponentDemo(
            id = "switch",
            title = "Switch",
            category = "Controls",
            description = "Toggle switch for enabling or disabling a setting.",
            content = { SwitchPage() },
        ),
        SampleComponentDemo(
            id = "slider",
            title = "Slider",
            category = "Controls",
            description = "Continuous value selector across a floating-point range.",
            content = { SliderPage() },
        ),
        SampleComponentDemo(
            id = "selection-list",
            title = "SelectionList",
            category = "Selection",
            description = "Searchable single/multiple-choice list with inline two-level disclosure.",
            content = { SelectionListPage() },
        ),
        SampleComponentDemo(
            id = "top-app-bar",
            title = "TopAppBar",
            category = "Navigation",
            description = "Screen-level header bar with title, nav icon, and actions.",
            content = { TopAppBarPage() },
        ),
        SampleComponentDemo(
            id = "tab-row",
            title = "TabRow",
            category = "Navigation",
            description = "Horizontal row of primary tabs.",
            content = { TabRowPage() },
        ),
        SampleComponentDemo(
            id = "nav-rail",
            title = "NavRail",
            category = "Navigation",
            description = "Vertical rail navigation for medium and large screens.",
            content = { NavRailPage() },
        ),
        SampleComponentDemo(
            id = "paginated-content",
            title = "PaginatedContent",
            category = "Pagination",
            description = "Horizontally paginated container with footer controls.",
            content = { PaginatedContentPage() },
        ),
        SampleComponentDemo(
            id = "segmented-content",
            title = "SegmentedContent",
            category = "Pagination",
            description = "Tap-driven segmented picker with scrolling overflow and edge fades.",
            content = { SegmentedContentPage() },
        ),
        SampleComponentDemo(
            id = "tab-bar",
            title = "TabBar",
            category = "Pagination",
            description = "Bottom navigation bar that swaps the view above it per tab, with badge and RTL support.",
            content = { TabBarPage() },
        ),
        SampleComponentDemo(
            id = "showcase-feed",
            title = "ShowcaseFeed",
            category = "Layout",
            description = "App Store–style feed: vertical sections that each scroll horizontally with a peeking next item.",
            content = { ShowcaseFeedPage() },
        ),
        SampleComponentDemo(
            id = "list-item",
            title = "ListItem",
            category = "Lists",
            description = "Single-row list item with headline, supporting text, and slots.",
            content = { ListItemPage() },
        ),
        SampleComponentDemo(
            id = "content-row",
            title = "ContentRow",
            category = "Lists",
            description = "Tappable content row with title, secondary text, and trailing slot.",
            content = { ContentRowPage() },
        ),
        SampleComponentDemo(
            id = "empty-state",
            title = "EmptyState",
            category = "Lists",
            description = "Placeholder shown when a list has no items.",
            content = { EmptyStatePage() },
        ),
        SampleComponentDemo(
            id = "loading-state",
            title = "LoadingState",
            category = "Lists",
            description = "Centred spinner shown while data is loading.",
            content = { LoadingStatePage() },
        ),
        SampleComponentDemo(
            id = "error-state",
            title = "ErrorState",
            category = "Lists",
            description = "Error message with optional retry action.",
            content = { ErrorStatePage() },
        ),
        SampleComponentDemo(
            id = "card",
            title = "Card",
            category = "Containers",
            description = "Elevated surface card with padded column content.",
            content = { CardPage() },
        ),
        SampleComponentDemo(
            id = "overlay-card",
            title = "OverlayCard",
            category = "Containers",
            description = "Translucent card intended to float over media content.",
            content = { OverlayCardPage() },
        ),
        SampleComponentDemo(
            id = "flip-card",
            title = "FlipCard",
            category = "Containers",
            description = "Two-sided card with an animated 3D flip for flash-card style UIs.",
            content = { FlipCardPage() },
        ),
        SampleComponentDemo(
            id = "snackbar",
            title = "Snackbar",
            category = "Containers",
            description = "Brief message bar with optional action.",
            content = { SnackbarPage() },
        ),
        SampleComponentDemo(
            id = "toast",
            title = "Toast",
            category = "Containers",
            description = "Dark pill notification with optional action label.",
            content = { ToastPage() },
        ),
        SampleComponentDemo(
            id = "toast-host",
            title = "ToastHost",
            category = "Containers",
            description = "Shows toasts with auto-dismiss or keep-until-dismissed behavior.",
            content = { ToastHostPage() },
        ),
        SampleComponentDemo(
            id = "progress-indicator",
            title = "ProgressIndicator",
            category = "Feedback",
            description = "Linear or circular progress with optional label.",
            content = { ProgressIndicatorPage() },
        ),
        SampleComponentDemo(
            id = "skeleton",
            title = "Skeleton",
            category = "Feedback",
            description = "Full-width shimmer placeholder for loading content.",
            content = { SkeletonPage() },
        ),
        SampleComponentDemo(
            id = "skeleton-block",
            title = "SkeletonBlock",
            category = "Feedback",
            description = "Sized skeleton placeholder block for ghost layouts.",
            content = { SkeletonBlockPage() },
        ),
        SampleComponentDemo(
            id = "typewriter-reveal",
            title = "rememberTypewriterReveal",
            category = "Feedback",
            description = "Progressively reveals a growing target string, e.g. a token stream.",
            content = { TypewriterRevealPage() },
        ),
        SampleComponentDemo(
            id = "assistant-chat",
            title = "AssistantChat",
            category = "Feedback",
            description = "Full assistant surface with context, chat log, quick actions, status, and limit states.",
            content = { AssistantChatPage() },
        ),
        SampleComponentDemo(
            id = "app-text",
            title = "AppText",
            category = "Typography",
            description = "Type-safe text component covering all AppTextStyle variants.",
            content = { AppTextPage() },
        ),
        SampleComponentDemo(
            id = "pill-chip",
            title = "PillChip",
            category = "Chips & Badges",
            description = "Selectable pill-shaped filter chip.",
            content = { PillChipPage() },
        ),
        SampleComponentDemo(
            id = "level-badge",
            title = "PillChip (tier badge)",
            category = "Chips & Badges",
            description = "Coloured tier badge for proficiency or level indicators.",
            content = { PillChipTierBadgePage() },
        ),
    )

/**
 * Returns demo entries grouped by [SampleComponentDemo.category], with categories sorted
 * alphabetically and entries within each category sorted alphabetically by [SampleComponentDemo.title].
 */
internal fun List<SampleComponentDemo>.groupedByCategory(): Map<String, List<SampleComponentDemo>> =
    groupBy { it.category }
        .toSortedMap()
        .mapValues { (_, entries) -> entries.sortedBy { it.title } }

/**
 * Case-insensitive filter over title, category, and description. A blank query
 * returns the list unchanged.
 */
internal fun List<SampleComponentDemo>.matching(query: String): List<SampleComponentDemo> {
    val trimmed = query.trim()
    if (trimmed.isEmpty()) return this
    return filter { demo ->
        demo.title.contains(trimmed, ignoreCase = true) ||
            demo.category.contains(trimmed, ignoreCase = true) ||
            demo.description.contains(trimmed, ignoreCase = true)
    }
}

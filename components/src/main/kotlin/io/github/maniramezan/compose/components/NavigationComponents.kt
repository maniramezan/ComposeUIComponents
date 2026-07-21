package io.github.maniramezan.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTarget
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight
import kotlin.math.roundToInt
import androidx.compose.material3.LargeTopAppBar as MaterialLargeTopAppBar
import androidx.compose.material3.MediumTopAppBar as MaterialMediumTopAppBar
import androidx.compose.material3.TopAppBar as MaterialTopAppBar

/**
 * A single destination entry shared by [TabBar], [NavRail], and [AdaptiveNavScaffold].
 *
 * [icon] and [label] are full composable slots (not raw [String]/`ImageVector` params) so
 * callers can supply badges, animated/selected-icon swaps, or any custom rendering without
 * the component needing a dedicated parameter for every variant.
 *
 * @param value the caller-owned identity of this destination (a route, enum, or any
 *   `equals`-comparable type). Selection is determined by comparing this to the current
 *   selection value, not by list position — reordering [items][TabBar] does not change which
 *   destination is selected.
 * @param icon the destination's icon. Rendered with the resolved selected/unselected/disabled
 *   content color already applied via [LocalContentColor]; pass a plain `Icon(...)` with no
 *   explicit tint.
 * @param label the destination's text label, or `null` for an icon-only destination. Rendered
 *   with the resolved content color and [AppTheme]'s `labelSmall` style already applied.
 * @param badge an optional composable pinned to the icon's top-end corner (e.g. an
 *   unread count). Supply your own `contentDescription` semantics on the badge content if it
 *   conveys information beyond what [contentDescription] already announces.
 * @param enabled whether this destination can be selected. Disabled items render with
 *   [TabBarItemColors]'s disabled colors and do not invoke the selection callback.
 * @param contentDescription supply a localized string describing this destination for
 *   TalkBack. Required when [label] is `null` (icon-only), since there is otherwise no
 *   accessible name for the destination; ignored when [label] is non-null, since the visible
 *   label already provides an accessible name.
 */
@Immutable
public data class TabBarItemData<T>(
    public val value: T,
    public val icon: @Composable () -> Unit,
    public val label: (@Composable () -> Unit)? = null,
    public val badge: (@Composable () -> Unit)? = null,
    public val enabled: Boolean = true,
    public val contentDescription: String = "",
)

/** Immutable color set for a [TabBar]/[NavRail] destination in each interactive state. */
@Immutable
public data class TabBarItemColors(
    public val selectedIconColor: Color,
    public val selectedTextColor: Color,
    public val selectedIndicatorColor: Color,
    public val unselectedIconColor: Color,
    public val unselectedTextColor: Color,
    public val disabledIconColor: Color,
    public val disabledTextColor: Color,
)

/** How destinations are laid out within a [TabBar]. */
public enum class TabBarArrangement {
    /** Every destination gets equal width, filling the bar (3–5 destinations; the common case). */
    EqualWeight,

    /** Destinations are sized to their content and grouped in the horizontal center of the bar. */
    Centered,
}

/** Default values, colors, and sizing tokens used by [TabBar], [NavRail], and [AdaptiveNavScaffold]. */
public object TabBarDefaults {
    /** Minimum content height of a [TabBar], excluding system bar insets. Matches Material 3's
     *  own NavigationBarTokens.ContainerHeight (64dp) — the same value used by stock Android
     *  bottom navigation (e.g. Play Store, Gmail). */
    public val MinHeight: Dp
        @Composable get() = AppTheme.spacing.x8

    /** Width of the selected-destination indicator pill. Matches Material 3's own
     *  NavigationBarVerticalItemTokens.ActiveIndicatorWidth (56dp) — the same value
     *  used by stock Android bottom navigation (e.g. Play Store, Gmail). */
    public val IndicatorWidth: Dp
        @Composable get() = AppTheme.spacing.x7

    /** Height of the selected-destination indicator pill. */
    public val IndicatorHeight: Dp
        @Composable get() = AppTheme.spacing.x4

    /** Default [TabBarArrangement] used when none is supplied. */
    public val arrangement: TabBarArrangement = TabBarArrangement.EqualWeight

    /** Default insets consumed by [TabBar]: horizontal + bottom system bars only. */
    public val windowInsets: WindowInsets
        @Composable
        get() =
            WindowInsets.systemBars.only(
                WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
            )

    /**
     * Builds a [TabBarItemColors], defaulting every slot to an [AppTheme] color.
     *
     * [selectedIndicatorColor] defaults to `AppTheme.colors.primaryContainer` — a
     * selected-destination pill behind the icon, matching stock Android bottom navigation (e.g.
     * Play Store, Gmail). Pass [Color.Transparent] for tint-only selection styling instead, with
     * no background highlight.
     */
    @Composable
    public fun itemColors(
        selectedIconColor: Color = AppTheme.colors.primary,
        selectedTextColor: Color = AppTheme.colors.primary,
        selectedIndicatorColor: Color = AppTheme.colors.primaryContainer,
        unselectedIconColor: Color = AppTheme.colors.onSurfaceVariant,
        unselectedTextColor: Color = AppTheme.colors.onSurfaceVariant,
        disabledIconColor: Color = unselectedIconColor.copy(alpha = 0.38f),
        disabledTextColor: Color = unselectedTextColor.copy(alpha = 0.38f),
    ): TabBarItemColors =
        TabBarItemColors(
            selectedIconColor = selectedIconColor,
            selectedTextColor = selectedTextColor,
            selectedIndicatorColor = selectedIndicatorColor,
            unselectedIconColor = unselectedIconColor,
            unselectedTextColor = unselectedTextColor,
            disabledIconColor = disabledIconColor,
            disabledTextColor = disabledTextColor,
        )
}

private val LocalTabBarArrangement = staticCompositionLocalOf { TabBarDefaults.arrangement }

/** A themed top app bar with a [title] and optional [navigationIcon]/[actions] slots. */
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

/**
 * A bottom navigation bar for switching between top-level, [items]-driven destinations.
 * See [AdaptiveNavScaffold] for width-aware switching between this and [NavRail].
 *
 * This is the data-driven convenience overload. Use the [TabBar] primitive overload (with a
 * `content` slot of [TabBarItem] calls) when you need full control over individual items.
 *
 * @param scrollBehavior when supplied (see [rememberTabBarScrollBehavior]), the bar hides
 *   itself as the caller's scrollable content scrolls down and reappears on scroll up. Attach
 *   the same behavior's `nestedScrollConnection` to that scrollable content.
 */
@Composable
public fun <T> TabBar(
    items: List<TabBarItemData<T>>,
    selection: T,
    onSelectionChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colors.surface,
    windowInsets: WindowInsets = TabBarDefaults.windowInsets,
    arrangement: TabBarArrangement = TabBarDefaults.arrangement,
    colors: TabBarItemColors = TabBarDefaults.itemColors(),
    scrollBehavior: TabBarScrollBehavior? = null,
) {
    TabBar(
        modifier = modifier,
        containerColor = containerColor,
        windowInsets = windowInsets,
        arrangement = arrangement,
        scrollBehavior = scrollBehavior,
    ) {
        items.forEach { item ->
            TabBarItem(
                value = item.value,
                selection = selection,
                onSelectionChange = onSelectionChange,
                icon = item.icon,
                enabled = item.enabled,
                label = item.label,
                badge = item.badge,
                colors = colors,
                contentDescription = item.contentDescription,
            )
        }
    }
}

/**
 * A bottom navigation bar primitive. Compose destinations inside [content] with [TabBarItem].
 *
 * Prefer the data-driven `TabBar(items = ..., selection = ..., onSelectionChange = ...)`
 * overload for the common case; use this primitive when individual items need per-item
 * composition that a plain data model can't express.
 *
 * @param scrollBehavior when supplied (see [rememberTabBarScrollBehavior]), the bar hides
 *   itself as the caller's scrollable content scrolls down and reappears on scroll up.
 */
@Composable
public fun TabBar(
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colors.surface,
    windowInsets: WindowInsets = TabBarDefaults.windowInsets,
    arrangement: TabBarArrangement = TabBarDefaults.arrangement,
    scrollBehavior: TabBarScrollBehavior? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val heightOffset = scrollBehavior?.heightOffset ?: 0f
    Surface(
        color = containerColor,
        modifier =
            modifier
                // Clip outside the size-reporting layout so hidden content cannot draw into the
                // space that the parent reclaims as this bar collapses.
                .graphicsLayer { clip = true }
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    // Report the bar's full natural height as the scroll behavior's collapse
                    // limit, then report a *shrunk* layout size to our own parent (e.g. Scaffold)
                    // as the bar hides — not just a draw-time offset — so the parent's reserved
                    // space (and therefore the content above it) actually shrinks/expands as the
                    // bar hides/reappears, instead of always reserving the bar's full height.
                    scrollBehavior?.updateHeightOffsetLimit(-placeable.height.toFloat())
                    val shrunkHeight = (placeable.height + heightOffset.roundToInt()).coerceIn(0, placeable.height)
                    layout(placeable.width, shrunkHeight) {
                        // Keep the bar's bottom edge pinned to the shrunk box's bottom edge, so it
                        // collapses top-down (sinks toward the bottom of the screen) rather than
                        // from the bottom up.
                        placeable.place(0, shrunkHeight - placeable.height)
                    }
                },
    ) {
        CompositionLocalProvider(LocalTabBarArrangement provides arrangement) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(windowInsets)
                        .defaultMinSize(minHeight = TabBarDefaults.MinHeight)
                        .selectableGroup(),
                horizontalArrangement =
                    when (arrangement) {
                        TabBarArrangement.EqualWeight -> Arrangement.Start
                        TabBarArrangement.Centered ->
                            Arrangement.spacedBy(AppTheme.spacing.x2, Alignment.CenterHorizontally)
                    },
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }
    }
}

/**
 * A single destination inside a [TabBar]. See [TabBarItemData] for the parameter contract —
 * this composable mirrors it exactly for the primitive, slot-based call style.
 */
@Composable
public fun <T> RowScope.TabBarItem(
    value: T,
    selection: T,
    onSelectionChange: (T) -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
    colors: TabBarItemColors = TabBarDefaults.itemColors(),
    contentDescription: String = "",
) {
    val selected = value == selection
    val arrangement = LocalTabBarArrangement.current
    val motionSpec = tween<Color>(durationMillis = AppTheme.motion.shortMillis, easing = AppTheme.motion.emphasizedEasing)

    val iconColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> colors.disabledIconColor
                selected -> colors.selectedIconColor
                else -> colors.unselectedIconColor
            },
        animationSpec = motionSpec,
        label = "TabBarItemIconColor",
    )
    val textColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> colors.disabledTextColor
                selected -> colors.selectedTextColor
                else -> colors.unselectedTextColor
            },
        animationSpec = motionSpec,
        label = "TabBarItemTextColor",
    )

    var itemModifier = modifier
    if (arrangement == TabBarArrangement.EqualWeight) {
        itemModifier = itemModifier.weight(1f)
    }
    itemModifier =
        itemModifier
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.Tab,
                onClick = { onSelectionChange(value) },
            ).minimumTouchTarget(minimumTouchTargetSize())
    if (label == null && contentDescription.isNotBlank()) {
        itemModifier = itemModifier.semantics { this.contentDescription = contentDescription }
    }

    Column(
        modifier = itemModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
    ) {
        Box(
            modifier = Modifier.size(width = TabBarDefaults.IndicatorWidth, height = TabBarDefaults.IndicatorHeight),
            contentAlignment = Alignment.Center,
        ) {
            SelectionIndicatorBackground(
                visible = selected,
                color = colors.selectedIndicatorColor,
                modifier = Modifier.matchParentSize(),
            )
            CompositionLocalProvider(LocalContentColor provides iconColor) {
                BadgeAwareIcon(icon = icon, badge = badge)
            }
        }
        if (label != null) {
            CompositionLocalProvider(
                LocalContentColor provides textColor,
                LocalTextStyle provides AppTheme.typography.labelSmall,
            ) {
                label()
            }
        }
    }
}

/**
 * The selected-destination indicator, drawn as a background fill behind the icon.
 *
 * Sized via [Modifier.matchParentSize] against a *fixed*-size parent (see [TabBarItem]) rather
 * than [androidx.compose.animation.AnimatedVisibility], and animated via alpha/scale instead of
 * entering/exiting the composition — so the indicator never changes the icon slot's layout
 * size, and selecting a destination never shifts the label's position. Pass
 * `color = Color.Transparent` (see [TabBarDefaults.itemColors]) to render no indicator at all
 * and rely on icon/label tint alone to convey selection, matching plain Material tab styling.
 */
@Composable
private fun SelectionIndicatorBackground(
    visible: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val motionSpec = tween<Float>(durationMillis = AppTheme.motion.mediumMillis, easing = AppTheme.motion.emphasizedEasing)
    val alpha by animateFloatAsState(targetValue = if (visible) 1f else 0f, animationSpec = motionSpec, label = "TabBarIndicatorAlpha")
    val scale by animateFloatAsState(targetValue = if (visible) 1f else 0.6f, animationSpec = motionSpec, label = "TabBarIndicatorScale")
    Box(
        modifier =
            modifier
                .alpha(alpha)
                .scale(scale)
                .clip(AppTheme.shapes.pill)
                .background(color),
    )
}

/**
 * Renders [icon], with [badge] (if non-null) pinned to its top-end corner.
 *
 * Uses a plain [Box] + [Alignment.TopEnd] + outward [Modifier.offset] instead of Material 3's
 * `BadgedBox`, whose default placement overlaps the badge inward over the icon rather than
 * sitting at the corner. The outward offset here pushes the badge to peek past the icon's
 * bounds, closer to how corner badges commonly read (e.g. notification badges).
 */
@Composable
private fun BadgeAwareIcon(
    icon: @Composable () -> Unit,
    badge: (@Composable () -> Unit)?,
) {
    if (badge != null) {
        Box {
            icon()
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = AppTheme.spacing.x1, y = -AppTheme.spacing.x1),
            ) {
                badge()
            }
        }
    } else {
        icon()
    }
}

/** A primary tab row for switching between [tabs] at the same navigation level. */
@Composable
public fun TabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                modifier = Modifier.minimumTouchTargetHeight(minimumTouchTargetSize()),
                text = { Text(text = tab) },
            )
        }
    }
}

/**
 * A side navigation rail for medium/expanded-width screens; see [AdaptiveNavScaffold] for
 * width-aware switching between this and [TabBar]. Shares [TabBarItemData]/[TabBarItemColors]
 * with [TabBar] so both surfaces stay visually consistent.
 */
@Composable
public fun <T> NavRail(
    items: List<TabBarItemData<T>>,
    selection: T,
    onSelectionChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    colors: TabBarItemColors = TabBarDefaults.itemColors(),
) {
    NavigationRail(modifier = modifier) {
        items.forEach { item ->
            val selected = item.value == selection
            NavigationRailItem(
                selected = selected,
                enabled = item.enabled,
                onClick = { onSelectionChange(item.value) },
                icon = { BadgeAwareIcon(icon = item.icon, badge = item.badge) },
                label = item.label,
                colors =
                    NavigationRailItemDefaults.colors(
                        selectedIconColor = colors.selectedIconColor,
                        selectedTextColor = colors.selectedTextColor,
                        indicatorColor = colors.selectedIndicatorColor,
                        unselectedIconColor = colors.unselectedIconColor,
                        unselectedTextColor = colors.unselectedTextColor,
                        disabledIconColor = colors.disabledIconColor,
                        disabledTextColor = colors.disabledTextColor,
                    ),
                modifier =
                    Modifier
                        .minimumTouchTargetHeight(minimumTouchTargetSize())
                        .let {
                            if (item.label == null && item.contentDescription.isNotBlank()) {
                                it.semantics { contentDescription = item.contentDescription }
                            } else {
                                it
                            }
                        },
            )
        }
    }
}

/** A themed medium (two-line) top app bar with a [title] and optional [navigationIcon]/[actions] slots. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MediumTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    MaterialMediumTopAppBar(
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

/** A themed large (three-line) top app bar with a [title] and optional [navigationIcon]/[actions] slots. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LargeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    MaterialLargeTopAppBar(
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

/**
 * An adaptive navigation scaffold that shows a [TabBar] on compact screens (< 600 dp wide)
 * and a [NavRail] on medium/expanded screens. Use the [topBar] slot for a [TopAppBar],
 * [MediumTopAppBar], or [LargeTopAppBar].
 *
 * @param scrollBehavior forwarded to the compact-width [TabBar] only; the expanded-width
 *   [NavRail] stays persistent and does not hide on scroll.
 */
@Composable
public fun <T> AdaptiveNavScaffold(
    items: List<TabBarItemData<T>>,
    selection: T,
    onSelectionChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    colors: TabBarItemColors = TabBarDefaults.itemColors(),
    scrollBehavior: TabBarScrollBehavior? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val containerWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidthDp = with(LocalDensity.current) { containerWidthPx.toDp() }
    val isExpandedWidth = screenWidthDp >= AppTheme.spacing.expandedNavigationBreakpoint
    if (isExpandedWidth) {
        Scaffold(topBar = topBar, modifier = modifier) { innerPadding ->
            Row(modifier = Modifier.padding(innerPadding)) {
                NavRail(
                    items = items,
                    selection = selection,
                    onSelectionChange = onSelectionChange,
                    colors = colors,
                )
                Box(modifier = Modifier.weight(1f)) {
                    content(PaddingValues())
                }
            }
        }
    } else {
        Scaffold(
            modifier = modifier,
            topBar = topBar,
            bottomBar = {
                TabBar(
                    items = items,
                    selection = selection,
                    onSelectionChange = onSelectionChange,
                    colors = colors,
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.ContentRow
import io.github.maniramezan.compose.components.EmptyState
import io.github.maniramezan.compose.components.ErrorState
import io.github.maniramezan.compose.components.LazyList
import io.github.maniramezan.compose.components.ListItem
import io.github.maniramezan.compose.components.LoadMoreFooter
import io.github.maniramezan.compose.components.LoadingState
import io.github.maniramezan.compose.components.PillChip
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.theme.AppTheme
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────────────────────────────────────
// Lists
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun ListItemPage() {
    var showSupporting by remember { mutableStateOf(true) }
    var showTrailing by remember { mutableStateOf(true) }

    SamplePage(
        preview = {
            ListItem(
                headline = "Workspace",
                supportingText = if (showSupporting) "Personal" else null,
                trailingContent = if (showTrailing) ({ Text("Open") }) else null,
            )
        },
        controls = {
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
        },
    )
}

@Composable
internal fun ContentRowPage() {
    var tappable by remember { mutableStateOf(true) }
    var showSecondary by remember { mutableStateOf(true) }
    var showSupporting by remember { mutableStateOf(true) }
    var showTrailing by remember { mutableStateOf(true) }

    SamplePage(
        preview = {
            ContentRow(
                title = "ephemeral",
                secondaryText = if (showSecondary) "/əˈfemərəl/" else null,
                supportingText = if (showSupporting) "Lasting for a very short time." else null,
                onClick = if (tappable) ({}) else null,
                trailingContent =
                    if (showTrailing) {
                        { PillChip(label = "C1", tier = AppTheme.colors.levels.tier(2)) }
                    } else {
                        null
                    },
            )
        },
        controls = {
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
        },
    )
}

@Composable
internal fun EmptyStatePage() {
    var showMessage by remember { mutableStateOf(true) }
    var showAction by remember { mutableStateOf(false) }

    SamplePage(
        preview = {
            EmptyState(
                title = "No projects",
                message = if (showMessage) "Create your first project to get started." else null,
                action = if (showAction) ({ TextButton(text = "Browse all", onClick = {}) }) else null,
            )
        },
        controls = {
            ControlSwitch(label = "Message", checked = showMessage, onCheckedChange = { showMessage = it })
            ControlSwitch(label = "Action", checked = showAction, onCheckedChange = { showAction = it })
        },
    )
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

    SamplePage(
        preview = {
            ErrorState(
                title = "Could not load",
                message = "Check your connection and retry.",
                action = if (showAction) ({ TextButton(text = "Retry", onClick = {}) }) else null,
            )
        },
        controls = {
            ControlSwitch(label = "Action", checked = showAction, onCheckedChange = { showAction = it })
        },
    )
}

/** Items appended per simulated page load in [LazyListPage]. */
private const val LAZY_LIST_PAGE_SIZE = 10

/** Total items available before [LazyListPage] reports there is nothing more to load. */
private const val LAZY_LIST_TOTAL_ITEMS = 40

/** Simulated network latency for one page load in [LazyListPage]. */
private const val LAZY_LIST_LOAD_DELAY_MILLIS = 1_200L

@Composable
internal fun LazyListPage() {
    var itemCount by remember { mutableIntStateOf(LAZY_LIST_PAGE_SIZE) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var paginate by remember { mutableStateOf(true) }
    val hasMore = paginate && itemCount < LAZY_LIST_TOTAL_ITEMS

    // Simulates a repository call: each LoadMoreFooter trigger appends one page.
    LaunchedEffect(isLoadingMore) {
        if (isLoadingMore) {
            delay(LAZY_LIST_LOAD_DELAY_MILLIS)
            itemCount = (itemCount + LAZY_LIST_PAGE_SIZE).coerceAtMost(LAZY_LIST_TOTAL_ITEMS)
            isLoadingMore = false
        }
    }

    SamplePage(
        preview = {
            // Lazy lists need a bounded height inside the scrolling detail pane.
            LazyList(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                items(items = List(itemCount) { it }, key = { it }) { index ->
                    ListItem(headline = "Item ${index + 1}", supportingText = "Supporting text")
                }
                if (hasMore) {
                    item(key = "load-more") {
                        LoadMoreFooter(
                            isLoadingMore = isLoadingMore,
                            onLoadMore = { if (!isLoadingMore) isLoadingMore = true },
                        )
                    }
                }
            }
            Text(text = "Loaded $itemCount of $LAZY_LIST_TOTAL_ITEMS")
        },
        controls = {
            ControlSwitch(label = "Load more on scroll", checked = paginate, onCheckedChange = { paginate = it })
            TextButton(
                text = "Reset",
                onClick = {
                    itemCount = LAZY_LIST_PAGE_SIZE
                    isLoadingMore = false
                },
            )
        },
    )
}

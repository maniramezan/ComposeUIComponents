package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

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

@Composable
internal fun InfiniteLazyListPage() {
    val pageSizes = listOf(10, 20, 40)
    var pageSizeIndex by remember { mutableIntStateOf(1) }
    var latencyMillis by remember { mutableFloatStateOf(800f) }
    var maxPages by remember { mutableIntStateOf(4) }
    var loadedPages by remember { mutableIntStateOf(1) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var loadRequests by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val pageSize = pageSizes[pageSizeIndex]
    val hasMore = loadedPages < maxPages

    SamplePage(
        preview = {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
                Text("Loaded ${loadedPages * pageSize} rows · footer requests: $loadRequests")
                LazyList(modifier = Modifier.height(SAMPLE_LIST_HEIGHT)) {
                    items(count = loadedPages * pageSize, key = { it }) { index ->
                        ListItem(headline = "Row ${index + 1}")
                    }
                    if (hasMore) {
                        item(key = "load-more") {
                            LoadMoreFooter(
                                isLoadingMore = isLoadingMore,
                                onLoadMore = {
                                    loadRequests++
                                    if (!isLoadingMore) {
                                        isLoadingMore = true
                                        scope.launch {
                                            delay(latencyMillis.roundToLong())
                                            loadedPages++
                                            isLoadingMore = false
                                        }
                                    }
                                },
                            )
                        }
                    }
                }
            }
        },
        controls = {
            ControlSegmented(
                label = "Page size",
                options = pageSizes.map { it.toString() },
                selectedIndex = pageSizeIndex,
                onOptionSelected = {
                    pageSizeIndex = it
                    loadedPages = 1
                },
            )
            ControlSegmented(
                label = "Total pages",
                options = listOf("2", "4", "8"),
                selectedIndex = listOf(2, 4, 8).indexOf(maxPages).coerceAtLeast(0),
                onOptionSelected = { maxPages = listOf(2, 4, 8)[it] },
            )
            ControlSlider(
                label = "Load latency: ${latencyMillis.roundToLong()}ms",
                value = latencyMillis,
                onValueChange = { latencyMillis = it },
                valueRange = 0f..3000f,
            )
            TextButton(
                text = "Reset",
                onClick = {
                    loadedPages = 1
                    loadRequests = 0
                },
            )
        },
    )
}

private val SAMPLE_LIST_HEIGHT = 320.dp

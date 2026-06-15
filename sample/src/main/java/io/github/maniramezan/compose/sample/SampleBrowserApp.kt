package io.github.maniramezan.compose.sample

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.AppText
import io.github.maniramezan.compose.components.AppTextStyle
import io.github.maniramezan.compose.components.EmptyState
import io.github.maniramezan.compose.components.SearchField
import io.github.maniramezan.compose.components.TopAppBar
import io.github.maniramezan.compose.theme.AppTheme

/** Width threshold (dp) above which the expanded split-pane layout is shown. */
private val SPLIT_BREAKPOINT = 840.dp

/** Fixed width of the list pane in split mode. */
private val LIST_PANE_WIDTH = 280.dp

/**
 * Root of the sample component browser.
 *
 * Renders an adaptive layout:
 * - Compact  (< 840 dp): list pane first; tapping an item pushes the detail
 *   pane; system/back button returns to the list.
 * - Expanded (≥ 840 dp): permanent side-by-side split; detail updates in place.
 */
@Composable
internal fun SampleBrowserApp() {
    val demos = remember { sampleDemos() }

    var query by rememberSaveable { mutableStateOf("") }
    val grouped =
        remember(demos, query) {
            demos.matching(query).groupedByCategory()
        }

    var selectedId by rememberSaveable { mutableStateOf(demos.first().id) }
    var compactDetailVisible by rememberSaveable { mutableStateOf(false) }

    val selectedDemo =
        remember(selectedId, demos) {
            demos.firstOrNull { it.id == selectedId } ?: demos.first()
        }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        if (maxWidth >= SPLIT_BREAKPOINT) {
            // ── Expanded: permanent split layout ────────────────────────────
            Row(modifier = Modifier.fillMaxSize()) {
                SampleListPane(
                    grouped = grouped,
                    selectedId = selectedId,
                    onSelect = { selectedId = it },
                    query = query,
                    onQueryChange = { query = it },
                    modifier =
                        Modifier
                            .width(LIST_PANE_WIDTH)
                            .fillMaxHeight(),
                )
                VerticalDivider()
                SampleDetailPane(
                    demo = selectedDemo,
                    showBackButton = false,
                    onBack = {},
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                )
            }
        } else {
            // ── Compact: list → detail navigation ───────────────────────────
            BackHandler(enabled = compactDetailVisible) {
                compactDetailVisible = false
            }

            if (compactDetailVisible) {
                SampleDetailPane(
                    demo = selectedDemo,
                    showBackButton = true,
                    onBack = { compactDetailVisible = false },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                SampleListPane(
                    grouped = grouped,
                    selectedId = selectedId,
                    onSelect = { id ->
                        selectedId = id
                        compactDetailVisible = true
                    },
                    query = query,
                    onQueryChange = { query = it },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// List pane
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SampleListPane(
    grouped: Map<String, List<SampleComponentDemo>>,
    selectedId: String,
    onSelect: (String) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = "Components") },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
        ) {
            SearchField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = "Search components",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                    )
                },
                modifier =
                    Modifier.padding(
                        horizontal = AppTheme.spacing.lg,
                        vertical = AppTheme.spacing.sm,
                    ),
            )
            if (grouped.isEmpty()) {
                EmptyState(
                    title = "No components found",
                    message = "No component matches \"$query\".",
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(AppTheme.spacing.lg),
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    grouped.forEach { (category, entries) ->
                        item(key = "header-$category") {
                            CategoryHeader(category = category)
                        }
                        items(entries, key = { it.id }) { demo ->
                            DemoListRow(
                                demo = demo,
                                isSelected = demo.id == selectedId,
                                onClick = { onSelect(demo.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(category: String) {
    Column {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.surfaceContainer)
                    .padding(
                        horizontal = AppTheme.spacing.lg,
                        vertical = AppTheme.spacing.xs,
                    ),
        ) {
            AppText(text = category, style = AppTextStyle.Label)
        }
        HorizontalDivider()
    }
}

@Composable
private fun DemoListRow(
    demo: SampleComponentDemo,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) AppTheme.colors.primaryContainer else AppTheme.colors.surface
    val textColor = if (isSelected) AppTheme.colors.onPrimaryContainer else AppTheme.colors.onSurface
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Selection accent bar
        Box(
            modifier =
                Modifier
                    .width(3.dp)
                    .padding(vertical = 2.dp)
                    .then(
                        if (isSelected) {
                            Modifier
                                .background(AppTheme.colors.primary)
                                .padding(vertical = AppTheme.spacing.md)
                        } else {
                            Modifier.padding(vertical = AppTheme.spacing.md)
                        },
                    ),
        )
        Text(
            text = demo.title,
            color = textColor,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(
                        horizontal = AppTheme.spacing.md,
                        vertical = AppTheme.spacing.md,
                    ),
        )
    }
    HorizontalDivider(modifier = Modifier.padding(start = AppTheme.spacing.lg))
}

// ─────────────────────────────────────────────────────────────────────────────
// Detail pane
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SampleDetailPane(
    demo: SampleComponentDemo,
    showBackButton: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = demo.title,
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
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
            ) {
                // Description header
                AppText(
                    text = demo.description,
                    style = AppTextStyle.Body,
                )

                // Demo surface
                Box(
                    modifier =
                        Modifier
                            .padding(top = AppTheme.spacing.x2)
                            .fillMaxWidth()
                            .clip(AppTheme.shapes.large)
                            .background(AppTheme.colors.surfaceContainer)
                            .padding(AppTheme.spacing.lg),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                        AppText(text = "Demo", style = AppTextStyle.Label)
                        demo.content()
                    }
                }
            }
        }
    }
}

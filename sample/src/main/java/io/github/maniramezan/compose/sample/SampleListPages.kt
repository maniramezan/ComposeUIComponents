package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.ContentRow
import io.github.maniramezan.compose.components.EmptyState
import io.github.maniramezan.compose.components.ErrorState
import io.github.maniramezan.compose.components.ListItem
import io.github.maniramezan.compose.components.LoadingState
import io.github.maniramezan.compose.components.PillChip
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.theme.AppTheme

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

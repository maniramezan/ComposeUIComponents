package io.github.maniramezan.compose.sample

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.Card
import io.github.maniramezan.compose.components.DisclosureCard
import io.github.maniramezan.compose.components.FlipAxis
import io.github.maniramezan.compose.components.FlipCard
import io.github.maniramezan.compose.components.OverlayCard
import io.github.maniramezan.compose.components.PrimaryButton
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.ShowcaseFeed
import io.github.maniramezan.compose.components.ShowcaseItemWidth
import io.github.maniramezan.compose.components.Snackbar
import io.github.maniramezan.compose.components.Toast
import io.github.maniramezan.compose.components.ToastDuration
import io.github.maniramezan.compose.components.ToastHost
import io.github.maniramezan.compose.components.ToastPosition
import io.github.maniramezan.compose.components.rememberToastHostState
import io.github.maniramezan.compose.theme.AppTheme
import kotlinx.coroutines.launch

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
internal fun DisclosureCardPage() {
    var expanded by remember { mutableStateOf(false) }
    var longDetail by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        DisclosureCard(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            expandedStateDescription = "Expanded",
            collapsedStateDescription = "Collapsed",
            summary = { Text(text = "Section title") },
            detail = {
                Text(
                    text =
                        if (longDetail) {
                            "Additional information can span multiple lines to validate wrapping and dynamic height."
                        } else {
                            "Additional information"
                        },
                )
            },
        )
        ControlsDivider()
        ControlSwitch(label = "Expanded", checked = expanded, onCheckedChange = { expanded = it })
        ControlSwitch(label = "Long detail", checked = longDetail, onCheckedChange = { longDetail = it })
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
    val speedOptions = listOf("Slow", "Normal", "Fast")
    var speedIndex by remember { mutableIntStateOf(1) }
    val durationMillis =
        when (speedIndex) {
            0 -> 1200
            2 -> 300
            else -> AppTheme.motion.mediumMillis
        }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        FlipCard(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            flipped = flipped,
            onFlippedChange = { flipped = it },
            axis = axis,
            animationSpec = tween(durationMillis = durationMillis, easing = AppTheme.motion.emphasizedEasing),
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
        ControlSegmented(
            label = "Speed",
            options = speedOptions,
            selectedIndex = speedIndex,
            onOptionSelected = { speedIndex = it },
        )
    }
}

@Composable
internal fun ShowcaseFeedPage() {
    var peek by remember { mutableFloatStateOf(0.85f) }
    val rowOptions = listOf("1 row", "2 rows")
    var rowsIndex by remember { mutableIntStateOf(0) }
    var showAction by remember { mutableStateOf(true) }
    val rows = rowsIndex + 1

    val topApps = listOf("Focus", "Sky Notes", "Trailhead", "Loop", "Pixel Paint")
    val games = listOf("Nova Run", "Blocks!", "Chess+", "Dungeon", "Kart Rally")

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        ShowcaseFeed(modifier = Modifier.fillMaxWidth().height(360.dp)) {
            section(
                title = "Top Apps",
                items = topApps,
                actionLabel = if (showAction) "See all" else null,
                onAction = if (showAction) ({}) else null,
                itemWidth = ShowcaseItemWidth.Peek(peek),
                rows = rows,
                rowHeight = if (rows > 1) 64.dp else null,
                rowContentDescription = "Top apps, horizontally scrollable",
            ) { app -> ShowcaseDemoCard(label = app, fillHeight = rows > 1) }

            section(
                title = "New Games",
                items = games,
                itemWidth = ShowcaseItemWidth.Peek(peek),
                rows = rows,
                rowHeight = if (rows > 1) 64.dp else null,
            ) { game -> ShowcaseDemoCard(label = game, fillHeight = rows > 1) }

            customSection {
                ShowcaseDemoBanner(label = "Editor's Choice")
            }
        }
        ControlsDivider()
        ControlSlider(
            label = "Peek fraction",
            value = peek,
            onValueChange = { peek = it },
            valueRange = 0.5f..1f,
        )
        ControlSegmented(
            label = "Rows",
            options = rowOptions,
            selectedIndex = rowsIndex,
            onOptionSelected = { rowsIndex = it },
        )
        ControlSwitch(
            label = "Show \"See all\" action",
            checked = showAction,
            onCheckedChange = { showAction = it },
        )
    }
}

@Composable
private fun ShowcaseDemoCard(
    label: String,
    fillHeight: Boolean,
) {
    val sizeModifier =
        if (fillHeight) {
            Modifier.fillMaxWidth().fillMaxHeight()
        } else {
            Modifier.fillMaxWidth().height(120.dp)
        }
    Box(
        modifier =
            sizeModifier
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.titleSmall,
            color = AppTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun ShowcaseDemoBanner(label: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.md)
                .height(96.dp)
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.onPrimaryContainer,
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

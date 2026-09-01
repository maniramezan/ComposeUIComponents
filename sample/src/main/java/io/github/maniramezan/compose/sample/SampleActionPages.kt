package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.ActionPill
import io.github.maniramezan.compose.components.FAB
import io.github.maniramezan.compose.components.IconButton
import io.github.maniramezan.compose.components.PrimaryButton
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.SegmentDensity
import io.github.maniramezan.compose.components.SegmentedControl
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Actions
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun PrimaryButtonPage() {
    var enabled by remember { mutableStateOf(true) }

    SamplePage(
        preview = {
            PrimaryButton(text = "Primary Button", onClick = {}, enabled = enabled)
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        },
    )
}

@Composable
internal fun ActionPillPage() {
    var showValue by remember { mutableStateOf(true) }
    var prominent by remember { mutableStateOf(false) }
    var compact by remember { mutableStateOf(false) }
    var tapCount by remember { mutableIntStateOf(0) }

    SamplePage(
        preview = {
            ActionPill(
                onClick = { tapCount += 1 },
                containerColor = if (prominent) AppTheme.colors.primary else AppTheme.colors.surfaceVariant,
                contentColor = if (prominent) AppTheme.colors.onPrimary else AppTheme.colors.onSurfaceVariant,
                contentPadding =
                    PaddingValues(
                        horizontal = if (compact) AppTheme.spacing.x1 else AppTheme.spacing.x1_5,
                        vertical = if (compact) AppTheme.spacing.half else AppTheme.spacing.x1,
                    ),
            ) {
                Text(text = "Status")
                if (showValue) Text(text = "Active")
            }
            Text(text = "Tap count: $tapCount")
        },
        controls = {
            ControlSwitch(label = "Show value", checked = showValue, onCheckedChange = { showValue = it })
            ControlSwitch(label = "Prominent colors", checked = prominent, onCheckedChange = { prominent = it })
            ControlSwitch(label = "Compact padding", checked = compact, onCheckedChange = { compact = it })
        },
    )
}

@Composable
internal fun SecondaryButtonPage() {
    var enabled by remember { mutableStateOf(true) }

    SamplePage(
        preview = {
            SecondaryButton(text = "Secondary Button", onClick = {}, enabled = enabled)
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        },
    )
}

@Composable
internal fun TextButtonPage() {
    var enabled by remember { mutableStateOf(true) }

    SamplePage(
        preview = {
            TextButton(text = "Text Button", onClick = {}, enabled = enabled)
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        },
    )
}

@Composable
internal fun IconButtonPage() {
    var enabled by remember { mutableStateOf(true) }
    val iconOptions = listOf("Check", "Close")
    var iconIndex by remember { mutableIntStateOf(0) }
    val icon = if (iconIndex == 0) AppTheme.icons.check else AppTheme.icons.close

    SamplePage(
        preview = {
            IconButton(icon = icon, contentDescription = iconOptions[iconIndex], onClick = {}, enabled = enabled)
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ControlSegmented(
                label = "Icon",
                options = iconOptions,
                selectedIndex = iconIndex,
                onOptionSelected = { iconIndex = it },
            )
        },
    )
}

@Composable
internal fun FabPage() {
    val iconOptions = listOf("Check", "Close")
    var iconIndex by remember { mutableIntStateOf(0) }
    val icon = if (iconIndex == 0) AppTheme.icons.check else AppTheme.icons.close

    SamplePage(
        preview = {
            FAB(icon = icon, contentDescription = iconOptions[iconIndex], onClick = {})
        },
        controls = {
            ControlSegmented(
                label = "Icon",
                options = iconOptions,
                selectedIndex = iconIndex,
                onOptionSelected = { iconIndex = it },
            )
        },
    )
}

@Composable
internal fun SegmentedControlPage() {
    var selected by remember { mutableIntStateOf(0) }
    var enabled by remember { mutableStateOf(true) }
    val options = listOf("Free", "Plus", "Pro")
    val densityOptions = listOf("Regular", "Compact")
    var densityIndex by remember { mutableIntStateOf(0) }
    val density = if (densityIndex == 0) SegmentDensity.Regular else SegmentDensity.Compact

    SamplePage(
        preview = {
            SegmentedControl(
                options = options,
                selectedIndex = selected,
                onOptionSelected = { selected = it },
                enabled = enabled,
                density = density,
            )
            Text(text = "Selected: ${options[selected]}")
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ControlSegmented(
                label = "Density",
                options = densityOptions,
                selectedIndex = densityIndex,
                onOptionSelected = { densityIndex = it },
            )
        },
    )
}

package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val densityOptions = listOf("Regular", "Compact")
    var densityIndex by remember { mutableIntStateOf(0) }
    val density = if (densityIndex == 0) SegmentDensity.Regular else SegmentDensity.Compact

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SegmentedControl(
            options = options,
            selectedIndex = selected,
            onOptionSelected = { selected = it },
            enabled = enabled,
            density = density,
        )
        Text(text = "Selected: ${options[selected]}")
        ControlsDivider()
        ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        ControlSegmented(
            label = "Density",
            options = densityOptions,
            selectedIndex = densityIndex,
            onOptionSelected = { densityIndex = it },
        )
    }
}

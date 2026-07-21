package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.components.AppText
import io.github.maniramezan.compose.components.AppTextStyle
import io.github.maniramezan.compose.components.SegmentedControl
import io.github.maniramezan.compose.components.Slider
import io.github.maniramezan.compose.components.Switch
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Shared helpers
// ─────────────────────────────────────────────────────────────────────────────

/** Horizontal divider between the live preview and the controls panel. */
@Composable
internal fun ControlsDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = AppTheme.spacing.sm))
    AppText(text = "Controls", style = AppTextStyle.Label)
}

/** A labeled toggle row used throughout the controls panels. */
@Composable
internal fun ControlSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Switch(checked = checked, onCheckedChange = onCheckedChange, label = label)
}

/** A labeled segmented-control row used throughout the controls panels. */
@Composable
internal fun ControlSegmented(
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

/** A labeled slider row used throughout the controls panels. */
@Composable
internal fun ControlSlider(
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

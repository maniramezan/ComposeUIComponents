package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.components.LabeledDivider
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.theme.AppTheme

@Composable
internal fun LabeledDividerPage() {
    var label by rememberSaveable { mutableStateOf("Section") }
    var thicknessIndex by rememberSaveable { mutableIntStateOf(0) }
    var colorIndex by rememberSaveable { mutableIntStateOf(0) }
    var narrow by rememberSaveable { mutableStateOf(false) }
    val thicknesses = listOf(AppTheme.spacing.strokeThin, AppTheme.spacing.strokeRegular)
    val colors = listOf(AppTheme.colors.outlineVariant, AppTheme.colors.primary)

    SamplePage(
        preview = {
            LabeledDivider(
                label = label,
                thickness = thicknesses[thicknessIndex],
                color = colors[colorIndex],
                modifier =
                    if (narrow) {
                        Modifier.widthIn(max = AppTheme.spacing.maxContentWidthForm / 2)
                    } else {
                        Modifier.fillMaxWidth()
                    },
            )
        },
        controls = {
            TextField(value = label, onValueChange = { label = it }, label = "Label (blank draws one rule)")
            ControlSegmented(
                label = "Thickness",
                options = listOf("Thin", "Regular"),
                selectedIndex = thicknessIndex,
                onOptionSelected = { thicknessIndex = it },
            )
            ControlSegmented(
                label = "Rule color",
                options = listOf("Outline", "Primary"),
                selectedIndex = colorIndex,
                onOptionSelected = { colorIndex = it },
            )
            ControlSwitch(label = "Narrow width", checked = narrow, onCheckedChange = { narrow = it })
        },
    )
}

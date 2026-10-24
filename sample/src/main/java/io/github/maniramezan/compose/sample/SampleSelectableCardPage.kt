package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.SelectableCard
import io.github.maniramezan.compose.theme.AppTheme

private data class SamplePlan(
    val name: String,
    val detail: String,
)

private val SAMPLE_PLANS =
    listOf(
        SamplePlan(name = "Monthly", detail = "Billed every month"),
        SamplePlan(name = "Yearly", detail = "Billed once a year"),
        SamplePlan(name = "Lifetime", detail = "Billed once, never renews"),
    )
private val SHAPE_OPTIONS = listOf("Small", "Standard", "Large")
private val PADDING_OPTIONS = listOf("Compact", "Regular", "Spacious")
private val CONTAINER_OPTIONS = listOf("Variant", "Surface", "Primary")

@Composable
internal fun SelectableCardPage() {
    var selectedIndices by remember { mutableStateOf(setOf(0)) }
    var enabled by remember { mutableStateOf(true) }
    var lastCardDisabled by remember { mutableStateOf(false) }
    var multiSelect by remember { mutableStateOf(false) }
    var stateDescriptionOn by remember { mutableStateOf(false) }
    var longContent by remember { mutableStateOf(false) }
    var shapeIndex by remember { mutableIntStateOf(2) }
    var paddingIndex by remember { mutableIntStateOf(1) }
    var containerIndex by remember { mutableIntStateOf(0) }

    val shape =
        when (shapeIndex) {
            0 -> AppTheme.shapes.small
            1 -> AppTheme.shapes.standard
            else -> AppTheme.shapes.large
        }
    val contentPadding =
        PaddingValues(
            when (paddingIndex) {
                0 -> AppTheme.spacing.sm
                2 -> AppTheme.spacing.x4
                else -> AppTheme.spacing.lg
            },
        )
    val containerColor =
        when (containerIndex) {
            1 -> AppTheme.colors.surface
            2 -> AppTheme.colors.primaryContainer
            else -> AppTheme.colors.surfaceVariant
        }
    val contentColor =
        when (containerIndex) {
            1 -> AppTheme.colors.onSurface
            2 -> AppTheme.colors.onPrimaryContainer
            else -> AppTheme.colors.onSurfaceVariant
        }

    SamplePage(
        preview = {
            Column(
                modifier = Modifier.fillMaxWidth().selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            ) {
                SAMPLE_PLANS.forEachIndexed { index, plan ->
                    val isSelected = index in selectedIndices
                    SelectableCard(
                        selected = isSelected,
                        onClick = {
                            selectedIndices =
                                when {
                                    !multiSelect -> setOf(index)
                                    isSelected -> selectedIndices - index
                                    else -> selectedIndices + index
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled && !(lastCardDisabled && index == SAMPLE_PLANS.lastIndex),
                        stateDescription = if (stateDescriptionOn && isSelected) "Current choice" else null,
                        role = if (multiSelect) Role.Checkbox else Role.RadioButton,
                        shape = shape,
                        containerColor = containerColor,
                        contentColor = contentColor,
                        contentPadding = contentPadding,
                    ) {
                        Text(text = plan.name, style = AppTheme.typography.titleSmall)
                        Text(text = plan.detail)
                        if (longContent) {
                            Text(
                                text = "Includes every feature, priority support, and early access to new releases.",
                            )
                        }
                    }
                }
            }
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ControlSwitch(
                label = "Disable last card only",
                checked = lastCardDisabled,
                onCheckedChange = { lastCardDisabled = it },
            )
            ControlSwitch(
                label = "Multi-select (checkbox role)",
                checked = multiSelect,
                onCheckedChange = {
                    multiSelect = it
                    selectedIndices = selectedIndices.take(1).toSet()
                },
            )
            ControlSwitch(
                label = "State description on selected",
                checked = stateDescriptionOn,
                onCheckedChange = { stateDescriptionOn = it },
            )
            ControlSwitch(label = "Long content", checked = longContent, onCheckedChange = { longContent = it })
            ControlSegmented(
                label = "Shape",
                options = SHAPE_OPTIONS,
                selectedIndex = shapeIndex,
                onOptionSelected = { shapeIndex = it },
            )
            ControlSegmented(
                label = "Content padding",
                options = PADDING_OPTIONS,
                selectedIndex = paddingIndex,
                onOptionSelected = { paddingIndex = it },
            )
            ControlSegmented(
                label = "Container",
                options = CONTAINER_OPTIONS,
                selectedIndex = containerIndex,
                onOptionSelected = { containerIndex = it },
            )
            SecondaryButton(
                text = "Clear selection",
                onClick = { selectedIndices = emptySet() },
                modifier = Modifier.fillMaxWidth(),
            )
        },
    )
}

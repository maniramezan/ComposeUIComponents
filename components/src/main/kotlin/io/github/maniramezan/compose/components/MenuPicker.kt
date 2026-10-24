package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight

/**
 * A themed single-choice dropdown picker built on Material 3's exposed dropdown menu.
 *
 * The field is read-only: tapping it opens a menu of [options] and choosing one calls
 * [onOptionSelected] and closes the menu. Selection is state-hoisted; the picker never
 * changes [selectedOption] on its own, so a caller may veto a choice by ignoring it.
 *
 * Every string is caller-supplied (ADR 0004). [optionLabel] produces the visible text for
 * an option and for the field's current value. A blank [label], [placeholder] or
 * [supportingText] renders no node. The [placeholder] is shown while nothing is selected
 * and the field is focused or has no [label].
 *
 * Accessibility: the field exposes [Role.DropdownList] together with Material's expanded and
 * collapsed state, and announces its current value. Pass [stateDescription] to announce a
 * richer localized description of the current choice instead (for example
 * `"Size: Medium, 2 of 3"`). The currently selected menu item exposes `selected` semantics
 * and is also highlighted. When [isError] is true and [supportingText] is not blank, the
 * supporting text is exposed as the field's error message. Both the field and the menu
 * items meet the 48dp minimum touch target.
 *
 * @param options The choices to show in the menu, in display order.
 * @param selectedOption The current choice, or `null` when nothing is selected yet.
 * @param onOptionSelected Called with the tapped option; the menu closes afterwards.
 * @param optionLabel Maps an option to its localized, user-visible text.
 * @param label Floating label for the field; blank renders none.
 * @param placeholder Hint shown while nothing is selected; blank renders none.
 * @param supportingText Helper or error text below the field; null or blank renders none.
 * @param enabled When false the field cannot be focused and the menu cannot open.
 * @param isError Renders the field in its error state.
 * @param stateDescription Optional localized state announcement for the field.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun <T> MenuPicker(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    supportingText: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    stateDescription: String? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val visibleSupportingText = supportingText?.takeIf { it.isNotBlank() }
    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { requested -> expanded = requested && enabled },
        modifier = modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = selectedOption?.let(optionLabel).orEmpty(),
            onValueChange = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled)
                    .minimumTouchTargetHeight(minimumTouchTargetSize())
                    .semantics {
                        role = Role.DropdownList
                        stateDescription?.takeIf { it.isNotBlank() }?.let { this.stateDescription = it }
                        if (isError && visibleSupportingText != null) error(visibleSupportingText)
                    },
            enabled = enabled,
            readOnly = true,
            isError = isError,
            label = label.takeIf { it.isNotBlank() }?.let { text -> { Text(text = text) } },
            placeholder = placeholder.takeIf { it.isNotBlank() }?.let { text -> { Text(text = text) } },
            supportingText = visibleSupportingText?.let { text -> { Text(text = text) } },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded && enabled) },
            singleLine = true,
        )
        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                DropdownMenuItem(
                    text = {
                        Text(
                            text = optionLabel(option),
                            color = if (isSelected) AppTheme.colors.onPrimaryContainer else Color.Unspecified,
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    modifier =
                        Modifier
                            .minimumTouchTargetHeight(minimumTouchTargetSize())
                            .background(if (isSelected) AppTheme.colors.primaryContainer else Color.Transparent)
                            .semantics { selected = isSelected },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

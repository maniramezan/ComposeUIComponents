package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight
import androidx.compose.material3.Checkbox as MaterialCheckbox
import androidx.compose.material3.Slider as MaterialSlider
import androidx.compose.material3.Switch as MaterialSwitch

/** A themed, single-line outlined text field with a floating [label]. */
@Composable
public fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        isError = isError,
        label = { Text(text = label) },
        supportingText = supportingText?.let { { Text(text = it) } },
        singleLine = true,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    )
}

/**
 * A themed, single-line outlined text field that masks its value by default.
 *
 * @param revealPassword Whether to show plaintext instead of masking; wire this to a
 *   caller-owned "show password" toggle (e.g. via [trailingIcon]).
 */
@Composable
public fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    revealPassword: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        isError = isError,
        label = { Text(text = label) },
        supportingText = supportingText?.let { { Text(text = it) } },
        singleLine = true,
        visualTransformation = if (revealPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = trailingIcon,
    )
}

/** A themed, single-line outlined text field styled for search, with an optional [placeholder]. */
@Composable
public fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        placeholder = { Text(text = placeholder) },
        leadingIcon = leadingIcon,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

/** A themed checkbox row with a tappable [label]; the whole row toggles [checked]. */
@Composable
public fun Checkbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier =
            modifier
                .minimumTouchTargetHeight(minimumTouchTargetSize())
                .toggleable(
                    value = checked,
                    enabled = enabled,
                    role = Role.Checkbox,
                    onValueChange = onCheckedChange,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        MaterialCheckbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
        )
        Text(text = label)
    }
}

/** A themed single-choice radio button list; each row's full width is tappable. */
@Composable
public fun RadioGroup(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(modifier = modifier) {
        options.forEachIndexed { index, option ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .minimumTouchTargetHeight(minimumTouchTargetSize())
                        .selectable(
                            selected = selectedIndex == index,
                            enabled = enabled,
                            onClick = { onOptionSelected(index) },
                        ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            ) {
                RadioButton(
                    selected = selectedIndex == index,
                    onClick = null,
                    enabled = enabled,
                )
                Text(text = option)
            }
        }
    }
}

/** A themed switch row with a tappable [label]; the whole row toggles [checked]. */
@Composable
public fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier =
            modifier
                .minimumTouchTargetHeight(minimumTouchTargetSize())
                .toggleable(
                    value = checked,
                    enabled = enabled,
                    role = Role.Switch,
                    onValueChange = onCheckedChange,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        MaterialSwitch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            thumbContent = thumbContent,
        )
        Text(text = label)
    }
}

/**
 * A single-value slider.
 *
 * A bare slider only announces a raw percentage to screen readers. Pass [label]
 * to describe what the slider controls and [valueDescription] to announce a
 * meaningful current value (e.g. `"Volume, 50%"`); both should be localized by
 * the caller. When omitted, the platform default value announcement is used.
 */
@Composable
public fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    label: String? = null,
    valueDescription: String? = null,
) {
    MaterialSlider(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .fillMaxWidth()
                .semantics {
                    label?.let { contentDescription = it }
                    valueDescription?.let { stateDescription = it }
                },
        valueRange = valueRange,
        enabled = enabled,
    )
}

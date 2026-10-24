package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

private val PreviewOptions = listOf("Small", "Medium", "Large")

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Menu Picker", group = "Inputs")
@Composable
public fun MenuPickerPreview(): Unit = AppTheme { MenuPickerPreviewContent() }

/** The preview rows without a theme, so callers can supply their own [AppTheme]. */
@Composable
internal fun MenuPickerPreviewContent() {
    Column(
        modifier = Modifier.padding(AppTheme.spacing.x2),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
    ) {
        MenuPickerPreviewRow(selected = "Medium", label = "Size")
        MenuPickerPreviewRow(selected = null, label = "Size", placeholder = "Choose a size")
        MenuPickerPreviewRow(selected = "Medium", label = "Size", supportingText = "Applies to new items")
        MenuPickerPreviewRow(selected = null, label = "Size", supportingText = "Choose a size", isError = true)
        MenuPickerPreviewRow(selected = "Large", label = "Size", enabled = false)
    }
}

@Composable
private fun MenuPickerPreviewRow(
    selected: String?,
    label: String,
    placeholder: String = "",
    supportingText: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    MenuPicker(
        options = PreviewOptions,
        selectedOption = selected,
        onOptionSelected = {},
        optionLabel = { it },
        label = label,
        placeholder = placeholder,
        supportingText = supportingText,
        enabled = enabled,
        isError = isError,
    )
}

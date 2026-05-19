package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Input Components", group = "Inputs")
@Composable
public fun InputComponentsPreview(): Unit =
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            TextField(
                value = "Mani",
                onValueChange = {},
                label = "Name",
            )
            PasswordField(
                value = "demo-password",
                onValueChange = {},
                label = "Password",
            )
            SearchField(
                value = "compose",
                onValueChange = {},
            )
            Checkbox(
                checked = true,
                onCheckedChange = {},
                label = "Email updates",
            )
            RadioGroup(
                options = listOf("Small", "Medium", "Large"),
                selectedIndex = 1,
                onOptionSelected = {},
            )
            Switch(
                checked = true,
                onCheckedChange = {},
                label = "Notifications",
            )
            Slider(
                value = 0.65f,
                onValueChange = {},
            )
        }
    }

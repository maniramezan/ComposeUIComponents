package io.github.maniramezan.compose.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.PasswordField
import io.github.maniramezan.compose.components.SearchField
import io.github.maniramezan.compose.components.TextField

// ─────────────────────────────────────────────────────────────────────────────
// Inputs
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun TextFieldPage() {
    var value by remember { mutableStateOf("Mani") }
    var enabled by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var showSupporting by remember { mutableStateOf(false) }

    SamplePage(
        preview = {
            TextField(
                value = value,
                onValueChange = { value = it },
                label = "Name",
                enabled = enabled,
                isError = isError,
                supportingText = if (showSupporting) "Supporting text" else null,
            )
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ControlSwitch(label = "Error state", checked = isError, onCheckedChange = { isError = it })
            ControlSwitch(
                label = "Supporting text",
                checked = showSupporting,
                onCheckedChange = { showSupporting = it },
            )
        },
    )
}

@Composable
internal fun PasswordFieldPage() {
    var value by remember { mutableStateOf("secret123") }
    var revealed by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }

    SamplePage(
        preview = {
            PasswordField(
                value = value,
                onValueChange = { value = it },
                label = "Password",
                enabled = enabled,
                revealPassword = revealed,
            )
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ControlSwitch(label = "Reveal password", checked = revealed, onCheckedChange = { revealed = it })
        },
    )
}

@Composable
internal fun SearchFieldPage() {
    var query by remember { mutableStateOf("") }
    var enabled by remember { mutableStateOf(true) }

    SamplePage(
        preview = {
            SearchField(value = query, onValueChange = { query = it }, enabled = enabled)
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
        },
    )
}

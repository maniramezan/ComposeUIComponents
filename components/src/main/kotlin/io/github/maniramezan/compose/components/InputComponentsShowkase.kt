package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Inputs", group = "Inputs")
@Composable
public fun InputComponentsShowkase(): Unit = InputComponentsPreview()

@ShowkaseComposable(name = "Text Field", group = "Inputs")
@Composable
public fun TextFieldShowkase(): Unit =
    AppTheme {
        TextField(value = "Mani", onValueChange = {}, label = "Name")
    }

@ShowkaseComposable(name = "Password Field", group = "Inputs")
@Composable
public fun PasswordFieldShowkase(): Unit =
    AppTheme {
        PasswordField(value = "secret", onValueChange = {}, label = "Password")
    }

@ShowkaseComposable(name = "Search Field", group = "Inputs")
@Composable
public fun SearchFieldShowkase(): Unit =
    AppTheme {
        SearchField(value = "compose", onValueChange = {})
    }

@ShowkaseComposable(name = "Checkbox", group = "Inputs")
@Composable
public fun CheckboxShowkase(): Unit =
    AppTheme {
        Checkbox(checked = true, onCheckedChange = {}, label = "Email updates")
    }

@ShowkaseComposable(name = "Radio Group", group = "Inputs")
@Composable
public fun RadioGroupShowkase(): Unit =
    AppTheme {
        RadioGroup(
            options = listOf("Small", "Medium", "Large"),
            selectedIndex = 1,
            onOptionSelected = {},
        )
    }

@ShowkaseComposable(name = "Switch", group = "Inputs")
@Composable
public fun SwitchShowkase(): Unit =
    AppTheme {
        Switch(checked = true, onCheckedChange = {}, label = "Notifications")
    }

@ShowkaseComposable(name = "Slider", group = "Inputs")
@Composable
public fun SliderShowkase(): Unit =
    AppTheme {
        Slider(value = 0.65f, onValueChange = {})
    }

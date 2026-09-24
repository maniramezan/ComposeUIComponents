# Inputs

Input components are state-hoisted wrappers around Material 3 controls.

## Components

- `TextField`
- `PasswordField`
- `SearchField`
- `Checkbox`
- `RadioGroup`
- `Switch`
- `Slider`

![Input components](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/input-components.png)

## Example

```kotlin
AppTheme {
    TextField(
        value = state.name,
        onValueChange = onNameChanged,
        label = "Name",
        keyboardOptions = KeyboardOptions.Default,
    )
}
```

Callers own input state and validation. Use `supportingText` and `isError` for field-level feedback.
`TextField` and `PasswordField` accept caller-supplied `keyboardOptions`; password fields default
to `KeyboardType.Password`.

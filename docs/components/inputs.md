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
    )
}
```

Callers own input state and validation. Use `supportingText` and `isError` for field-level feedback.

## Selection controls

`Checkbox`, `RadioGroup`, and `Switch` make the whole row (control + label) the touch target,
with at least a 48dp height. `RadioGroup` rows expose the radio-button role inside a
selectable group, so TalkBack announces each option's position ("1 of 3"). When `enabled` is
`false`, labels dim with the control using Material's disabled-content opacity.

`SearchField` omits the placeholder entirely when `placeholder` is blank, so no empty text
node reaches the accessibility tree.

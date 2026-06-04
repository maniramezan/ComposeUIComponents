# Actions

Action components wrap Material 3 controls with AppTheme colors, spacing, and minimum touch targets.

## Components

- `PrimaryButton`
- `SecondaryButton`
- `TextButton`
- `IconButton`
- `FAB`
- `ExtendedFloatingActionButton`
- `SegmentedControl`
- `SingleChoiceSegmentedButtonRow`

![Actions components](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/actions-components.png)

## Example

```kotlin
AppTheme {
    SecondaryButton(
        text = "Back",
        onClick = ::goBack,
    )
}
```

Icon-based actions require a non-null `contentDescription` unless the icon is purely decorative.

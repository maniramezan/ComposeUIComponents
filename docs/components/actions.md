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
- `PillChip`

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

## PillChip

`PillChip` is a capsule-shaped tappable chip that toggles between a selected and
unselected visual state. Typical use cases are filter rows, level selectors, and
persistent multi-option toggles.

```kotlin
AppTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
        PillChip(label = "All", isSelected = true, onClick = {})
        PillChip(label = "Beginner", isSelected = false, onClick = {})
        PillChip(label = "Advanced", isSelected = false, onClick = {})
    }
}
```

Defaults pull colors from `AppTheme.colors` so the chip picks up the surrounding theme
automatically. Override `selectedBackground`, `unselectedBackground`, `selectedLabel`,
and `unselectedLabel` when a specific tint is needed — for example, per-level filter
capsules that reuse a `LevelPalette` tier.

### Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| `label` | `String` | Chip text. |
| `isSelected` | `Boolean` | Whether the chip renders in its selected state. |
| `onClick` | `() -> Unit` | Invoked on tap. |
| `modifier` | `Modifier` | Applied to the chip container. |
| `selectedBackground` | `Color` | Background when selected. Defaults to `AppTheme.colors.primary`. |
| `unselectedBackground` | `Color` | Background when unselected. Defaults to `AppTheme.colors.primaryContainer`. |
| `selectedLabel` | `Color` | Label color when selected. Defaults to `AppTheme.colors.onPrimary`. |
| `unselectedLabel` | `Color` | Label color when unselected. Defaults to `AppTheme.colors.onSurface`. |

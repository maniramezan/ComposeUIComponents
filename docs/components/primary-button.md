# PrimaryButton

`PrimaryButton` is the Phase 1 vertical-slice action component.

## Behavior

- Uses `AppTheme.colors.primary` and `AppTheme.colors.onPrimary`.
- Uses semantic spacing from `AppTheme.spacing`.
- Enforces a minimum 48dp touch target.
- Ships with Compose preview, Showkase entry, unit test, and Roborazzi smoke screenshot test.

## Example

```kotlin
AppTheme {
    PrimaryButton(
        text = "Continue",
        onClick = ::continueFlow,
    )
}
```

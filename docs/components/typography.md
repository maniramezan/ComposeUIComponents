# Typography

Typography components expose semantic text styles through `AppTheme.typography` and `AppText`.

## Components

- `AppText`
- `AppTextStyle`

## Example

```kotlin
AppTheme {
    AppText(
        text = "Dashboard",
        style = AppTextStyle.Title,
    )
}
```

Use semantic styles rather than ad-hoc font sizes in app UI.

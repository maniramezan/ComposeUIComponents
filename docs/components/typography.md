# Typography

Typography components expose semantic text styles through `AppTheme.typography` and `AppText`.

## Components

- `AppText`
- `AppTextStyle`

![Typography components](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/typography-components.png)

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

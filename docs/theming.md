# Theming

`AppTheme` extends Material 3 with semantic tokens.

## Dynamic color

Android 12+ dynamic color is opt-in:

```kotlin
AppTheme(dynamicColor = true) {
    // Content
}
```

When dynamic color is unavailable, `AppTheme` falls back to the supplied `AppColors` and the current dark mode setting.

## Brand overrides

```kotlin
val brandColors = AppColors.light().copy(primary = BrandBlue)

AppTheme(colors = brandColors) {
    // Content
}
```

Components read semantic colors, spacing, motion, typography, and icon contracts from `AppTheme`.

## Catalog token coverage

Showkase discovers the light and dark semantic color tokens plus the default typography scale through annotated catalog entries in `:components`. Spacing, motion, shapes, and icons remain visible through component previews because Showkase only has first-class token annotations for colors and typography.

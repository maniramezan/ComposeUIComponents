# Theming

`AppTheme` extends Material 3 with semantic tokens. Components read colors, spacing, typography, shapes, motion, and icon contracts from `AppTheme`; apps supply their own values into the same slots.

## Token bundles

| Slot bundle | Type | Default | Accessor |
|---|---|---|---|
| Colors | `AppColors` | `AppColors.light()` / `AppColors.dark()` | `AppTheme.colors` |
| Spacing | `AppSpacing` | `AppSpacing.default()` (8-dp grid) | `AppTheme.spacing` |
| Typography | `AppTypography` | `AppTypography.default()` (M3 scale + extensions) | `AppTheme.typography` |
| Shapes | `AppShapes` | `AppShapes.default()` (standard/small/large/image/pill/badge) | `AppTheme.shapes` |
| Motion | `AppMotion` | `AppMotion.default()` (short/medium/long + emphasized easing) | `AppTheme.motion` |
| Icons | `AppIcons` | `AppIcons.default()` (placeholder glyphs) | `AppTheme.icons` |

For non-`@Composable` code that needs default spacing values, the static `AppSpacingDefaults` object exposes the same defaults as plain `Dp` constants.

## Light + dark color pair

`AppTheme` takes both light and dark color schemes and swaps between them based on `darkTheme`. Brand apps typically pass both:

```kotlin
AppTheme(
    lightColors = NovalingoColors.light(),
    darkColors = NovalingoColors.dark(),
) {
    // content
}
```

If only one set is provided, the other defaults to the library's neutral defaults.

## Dynamic color

Android 12+ dynamic color is opt-in:

```kotlin
AppTheme(dynamicColor = true) {
    // Content
}
```

When dynamic color is unavailable, `AppTheme` falls back to the supplied `AppColors` and the current dark mode setting. Extension slots (status, overlay, level palette) are always sourced from the supplied `AppColors`, never overridden by the dynamic Material You scheme.

## Color slot taxonomy

The `AppColors` data class groups slots into five buckets:

1. **Core (Material 3 standard)** — `primary`, `onPrimary`, `primaryContainer`, `onPrimaryContainer`, `secondary`, `onSecondary`, `background`, `onBackground`, `surface`, `onSurface`, `surfaceVariant`, `onSurfaceVariant`, `surfaceContainer`, `outline`, `outlineVariant`, `error`, `onError`, `scrim`. These map straight onto Material 3's `ColorScheme`.
2. **Status (extensions)** — `success`, `onSuccess`, `warning`, `onWarning`. Apps that need success/warning indicators no longer have to repurpose `tertiary` or invent their own.
3. **Overlay (extensions)** — `overlayHeavy`, `overlayMedium`, `overlaySubtle`, `overlayShadowStart`, `overlayShadowEnd`, `overlayBottomTint`. Useful for media-overlay UIs (player thumbnails, gradient scrims) without baking magic alphas into call sites.
4. **Levels (extension)** — `levels: LevelPalette`. A `LevelPalette` is an ordered list of `LevelTier(background, foreground)` pairs that powers tiered-badge usages like `PillChip(label, tier)`. Apps own the ordering and color values.
5. **Skeleton (extension)** — `shimmerHighlight`. The highlight `Modifier.skeletonShimmer()` sweeps across loading placeholders. Defaults to `onSurface` (the placeholders sit on neutral surface fills, so the sweep needs the theme's ink color); override it for skeletons on an unusual surface.

## Spacing slot taxonomy

Two parallel naming schemes are exposed for back-compat:

- **Multiplier scale** — `half` (4 dp), `x1` (8 dp), `x1_5` (12 dp), `x2` (16 dp), `x3` (24 dp), `x4` (32 dp), `x5` (40 dp), `x6` (48 dp), `x9` (72 dp). Preferred for new code.
- **T-shirt scale** — `xs` (4 dp) … `xl` (24 dp). Kept for back-compat and Material-style component sizing.
- **Semantic aliases** — `padding`, `contentPadding`, `itemSpacing`, `gridSpacing`.
- **Strokes** — `strokeThin`, `strokeRegular`, `strokeThick`.
- **Interaction** — `minTapTarget` (44 dp).
- **Layout dimensions (apps override)** — `gridItemMinWidth`, `gridImageHeight`, `videoCardThumbnailHeight`, `maxContentWidthForm`, `maxContentWidthList`, `maxContentWidthPlayer`. Default to `0.dp`; apps that use those layouts must supply real values.

## Typography slot taxonomy

`AppTypography` exposes four groups:

1. **Original 4-slot scale** — `display`, `title`, `body`, `label`. Kept for back-compat with the library's first cut.
2. **Material 3 standard scale** — `displayLarge`/`Medium`/`Small`, `headlineLarge`/`Medium`/`Small`, `titleLarge`/`Medium`/`Small`, `bodyLarge`/`Medium`/`Small`, `labelLarge`/`Medium`/`Small`. These also flow into `MaterialTheme.typography`.
3. **Custom extensions** — `subtitle`, `bodyMonospaced`, `captionMonospaced`, `displayRounded`, `iconSmall`/`Medium`/`Large`/`ExtraLarge`.
4. **Weight variants** — `labelSmallSemibold`, `labelSmallBold`, `labelMediumSemibold`, `labelMediumBold`, `labelLargeBold`, `bodySmallMedium`, `bodySmallSemibold`, `bodyMediumSemibold`, `bodyLargeSemibold`. Each is its M3 base slot re-cut at a heavier weight for emphasis, and defaults to that slot with only `fontWeight` changed — size and line height stay on the scale, and a theme overrides only the ones it wants to tune. They come last in the `AppTypography` constructor, after the custom extensions, so every one carries a default. The `:sample` browser has a **Typography → Typography weight variants** page that renders each variant against its base slot.

## Brand overrides

```kotlin
val brandLight = AppColors.light().copy(
    primary = BrandBlue,
    primaryContainer = BrandBlueSubtle,
    success = BrandGreen,
    overlayHeavy = Color(0xBF000000), // app-specific gradient overlays
    levels = LevelPalette(
        listOf(
            LevelTier(background = LevelGreen, foreground = Color.White),
            LevelTier(background = LevelOrange, foreground = Color.White),
            LevelTier(background = LevelPurple, foreground = Color.White),
        ),
    ),
)

AppTheme(lightColors = brandLight) {
    // Content reads tokens from `AppTheme.colors.*` etc.
}
```

## Catalog token coverage

Showkase discovers the light and dark semantic color tokens plus the default typography scale through annotated catalog entries in `:components`. Spacing, motion, shapes, and icons remain visible through component previews because Showkase only has first-class token annotations for colors and typography.

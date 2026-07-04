# Feedback

Feedback components communicate loading, transient status, and placeholder states.

## Components

- `ProgressIndicator`
- `Skeleton`
- `SkeletonBlock`
- `Toast`
- `ToastHost`
- `LevelBadge`

![Feedback components](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/feedback-components.png)

## Example

```kotlin
AppTheme {
    ProgressIndicator(
        progress = state.progress,
        label = "Syncing",
    )
}
```

Use `progress = null` for indeterminate loading. `Toast` is an in-composition visual component, not a platform `android.widget.Toast` wrapper.

## Skeleton loading placeholders

`Skeleton` renders a single full-width placeholder block sized to a minimum touch
target height — a quick ghost row for simple loading states. For finer control (an
explicit height, an optional fixed width, or a custom shape/color to mimic the real
content it stands in for), use `SkeletonBlock` directly:

```kotlin
AppTheme {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
        SkeletonBlock(height = 20.dp, width = 160.dp)
        SkeletonBlock(height = 16.dp)
    }
}
```

Both are decorative loading placeholders and are hidden from the accessibility tree
(`clearAndSetSemantics {}`) since they carry no information a screen reader should stop
on.

### SkeletonBlock parameters

| Parameter | Type | Description |
| --- | --- | --- |
| `height` | `Dp` | Block height. |
| `modifier` | `Modifier` | Applied to the block. |
| `width` | `Dp?` | Fixed width; `null` expands to the constraints from `modifier`. |
| `shape` | `Shape` | Clip shape. Defaults to `AppTheme.shapes.small`. |
| `background` | `Color` | Placeholder color. Defaults to `AppTheme.colors.surfaceContainer`. |

## Showing toasts

`Toast` is only the visual; `ToastHost` is the driver. Hold a `ToastHostState`,
place a `ToastHost` over your screen content, and call `showToast` from a
coroutine. It shows one toast at a time, animates it in and out, and suspends
until the toast is dismissed — returning how it ended (`ToastResult`).

Choose the lifetime per call with `ToastDuration`: `Short`/`Long` auto-dismiss
after a timeout, while `Indefinite` keeps the toast until the user or caller
dismisses it. An `Indefinite` toast with no action becomes tap-to-dismiss, so
pass `dismissContentDescription` for TalkBack.

Anchor the toast with `position = ToastPosition.Top` or `ToastPosition.Bottom`
(default). Pass an `icon` for a leading glyph; the icon is tinted to the surface
color by default — pass `iconTint = Color.Unspecified` to keep a colorful icon's
own colors.

```kotlin
val hostState = rememberToastHostState()
val scope = rememberCoroutineScope()

Box(Modifier.fillMaxSize()) {
    // …your screen content…

    PrimaryButton(
        text = "Save",
        onClick = {
            scope.launch {
                val result = hostState.showToast(
                    message = "Saved",
                    icon = AppTheme.icons.check,
                    actionLabel = "Undo",
                    duration = ToastDuration.Long,
                )
                if (result == ToastResult.ActionPerformed) {
                    // undo…
                }
            }
        },
    )

    ToastHost(
        hostState = hostState,
        position = ToastPosition.Bottom, // or ToastPosition.Top
        dismissContentDescription = "Dismiss",
    )
}
```

## LevelBadge

`LevelBadge` is a small inline badge labeled with text and tinted with a `LevelTier`
from a `LevelPalette`. Use it for tiered indicators — skill level, difficulty,
priority, and similar categorical scales — where the tint encodes the tier and the
label spells it out.

```kotlin
AppTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
        LevelBadge(label = "Beginner", tier = AppTheme.colors.levels.tier(0))
        LevelBadge(label = "Advanced", tier = AppTheme.colors.levels.tier(1))
        LevelBadge(label = "Expert", tier = AppTheme.colors.levels.tier(2))
    }
}
```

Callers typically read a tier via `AppTheme.colors.levels.tier(level.ordinal)` and pass
it here, decoupling the badge from the app's own level taxonomy. `LevelPalette` ships
empty by default — supply your own tiers (background + foreground color pairs) when
building `AppTheme`.

### Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| `label` | `String` | Badge text. |
| `tier` | `LevelTier` | Background/foreground color pair for this tier. |
| `modifier` | `Modifier` | Applied to the badge container. |

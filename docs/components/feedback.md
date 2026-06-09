# Feedback

Feedback components communicate loading, transient status, and placeholder states.

## Components

- `ProgressIndicator`
- `Skeleton`
- `Toast`
- `ToastHost`

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

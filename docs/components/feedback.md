# Feedback

Feedback components communicate loading, transient status, and placeholder states.

## Components

- `ProgressIndicator`
- `Skeleton`
- `Toast`

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

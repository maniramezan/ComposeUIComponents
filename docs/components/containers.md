# Containers

Container components provide themed layout surfaces and transient UI wrappers.

## Components

- `Card`
- `Surface`
- `Section`
- `BottomSheet`
- `Dialog`
- `Snackbar`

## Example

```kotlin
AppTheme {
    Section(title = "Account") {
        Card {
            Text("Plan")
            Text("Compose Pro")
        }
    }
}
```

Prefer slot content for extensibility. `BottomSheet`, `Dialog`, and `Snackbar` are intentionally thin wrappers around Material 3 behavior.

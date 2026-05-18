# Lists

List components cover common row and state patterns.

## Components

- `ListItem`
- `LazyList`
- `EmptyState`
- `LoadingState`
- `ErrorState`

## Example

```kotlin
AppTheme {
    ListItem(
        headline = "Compose Pro",
        supportingText = "Active subscription",
    )
}
```

Use `LazyList` for edge-to-edge compatible scrollable content and pass stable keys in item blocks where possible.

# Navigation

Navigation components provide themed Material 3 wrappers for common app structure.

## Components

- `TopAppBar`
- `BottomBar`
- `TabRow`
- `NavRail`

## Example

```kotlin
AppTheme {
    BottomBar(
        items = items,
        selectedIndex = selectedIndex,
        onItemSelected = onItemSelected,
    )
}
```

Navigation state is owned by the caller. Pass callbacks instead of navigation controllers to keep components reusable.

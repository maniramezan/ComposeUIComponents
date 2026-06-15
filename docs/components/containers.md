# Containers

Container components provide themed layout surfaces and transient UI wrappers.

## Components

- `Card`
- `FlipCard`
- `Surface`
- `Section`
- `BottomSheet`
- `Dialog`
- `Snackbar`
- `HorizontalDivider`
- `VerticalDivider`

![Container components](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/container-components.png)

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

## FlipCard

`FlipCard` is a two-sided card that animates a 3D flip between a `front` and a `back` face — the building block for flash-card style UIs (vocabulary cards, quiz answers, reveal interactions). It is slot-based: each face is a `@Composable BoxScope.() -> Unit`, so you control the content entirely.

```kotlin
AppTheme {
    FlipCard(
        onClickLabel = "Flip card",
        frontStateDescription = "Showing question",
        backStateDescription = "Showing answer",
        front = { Text("What is Compose?") },
        back = { Text("A declarative UI toolkit.") },
    )
}
```

### Controlled vs. uncontrolled

`FlipCard` supports both usage modes:

- **Uncontrolled** — omit `flipped`. The card keeps its own flip state (preserved across configuration changes via `rememberSaveable`) and toggles on tap. `onFlippedChange` is still invoked if you want to observe flips.
- **Controlled** — pass `flipped` and `onFlippedChange`. You own the state; the card renders whichever face you ask for and reports tap requests back to you.

```kotlin
// Controlled: drive the flip from hoisted state and an external button.
var flipped by remember { mutableStateOf(false) }

Column {
    FlipCard(
        flipped = flipped,
        onFlippedChange = { flipped = it },
        front = { Text("Front") },
        back = { Text("Back") },
    )
    SecondaryButton(
        text = if (flipped) "Show front" else "Show back",
        onClick = { flipped = !flipped },
    )
}
```

### Flip axis

`axis` selects the rotation axis (defaults to `FlipAxis.Horizontal`):

- `FlipAxis.Horizontal` — rotates around the vertical (Y) axis, like turning a page.
- `FlipAxis.Vertical` — rotates around the horizontal (X) axis, flipping top over bottom.

Both faces are composed continuously, each on its own rotated graphics layer, so the card reads as a single solid surface turning through edge-on (rather than one face blinking out and the other in). The face turned away from the viewer is culled — `alpha = 0` plus cleared semantics — so it is neither drawn nor announced, and the back face is counter-rotated so its content is never mirrored. The graphics-layer camera distance sets the perspective depth so the rotation reads as a 3D flip and the chosen axis is clearly distinguishable.

### Theming and motion

| Parameter | Default | Notes |
| --- | --- | --- |
| `shape` | `AppTheme.shapes.large` | Clip and container shape. |
| `containerColor` | `AppTheme.colors.surface` | Background of each face. |
| flip duration / easing | `AppTheme.motion.mediumMillis` + `emphasizedEasing` | Driven by motion tokens, not hardcoded. |

### Accessibility

- The card exposes a `Role.Switch` toggle. Provide `frontStateDescription` / `backStateDescription` so TalkBack announces which face is showing when the state changes.
- Provide `onClickLabel` to describe the flip action (for example, `"Flip card"`).
- All strings are caller-supplied with no English defaults, per [ADR 0004](../adr/0004-localization-and-a11y-string-contract.md).
- Set `enabled = false` for display-only cards or when the flip is driven entirely from outside; tapping then does nothing.

### Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| `front` | `@Composable BoxScope.() -> Unit` | Front face content. |
| `back` | `@Composable BoxScope.() -> Unit` | Back face content. |
| `modifier` | `Modifier` | Applied to the card container. |
| `flipped` | `Boolean?` | `null` = uncontrolled; non-null = controlled (shows back while `true`). |
| `onFlippedChange` | `((Boolean) -> Unit)?` | Invoked with the requested face on tap. Required for tap-to-flip in controlled mode. |
| `axis` | `FlipAxis` | Rotation axis. |
| `enabled` | `Boolean` | Whether tapping flips the card. |
| `shape` | `Shape` | Container shape and clip. |
| `containerColor` | `Color` | Background of each face. |
| `frontStateDescription` | `String?` | TalkBack state description while the front shows. |
| `backStateDescription` | `String?` | TalkBack state description while the back shows. |
| `onClickLabel` | `String?` | Accessibility label for the flip action. |

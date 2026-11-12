# Actions

Action components wrap Material 3 controls with AppTheme colors, spacing, and minimum touch targets.

## Components

- `PrimaryButton`
- `SecondaryButton`
- `TextButton`
- `IconButton`
- `ExpandableFAB`
- `FAB`
- `ExtendedFloatingActionButton`
- `SegmentedControl`
- `SingleChoiceSegmentedButtonRow`
- `PillChip`
- `ActionPill`

![Actions components](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/actions-components.png)

## Example

```kotlin
AppTheme {
    SecondaryButton(
        text = "Back",
        onClick = ::goBack,
    )
}
```

Icon-based actions require a non-null `contentDescription` unless the icon is purely decorative.

## PillChip

`PillChip` is a capsule-shaped tappable chip that toggles between a selected and
unselected visual state. Typical use cases are filter rows, level selectors, and
persistent multi-option toggles.

```kotlin
AppTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
        PillChip(label = "All", isSelected = true, onClick = {})
        PillChip(label = "Beginner", isSelected = false, onClick = {})
        PillChip(label = "Advanced", isSelected = false, onClick = {})
    }
}
```

Defaults pull colors from `AppTheme.colors` so the chip picks up the surrounding theme
automatically. Override `selectedBackground`, `unselectedBackground`, `selectedLabel`,
and `unselectedLabel` when a specific tint is needed — for example, per-level filter
capsules that reuse a `LevelPalette` tier.

### Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| `label` | `String` | Chip text. |
| `isSelected` | `Boolean` | Whether the chip renders in its selected state. |
| `modifier` | `Modifier` | Applied to the chip container. |
| `onClick` | `(() -> Unit)?` | Invoked on tap. Leave `null` to render a static, non-interactive badge — no touch target, no click semantics. |
| `selectedBackground` | `Color` | Background when selected. Defaults to `AppTheme.colors.primary`. |
| `unselectedBackground` | `Color` | Background when unselected. Defaults to `AppTheme.colors.primaryContainer`. |
| `selectedLabel` | `Color` | Label color when selected. Defaults to `AppTheme.colors.onPrimary`. |
| `unselectedLabel` | `Color` | Label color when unselected. Defaults to `AppTheme.colors.onSurface`. |

## ActionPill

`ActionPill` is a capsule-shaped command with slot-based content. It is for links
and compact actions, so it exposes button semantics rather than selection state.

```kotlin
ActionPill(onClick = ::performAction) {
    Text("Status", fontWeight = FontWeight.Bold)
    Text("Active")
}
```

## PillChip as a tier badge

A second `PillChip` overload renders a static, non-interactive badge tinted with a
`LevelTier` from a `LevelPalette`. Use it for tiered indicators — skill level,
difficulty, priority, and similar categorical scales — where the tint encodes the
tier and the label spells it out.

```kotlin
AppTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
        PillChip(label = "Beginner", tier = AppTheme.colors.levels.tier(0))
        PillChip(label = "Advanced", tier = AppTheme.colors.levels.tier(1))
        PillChip(label = "Expert", tier = AppTheme.colors.levels.tier(2))
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

## ExpandableFAB

`FAB` already provides the normal icon-only floating action button, and
`ExtendedFloatingActionButton` pairs an icon with a visible label. `ExpandableFAB`
adds a state-hoisted menu whose actions each have an icon and caller-supplied label.
Set `showActionLabels = false` for an icon-only menu; each action retains its label
as its accessibility name. The default is `true`, preserving visible action text.
Supply a nonblank `triggerLabel` for an extended trigger with a visible title;
null or blank keeps the trigger icon-only and uses `contentDescription` as its name.

```kotlin
var expanded by remember { mutableStateOf(false) }
ExpandableFAB(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    actions = listOf(
        FABAction(
            id = "first",
            label = firstActionLabel,
            icon = AppTheme.icons.check,
            onClick = ::performFirstAction,
        ),
    ),
    icon = AppTheme.icons.expand,
    contentDescription = actionsDescription,
    expandedStateDescription = expandedDescription,
    collapsedStateDescription = collapsedDescription,
    direction = FABExpansionDirection.Up,
    showActionLabels = false,
    triggerLabel = actionsDescription,
)
```

All labels and accessibility descriptions come from the caller. Action labels must
be nonblank and IDs must be unique and stable. Each icon is decorative because its
button receives its name from visible text or, in icon-only mode, the action label.
Actions have a minimum 48dp touch target and support disabled states. Collapsing
removes interaction and accessibility nodes immediately while the exit animation
finishes.

Choose `Up` or `Down` for vertical expansion, or `Start` or `End` for horizontal
expansion. Start/end follow RTL layout. Items retain their supplied order and
scroll along the expansion axis when space is limited. Vertical labels wrap at large font
sizes; horizontal labels remain reachable by scrolling. An empty menu renders only the trigger.

Transitions combine fading with expansion/shrinkage using `AppTheme.motion` and
can reverse during animation. Compose applies the system animation duration
scale, including disabled animations. Keep the component in an aligned container
so its trigger remains anchored: use bottom-end for up/start, top-end for down,
and bottom-start for end. A `Scaffold` floating-action slot works for up/start;
leave system-bar and IME inset handling to the host scaffold.

Selecting an enabled item requests collapse before invoking its callback. Set
`collapseOnAction = false` to keep the menu open. The caller owns expanded state,
outside-tap dismissal, and back navigation. The sample browser includes live
controls for direction, expansion, menu item count, trigger and menu title visibility,
icons, disabled actions, RTL, state descriptions, and collapse behavior. Its icon picker
uses a fixed set of choices. Enable “Show selected item on FAB” beside the preview
to display the last selected item's title and icon without enabling a separate title
control. Turning it off restores the configured trigger.
The standalone FAB demo also includes the icon picker and a button tap counter.

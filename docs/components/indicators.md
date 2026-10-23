# Indicators

Indicators are small, non-interactive markers for status, counts, and identity.

## Components

- `Badge`
- `CountBadge`
- `Avatar`

## Badge

A capsule label such as "Beta" or "New". The text is caller-localized and read as-is.

```kotlin
Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
    Badge(text = "Beta")
    Badge(text = "New", prominent = true)
}
```

`prominent = true` fills with `AppTheme.colors.primary`; the default uses `surfaceVariant`.
Use `PillChip` instead when the capsule is tappable or selectable. Blank labels render nothing.

## CountBadge

A notification marker for an icon corner. Pass it to `TabBarItemData.badge`, supplying a
localized `unreadDescription` such as "3 unread" when the count conveys new information:

```kotlin
TabBarItemData(
    value = Destination.Inbox,
    icon = { Icon(inboxIcon, contentDescription = null) },
    label = { Text("Inbox") },
    badge = { CountBadge(count = unreadCount, contentDescription = unreadDescription) },
)
```

| `count` | Renders |
|---|---|
| `null` | A dot |
| `1..maxCount` | The number, formatted for the current locale |
| `> maxCount` | `"{maxCount}+"` (default `maxCount` is 99) |
| `<= 0` | Nothing |

`contentDescription` is caller-localized (for example "3 unread"). Leave it blank when the host
already announces the count, such as a tab whose visible label includes it. `TabBarItemData.contentDescription` is ignored
when a label slot is present, so supply the badge description in that case. A
blank description keeps the badge out of the accessibility tree.

Numbers follow the Android configuration locale, including per-app language changes.

## Avatar

A circular avatar that shows the caller's `image` when provided and otherwise shows initials
derived from `name`.

```kotlin
Avatar(
    name = person.displayName,
    size = AvatarSize.Medium,
    image = person.photoUrl?.let { url ->
        { CachedAsyncImage(data = url, contentDescription = null, contentScale = ContentScale.Crop) }
    },
)
```

- Initials are the first letters of the first and last words (one letter for a single word),
  and digits and punctuation are skipped. Unicode whitespace, punctuation, and letters
  are recognized. If the name has no letters, the circle renders empty.
- Sizes: `Small` = `spacing.x4`, `Medium` = `spacing.x5`, `Large` = `spacing.x6`.
- The avatar is one accessibility node with an image role, announced as `name`. Pass a blank
  `name` together with an `image` for a decorative avatar next to text that already names the
  person.
- At large font scales the initials shrink to fit the fixed circle rather than clipping.

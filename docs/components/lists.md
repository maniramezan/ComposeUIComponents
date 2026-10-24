# Lists

List components cover common row and state patterns.

## Components

- `ListItem`
- `LazyList`
- `EmptyState`
- `LoadingState`
- `ErrorState`
- `AsyncContent`
- `ContentRow`

![List components](https://maniramezan.github.io/ComposeUIComponents/images/screenshots/list-components.png)

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

## AsyncContent

`AsyncContent` switches between `LoadingState`, `EmptyState`, `ErrorState`, and your content from one
hoisted `AsyncContentState<T>`, so screens do not hand-write the same `when`.

```kotlin
AppTheme {
    AsyncContent(
        state = uiState, // AsyncContentState<List<Project>>
        loadingLabel = stringResource(R.string.loading_projects),
        emptyTitle = stringResource(R.string.no_projects),
        errorTitle = stringResource(R.string.load_failed_title),
        errorMessage = stringResource(R.string.load_failed_message),
        emptyAction = { TextButton(text = stringResource(R.string.create), onClick = onCreate) },
        errorAction = { TextButton(text = stringResource(R.string.retry), onClick = onRetry) },
    ) { projects ->
        LazyList { items(projects, key = { it.id }) { ListItem(headline = it.name) } }
    }
}
```

`AsyncContentState<out T>` is a sealed interface:

| State | Shows |
| --- | --- |
| `AsyncContentState.Loading` | `LoadingState` with `loadingLabel`. |
| `AsyncContentState.Empty` | `EmptyState` with `emptyTitle`, optional `emptyMessage`, optional `emptyAction`. |
| `AsyncContentState.Error(message: String? = null)` | `ErrorState` with `errorTitle`, the state's non-blank `message` or else `errorMessage`, optional `errorAction`. |
| `AsyncContentState.Content(data: T)` | Your `content` lambda, called with `data`. |

Every string is caller-supplied (ADR 0004); there are no English defaults. Pass a `message` to
`Error` to surface a localized, request-specific reason while keeping `errorMessage` as the fallback.

### Custom slots

A second overload takes `loading`, `empty`, `error`, and `content` lambdas instead of strings, for
screens that need skeletons or bespoke placeholders. The `error` slot receives the
`AsyncContentState.Error` so it can read its `message`.

```kotlin
AsyncContent(
    state = uiState,
    loading = { SkeletonBlock(height = AppTheme.spacing.x8) },
    empty = { MyEmptyIllustration() },
    error = { error -> MyErrorBanner(error.message) },
) { projects -> ProjectList(projects) }
```

### Accessibility and motion

- Entering each state is announced as a polite live region: `LoadingState` and `ErrorState` provide
  their own, and `AsyncContent` wraps the empty placeholder in one. In the slot overload, `loading`
  and `error` slots are not wrapped, so add `Modifier.semantics { liveRegion = LiveRegionMode.Polite }`
  to custom ones if they should be announced.
- Placeholders cross-fade over `AppTheme.motion.shortMillis`. The fade runs only when the state
  kind changes, so new `Content` data does not re-animate, and it is skipped when the system
  removes animations.
- The `content` slot leaves composition while another state is showing. Hoist scroll positions
  and input state that must survive a reload.
- `contentAlignment` (default `Alignment.TopStart`) positions the active state when the container
  is larger than it; use `Alignment.Center` with `Modifier.fillMaxSize()` for full-screen states.

### Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| `state` | `AsyncContentState<T>` | Which presentation to show. |
| `loadingLabel` | `String` | Loading placeholder label. |
| `emptyTitle` | `String` | Empty placeholder title. |
| `errorTitle` | `String` | Error placeholder title. |
| `errorMessage` | `String` | Error placeholder message, unless the `Error` state supplies one. |
| `modifier` | `Modifier` | Applied to the switching container. |
| `emptyMessage` | `String?` | Optional supporting text for the empty placeholder. |
| `emptyAction` | `(@Composable () -> Unit)?` | Optional empty-state action. |
| `errorAction` | `(@Composable () -> Unit)?` | Optional error-state action, for example retry. |
| `contentAlignment` | `Alignment` | Placement inside a larger container. |
| `content` | `@Composable (T) -> Unit` | Content for `AsyncContentState.Content`. |

## ContentRow

`ContentRow` is a two-line list row with a title, optional secondary/supporting text,
and leading + trailing content slots. It suits item rows, document lists,
settings rows, and similar single-tap entry points.

```kotlin
AppTheme {
    ContentRow(
        title = "ephemeral",
        secondaryText = "/əˈfemərəl/",
        supportingText = "Lasting for a very short time.",
        onClick = { /* open detail */ },
        trailingContent = {
            PillChip(label = "C1", tier = AppTheme.colors.levels.tier(2))
        },
    )
}
```

The whole row is clickable when `onClick` is non-null; pass `null` for a read-only
display. Title, secondary text, and supporting text are merged into a single
accessibility node so TalkBack reads the row as one stop rather than three.

### Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | Primary row text. |
| `modifier` | `Modifier` | Applied to the row container. |
| `secondaryText` | `String?` | Optional text shown inline after the title. |
| `supportingText` | `String?` | Optional second line, truncated to 2 lines. |
| `onClick` | `(() -> Unit)?` | Tap handler; `null` renders a read-only row. |
| `leadingContent` | `(@Composable () -> Unit)?` | Optional leading slot (icon, avatar, …). |
| `trailingContent` | `(@Composable () -> Unit)?` | Optional trailing slot (badge, icon, …). |

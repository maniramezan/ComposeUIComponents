---
name: new-component
description: Add a new public component (or split an existing one into a reusable piece) in the :components module end to end — implementation, preview, Showkase entry, tests, configurable :sample page, and docs. Use when asked to add, extract, or break out a Compose component in this repo.
---

# Adding a component

A component PR is complete only when every item below is done in the same change. Read
`AGENTS.md` first; this skill is the concrete file-by-file checklist for it.

## 1. Decide where it lives

- Reads `AppTheme` → `:components`. Theme-agnostic modifier/utility → `:compose-utils`.
  Pure Kotlin logic → `:foundation` (ADR 0002).
- Check the existing building blocks before writing new ones:
  - `Modifier.buttonRole` / `Modifier.selectableRole` / `minimumTouchTarget*` (`:compose-utils`).
  - `Modifier.pillSurface`, `RowScope.ListPrimaryTextBlock` (`ComponentSurfaces.kt`).
  - `minimumTouchTargetSize()`, `standardIconSize()`, `containerCornerRadius()`,
    `DISABLED_CONTENT_ALPHA` (`ComponentMetrics.kt`).
- If an existing component has a private sub-piece that is useful on its own (e.g.
  `PageIndicator` was extracted from `PaginatedContent`), promote it to a public component and
  make the original delegate to it rather than duplicating it.

## 2. Implement — `components/src/main/kotlin/.../components/<Name>.kt`

- `public` visibility on every public declaration; KDoc on the composable and each non-obvious
  parameter.
- Parameter order: required params, `modifier: Modifier = Modifier`, optional params, trailing
  content slot.
- Every user-visible or accessibility `String` is caller-supplied with no English default
  (`String = ""` or `String? = null`; guard blanks so no empty nodes render). ADR 0004.
- Colors, spacing, shapes, typography, and motion come from `AppTheme`; no raw `dp` or
  `Color(0x…)`. The `checkComponentTokenUsage` task fails the build on raw `N.dp`,
  `Color(0x…)`, `contentDescription = null`, and `contentDescription: String?` unless the line
  ends with `// @check:suppress` (only for decorative icons whose meaning is carried elsewhere).
- Interactive elements: 48dp minimum touch target, correct `Role`, `selectableGroup()` for
  single-choice groups, `stateDescription` for custom states.
- Defaults objects follow the `XxxDefaults` pattern (`TabBarDefaults`, `BadgeDefaults`).
- Performance: read per-frame state (scroll offsets, animations) inside `layout {}`,
  `graphicsLayer {}`, or draw lambdas; use `rememberUpdatedState` for callbacks read by
  long-lived effects; see `docs/performance.md` → "Deferring State Reads".

## 3. Preview + Showkase — `<Name>Preview.kt`

```kotlin
@PreviewLightDark
@PreviewFontScale
@Preview(name = "<Name>", group = "<Docs category>")
@Composable
public fun <Name>Preview(): Unit = AppTheme { /* representative states */ }

@ShowkaseComposable(name = "<Name>", group = "<Docs category>")
@Composable
public fun <Name>Showkase(): Unit = <Name>Preview()
```

Use a domain-neutral fixture: labels, items, sections, statuses — never product names.

## 4. Tests — `components/src/test/kotlin/.../components/`

- Pure logic (formatting, filtering, clamping) → JUnit 5 test (`org.junit.jupiter.api.Test`),
  like `BadgeCountTextTest` or `SelectionListTest`.
- Semantics and interaction → Robolectric Compose test (`@RunWith(AndroidJUnit4::class)`,
  `@Config(sdk = [35])`, `createComposeRule()` from `androidx.compose.ui.test.junit4.v2`).
  Assert roles, selection/toggle state, content descriptions, live regions, touch targets
  (`assertMinimumTouchTarget()` from `:testing`).
- Do not write "names are stable" tests that assert a literal list equals itself; API stability
  is already enforced by `binaryCompatibilityCheck`.
- Optional visual coverage: add a capture to `ComponentCategoryScreenshotTest`.

## 5. Sample page — required

- Add `internal fun <Name>Page()` to the matching `sample/src/main/java/.../Sample<Category>Pages.kt`
  (create one if the category is new), built with
  `SamplePage(preview = { … }, controls = { … })` and `ControlSwitch` / `ControlSegmented` /
  `ControlSlider` for **every** meaningful parameter and state.
- Register it in `sampleDemos()` in `SampleComponentDemo.kt` (unique `id`, `title`, `category`,
  one-line `description`).
- Lazy lists or `Scaffold`s inside a page need a bounded height (the detail pane scrolls).
- Run `.claude/skills/verify-changes/check-sample-coverage.sh`; the new component must not be
  listed.

## 6. Docs

- Add the component to the list and a usage section in the matching `docs/components/*.md`.
- New category page → also add it to `mkdocs.yml` `nav`.
- Non-obvious design decisions → a new ADR in `docs/adr/` and `mkdocs.yml`.

## 7. Verify

Follow the `verify-changes` skill. At minimum:
`./gradlew :components:testDebugUnitTest :components:ktlintCheck :components:detekt :sample:assembleDebug`
plus the root `./gradlew checkComponentTokenUsage`.

---
name: review-component
description: Review a change to this Compose design-system repo (components, theme, sample, docs) for bugs, accessibility, performance, API/binary compatibility, and product leakage. Use when asked to review a PR, a diff, or a component in this repo, or before opening a PR.
---

# Reviewing a design-system change

Work through each section against the diff. Report concrete findings with file:line, the
failure scenario, and a fix; skip sections that do not apply.

## Correctness

- Selection is compared by value (not index) where the API promises it; indices are clamped
  (`coerceIn(0, lastIndex)`) before indexing into lists; empty lists render nothing instead of
  crashing.
- `remember` keys cover every input the remembered value depends on; UI state a user would
  expect to survive rotation (search queries, expanded flags with saveable keys) uses
  `rememberSaveable`.
- Effects: `LaunchedEffect` keyed on a caller lambda restarts on every recomposition — read
  callbacks via `rememberUpdatedState` instead.
- Early `return` from a composable is fine, but state created after it is lost when the
  branch flips; confirm that is intended.

## Accessibility (ADR 0004, `docs/accessibility.md`)

- Every user-visible or a11y string is caller-supplied; no English defaults; blank strings do
  not produce empty nodes.
- Correct semantics: `Role.Button` / `Role.Tab` / `Role.RadioButton` (+ `selectableGroup()`) /
  `Role.Switch` / `Role.Checkbox`; `stateDescription` for custom states; `heading()` on section
  titles; `liveRegion = Polite` on status/loading/error surfaces that appear dynamically.
- Decorative icons use `contentDescription = null // @check:suppress` only when meaning is
  carried elsewhere; icon-only controls require a description.
- 48dp minimum touch targets; long labels wrap instead of pushing actions off-screen (give the
  text `Modifier.weight(1f)` in rows); 200% font scale does not clip (prefer `defaultMinSize`
  over fixed sizes for text containers).
- Disabled state dims labels with the control (`DISABLED_CONTENT_ALPHA`).
- Timed UI (toasts) honors `LocalAccessibilityManager.calculateRecommendedTimeoutMillis`.

## Performance (`docs/performance.md`)

- Per-frame values (scroll offsets, animation progress) are read in `layout {}`,
  `graphicsLayer {}`, or draw lambdas — not in composition. Look for `val x by animate*AsState`
  whose value is then passed as a plain `Float` parameter.
- Derived booleans from fast-changing state use `derivedStateOf`.
- Expensive conversions in frequently recomposed scopes are remembered.
- Lazy lists pass stable keys; no nested unbounded scrolling containers.

## Theming and tokens

- No raw `dp`, raw `Color(0x…)`, or ad-hoc alpha literals in `:components`; use `AppTheme` and
  `ComponentMetrics.kt`. Text uses an explicit `AppTheme.typography` style and color rather
  than inheriting Material defaults.
- Reuse `pillSurface`, `ListPrimaryTextBlock`, `buttonRole`, `selectableRole` instead of
  re-inlining them.

## API and binary compatibility

- `binaryCompatibilityCheck` (japicmp) compares against the published `API_BASELINE_VERSION`.
  Adding a parameter to an existing public function breaks it; require a new overload plus a
  `@Deprecated(level = HIDDEN)` forwarding overload. Renames of public names or files
  referenced by docs/Showkase need migration notes (`docs/migration.md`).
- New public API has KDoc, `public` modifiers, and a `XxxDefaults` object for shared defaults.

## Coverage

- Preview (`@PreviewLightDark` + `@PreviewFontScale`) and Showkase entry for new visuals.
- Behavior tests assert semantics/interaction; no tautological "names are stable" tests.
- A configurable `:sample` page is registered for every new public component — run
  `.claude/skills/verify-changes/check-sample-coverage.sh`.
- `docs/components/*.md` and `mkdocs.yml` updated.

## Product leakage (blocking)

Search every changed line — code, KDoc, previews, tests, sample data — for consuming-app
names, domain terms (e.g. vocabulary, lessons, levels tied to a specific product), analytics,
or business rules. Use generic labels: items, sections, statuses, actions.

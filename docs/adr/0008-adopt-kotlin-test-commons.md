# ADR 0008: Adopt KotlinTestCommons For Generic Test Helpers

## Status

Accepted

## Context

The `:testing` module shipped generic Compose test helpers (`MinimumTouchTargetSize`,
`assertMinimumTouchTarget`, `onNodeWithRequiredContentDescription`) that have nothing to do with
this design system. Component tests also repeated `@get:Rule createComposeRule()` plus
`setContent { AppTheme { ... } }`, and hand-rolled loops over `advanceTimeByFrame()`.

[KotlinTestCommons](https://github.com/maniramezan/KotlinTestCommons) now owns reusable test
helpers for Kotlin, Android, and Compose, including the same three helpers, `ThemedComposeRule`,
`MainTestClock.advanceFrames`, wait helpers, and `MainDispatcherRule`. `:testing` is a published
artifact guarded by `apiCheck` and the japicmp `binaryCompatibilityCheck`, so removing its public
declarations outright would be a breaking change.

## Decision

1. **Generic test helpers come from KotlinTestCommons**
   (`io.github.maniramezan.testcommons:{core,junit,compose,device}`), pinned in
   `gradle/libs.versions.toml`. Do not add new generic helpers (waits, scrolling, geometry,
   dispatcher rules, frame stepping) to this repo.
2. **`:testing` keeps only design-system-specific helpers.** It exposes
   `testcommons-compose` as an `api` dependency so existing consumers keep resolving the helpers
   transitively.
3. **The three moved declarations stay in `:testing` as `@Deprecated` delegates** with a
   `ReplaceWith` pointing at the KotlinTestCommons equivalent. Their signatures are unchanged, so
   the published API stays binary-compatible. They can be removed in a later breaking release.
4. **Tests adopt the shared helpers where it is clean.** Component tests that wrap every
   `setContent` in a default `AppTheme` use
   `ThemedComposeRule(theme = { content -> AppTheme(content = content) })`. Tests that need a
   different theme per call, no theme, or a nested theme keep a plain `createComposeRule()`.
   Frame-stepping loops use `advanceFrames(n)`.

## Consequences

- Test boilerplate shrinks and generic helpers are maintained in one place for all projects.
- Test code depends on a pre-1.0 library whose minor releases may break; the version is pinned
  and bumped deliberately.
- `:testing` consumers see deprecation warnings pointing at the replacements, and gain the
  KotlinTestCommons Compose helpers transitively.
- Adding a design-system-specific helper still belongs in `:testing`, subject to
  [ADR 0002](0002-module-boundaries.md) (it must not depend on `:components`).

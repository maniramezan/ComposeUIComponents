---
name: new-component
description: Add or extract a public Compose component in this repo, including implementation, previews, Showkase, tests, a configurable sample page, and docs.
---

# Adding a component

Read `AGENTS.md` first. A new public component is complete only when its implementation and validation surfaces are included in the same change.

## 1. Decide where it lives

- Reads `AppTheme` → `:components`. Theme-agnostic modifier or utility → `:compose-utils`. Pure Kotlin logic → `:foundation` (ADR 0002).
- Check existing building blocks first: `Modifier.buttonRole`, `Modifier.selectableRole`, and `minimumTouchTarget*` in `:compose-utils`; `Modifier.pillSurface` and `RowScope.ListPrimaryTextBlock` in `ComponentSurfaces.kt`; and `minimumTouchTargetSize()`, `standardIconSize()`, `containerCornerRadius()`, and `DISABLED_CONTENT_ALPHA` in `ComponentMetrics.kt`.
- If an existing component has a private piece useful on its own, promote that piece and make the original delegate to it rather than duplicating it.

## 2. Implement

Add the public API in `components/src/main/kotlin/io/github/maniramezan/compose/components/`. Use explicit `public` visibility and KDoc for the composable and non-obvious public types or parameters. Order parameters as required parameters, `modifier: Modifier = Modifier`, optional parameters, then a trailing content slot.

- Hoist state and use slots where callers need to customize content. Follow the existing `XxxDefaults` convention for defaults objects.
- Every UI or accessibility `String` is caller-supplied with no English default. Use `String = ""` only when blank is safe; otherwise use `String? = null` or guard blank values so no empty node renders (ADR 0004).
- Use `AppTheme` for colors, spacing, shapes, typography, and motion. `checkComponentTokenUsage` rejects raw `N.dp`, `Color(0x…)`, `contentDescription = null`, and `contentDescription: String?` in component source unless the line has `// @check:suppress`. Use that marker only for an intentional exception, such as a decorative icon whose meaning is conveyed by the parent semantics.
- Give interactive controls a 48dp minimum touch target and the correct role. Use `selectableGroup()` for single-choice groups and caller-supplied `stateDescription` for custom states.
- Defer per-frame state reads to `layout {}`, `graphicsLayer {}`, or draw lambdas. Use `rememberUpdatedState` for callbacks read by long-lived effects. See `docs/performance.md`.

## 3. Preview and Showkase

Add a `<Name>Preview.kt` with light/dark and font-scale coverage using `@PreviewLightDark` and `@PreviewFontScale`. Render it inside `AppTheme` with domain-neutral data. Add a `@ShowkaseComposable(name = "<Name>", group = "<Docs category>")` entry in a `<Name>Showkase.kt` file or the existing category Showkase file. Use an existing category when it fits.

## 4. Tests

Add focused Compose UI tests for semantics, interaction, disabled or empty states, and touch target behavior as applicable. Add or update Roborazzi coverage for visible changes. Pure logic belongs in JVM tests. Avoid tests that only compare a fixed list of public names with itself; binary compatibility checks cover public API stability. Follow ADR 0003 and the existing `components/src/test` patterns.

## 5. Configurable sample page

Register the component in `sample/src/main/java/io/github/maniramezan/compose/sample/SampleComponentDemo.kt`. Add a page using `SamplePage(preview = { … }, controls = { … })` and the existing `ControlSwitch`, `ControlSegmented`, and `ControlSlider` helpers where appropriate. Give reviewers live controls for every meaningful parameter and state. Bound the height of lazy lists and scaffolds because the sample detail pane scrolls vertically. A static demo, preview, or Showkase entry does not meet this requirement. Run `.claude/skills/verify-changes/check-sample-coverage.sh` and `./gradlew :sample:assembleDebug`.

## 6. Docs and compatibility

Update the relevant `docs/components/*.md` page and `mkdocs.yml` if a category or page is added. Document caller-provided strings, state, slots, and accessibility behavior. Avoid renaming existing public symbols casually; check binary compatibility for public API changes. Use [verify-changes](../verify-changes/SKILL.md) for the focused checks and visual review. Search changed lines for app-specific names, strings, fixtures, and business logic before opening a PR.

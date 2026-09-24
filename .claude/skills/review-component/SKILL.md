---
name: review-component
description: Review a change to this Compose design-system repo for correctness, accessibility, performance, API compatibility, sample coverage, and product leakage.
---

# Reviewing a design-system change

Read `AGENTS.md`, the relevant ADRs, and the diff. Apply the sections that fit the change. Report only concrete findings, each with `file:line`, the failure scenario, and a suggested fix. Order findings by severity; say explicitly when no findings remain.

## Correctness

- Compare selection by value where the API promises value selection. Clamp indices before indexing; empty lists should render safely.
- Check every `remember` key against its inputs. Use `rememberSaveable` for local UI state that should survive rotation, with saveable keys where needed.
- A `LaunchedEffect` keyed on a changing caller lambda can restart on recomposition; read such callbacks through `rememberUpdatedState`.
- Check whether state declared after an early composable return should be discarded when that branch flips.

## Accessibility

Use ADR 0004 and `docs/accessibility.md`.

- All visible and accessibility strings come from callers; blanks do not create empty nodes.
- Check roles (`Button`, `Tab`, `RadioButton`, `Switch`, `Checkbox`), `selectableGroup()`, `stateDescription` for custom states, `heading()` for section titles, and `liveRegion = Polite` for dynamic status, loading, or error surfaces where appropriate.
- Decorative icons may use `contentDescription = null // @check:suppress` only when another node or property carries their meaning. Icon-only controls need a description.
- Verify 48dp interactive targets, long labels at narrow widths, RTL, and 200% font scale. In rows, weighted text often prevents actions being pushed off-screen; text containers usually need `defaultMinSize` rather than a fixed height.
- Disabled labels should dim with controls, using `DISABLED_CONTENT_ALPHA` where appropriate. Timed UI should honor `LocalAccessibilityManager.calculateRecommendedTimeoutMillis`.

## Performance

Use `docs/performance.md`. Check that scroll offsets and animation progress are read inside `layout {}`, `graphicsLayer {}`, or draw lambdas when they change per frame. A plain `Float` parameter fed from `animate*AsState` can force composition every frame. Use `derivedStateOf` for derived booleans from fast-changing state when it avoids unnecessary recomposition. Check stable keys in lazy lists and bounded height for nested scrolling containers.

## API and repository fit

- Check state hoisting, slot flexibility, stable naming, explicit `public`, KDoc, and compatibility with existing callers. Identify intentional breaking changes and migration needs; run `:components:binaryCompatibilityCheck` for changed public APIs.
- Check module boundaries, `AppTheme` tokens, and reuse of shared modifiers and component building blocks. Avoid raw colors, dimensions, and ad hoc alpha literals in `:components`.
- New public components need light/dark and font-scale previews, Showkase, tests, docs, and a registered `SamplePage` with live controls for meaningful states and parameters.
- Search changed code, examples, previews, tests, and docs for branding, feature names, analytics, domain models, and product fixtures.

Use [verify-changes](../verify-changes/SKILL.md) to select checks when the review includes local validation.

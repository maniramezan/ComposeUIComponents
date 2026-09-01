# Contributing

## Local Setup

Use JDK 17 and the checked-in Gradle wrapper.

```bash
./gradlew check
```

Install docs dependencies before editing MkDocs content:

```bash
python -m pip install -r requirements-docs.txt
mkdocs build --strict
```

## Component Changes

- Use `AppTheme` tokens instead of hardcoded colors or raw `dp` literals in `:components`.
- Put reusable pure-Kotlin helpers in `:foundation` and keep that module free of Android or Compose dependencies.
- Keep APIs state-hoisted and slot-based.
- Add or update light/dark previews, 200% font-scale previews, tests, docs, and screenshot coverage for visible changes.
- Check accessibility: 48dp touch targets, meaningful icon descriptions, readable state text, and 200% font scale behavior.
- Prefer `:testing` helpers for repeated Compose accessibility assertions.

### Reuse shared helpers and utilities before reinventing them

Before writing a new component or modifier, reuse the shared building blocks the repo already owns. New components should be assembled from these rather than re-inlining the same boilerplate:

**Interaction modifiers (`:compose-utils`)** — for tappable, `Role.Button` surfaces:
- `Modifier.minimumTouchTarget(size)`, `Modifier.minimumTouchTargetHeight(height)`, `Modifier.minimumTouchTargetWidth(width)` — the accessibility touch-target primitives.
- `Modifier.buttonRole(onClick, minimumTouchTarget)` — `clickable(role = Role.Button)` + minimum touch target in one chain. Use for single-action surfaces (pills, links, tappable rows, buttons).
- `Modifier.selectableRole(selected, onClick, minimumTouchTarget)` — `selectable(role = Role.Button)` + minimum touch target for single-choice options (filter chips, selectable rows).
- Prefer the `minimumTouchTargetSize()`/`standardIconSize()`/`containerCornerRadius()` metrics helpers in `:components` (`ComponentMetrics.kt`) for the canonical values.

**Theme-coupled component building blocks (`:components`, package-internal)**:
- `Modifier.pillSurface(containerColor, border = false)` — the capsule/pill fill+clip (+ optional outline border) shared by chips, pills, badges, and compact indicators. Use it instead of repeating `.clip(AppTheme.shapes.pill).background(...)`.
- `RowScope.ListPrimaryTextBlock(...)` — the title+secondary+supporting merged-semantics text block for list rows. Use it so headline/supporting layout and the single-focus-stop TalkBack behavior stay consistent.

**Sample browser scaffolding (`:sample`)**:
- `SamplePage(preview = { … }, controls = { … })` — the standard demo-page skeleton (live preview, divider, controls panel). Every new component demo must be a `SamplePage`; use the `ControlSwitch`/`ControlSegmented`/`ControlSlider` control helpers for its controls.

When adding a shared helper, follow the module-boundary rules from ADR 0002: theme-agnostic Modifier/utility helpers belong in `:compose-utils`; anything that reads `AppTheme` belongs in `:components`. Keep new shared helpers public and documented (KDoc) so later components reuse them instead of drifting.

## Verification

Run the focused task for your change first, then the full verification before opening a PR:

```bash
./gradlew ktlintCheck detekt check :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assembleDebug
mkdocs build --strict
```

## Releases

Releases are automated after `CI` succeeds on `main`, with tags matching `X.Y.Z` (no `v` prefix).

- Use Conventional Commit PR titles such as `fix:`, `feat:`, and `feat!:` so the workflow can compute the correct version bump.
- The release workflow creates the tag, publishes to Maven Central, and creates the GitHub Release automatically.
- After a release, update `VERSION_NAME` and `API_BASELINE_VERSION` in `gradle.properties` on `main` to match the released version.
- Do not commit signing keys or Maven credentials; configure them as repository secrets.

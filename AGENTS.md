# ComposeUIComponents Agent Instructions

## Purpose

This repo owns a reusable Kotlin-first Jetpack Compose design system for Android apps. Keep the library product-agnostic, reusable across projects, and aligned with the public module boundaries in `spec.md`.

`AGENTS.md` is the source of truth for agent guidance. Keep `CLAUDE.md` thin and point back to this file.

## Required Skill Loading

Load the best matching installed skill before non-trivial planning, implementation, or review work:

- `compose-multiplatform-patterns` for Compose components, state, theming, slots, previews, and recomposition-sensitive UI.
- `android-gradle-logic` for Gradle convention plugins, version catalogs, module wiring, and build logic.
- `gradle-build-performance` for slow Gradle builds, CI time, dependency graph cost, and build scan analysis.
- `edge-to-edge` for insets, IME, system bars, scaffold padding, and edge-to-edge sample/catalog behavior.
- `web-design-guidelines` for UX, accessibility, visual consistency, and component API review.
- `android-cli` for Android SDK, emulator/device, and command-line environment diagnostics.

When adding or updating dependencies, check the latest stable version online first when network access is available, then pin the verified version in `gradle/libs.versions.toml`.

## Repository Map

- `:foundation` — pure Kotlin helpers only; no Android or Compose dependencies.
- `:tokens` — reference tokens; depends on `:foundation` only.
- `:theme` — semantic `AppTheme`, token data classes, CompositionLocals, icon contracts.
- `:icons` — curated default icon implementations.
- `:compose-utils` — preview annotations, modifiers, semantics helpers.
- `:components` — public UI components; depends on theme/icons/utils.
- `:testing` — Compose testing helpers; must not depend on `:components`.
- `:catalog` — exhaustive component browser via Showkase.
- `:sample` — consumer-like app for realistic validation.
- `:baselineprofile` — sample startup macrobenchmark/profile generation.
- `docs/` — MkDocs site content and ADRs.
- `build-logic/` — Gradle convention plugins and custom checks.

## Component Rules

- Keep components generic; never introduce product-specific names, strings, analytics, networking, or business rules.
- Use `AppTheme` tokens for colors, spacing, typography, shapes, and motion.
- Do not hardcode raw colors or raw `dp` values in `:components`; add semantic tokens first when needed.
- Prefer state-hoisted, slot-based APIs over inheritance or app-specific callbacks.
- Use `public` explicitly for public APIs to match the repo style.
- Add KDoc for public components and non-obvious public types.
- Add or update light/dark previews, font-scale previews, Showkase entries, docs, and tests for visible component changes.
- Keep accessibility first: meaningful content descriptions, 48dp touch targets, readable states, and 200% font-scale behavior.
- For media/network-backed components, keep the public API independent of implementation-library types unless exposing that type is the explicit design goal.

## Documentation Rules

- Update `docs/components/*.md` and `mkdocs.yml` when adding a new component category or public component.
- Record non-obvious architectural decisions in `docs/adr/`.
- Keep `README.md` high-level; put detailed contributor guidance in `docs/contributing.md`.
- If docs dependencies are available, validate docs with `mkdocs build --strict` after docs changes.

## Build And Verification

Use focused checks first, then broader checks when the change is significant.

```bash
./gradlew :components:testDebugUnitTest
./gradlew :components:ktlintCheck :components:detekt :components:lintDebug
./gradlew check
./gradlew ktlintCheck detekt check :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assembleDebug
mkdocs build --strict
```

For dependency/build-logic changes, also run the affected module assemble/test tasks and inspect generated dependency or build failures before broadening scope.

## Release Hygiene

- Releases are tag-driven with tags matching `vX.Y.Z`.
- Do not commit signing keys, Maven credentials, local SDK paths, generated build outputs, or personal IDE files.
- Keep `.claude/settings.local.json` ignored.
- Do not update binary compatibility baselines or screenshots casually; only update them when the API/visual change is intentional.

## Extraction From Apps

- Treat Novalingo and other apps as sources of patterns, not code to copy wholesale.
- Strip branding, strings, analytics, feature names, and app-specific dependencies.
- Validate the extracted API in `:sample` or a consumer app before considering it stable.

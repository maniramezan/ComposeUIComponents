# ComposeUIComponents Agent Instructions

## Purpose

This repo owns a reusable Kotlin-first Jetpack Compose design system for Android apps. Keep the library product-agnostic, reusable across projects, and aligned with the accepted ADRs in `docs/adr/`, the module graph in `settings.gradle.kts`, and the current implementation in the module build files.

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

## Current Architecture Decisions

- The theming model is defined by ADR 0001: extend Material 3 with immutable semantic token bundles exposed through `AppTheme` and `CompositionLocal`s.
- The module boundary model is defined by ADR 0002: keep pure Kotlin helpers, tokens, theme contracts, icons, Compose utilities, components, testing, catalog, sample, and benchmark concerns separate.
- The testing model is defined by ADR 0003: use JVM/domain tests for pure logic, Compose UI tests for semantics and interaction, and Roborazzi for screenshot verification.
- The localization and a11y string contract is defined by ADR 0004: all user-visible and accessibility strings are caller-supplied; the library ships no Android string resources and uses no English defaults.
- Treat `docs/adr/*.md`, `docs/theming.md`, `docs/accessibility.md`, `docs/performance.md`, and `docs/contributing.md` as the current design and process references when behavior or architecture questions come up.
- The library is approaching `1.0.0`; prefer additive, stable API evolution and avoid casual churn in public names, file names referenced by docs/Showkase, and module boundaries.

## Build Logic And Dependency Shape

- Shared Gradle behavior lives in `build-logic/` and the custom `compose.system.*` convention plugins. Reuse those conventions instead of duplicating Android/Compose/Kotlin setup in module build files.
- Module inclusion lives in `settings.gradle.kts`; update that file when module topology changes.
- Version pins live in `gradle/libs.versions.toml`; when changing dependencies, update the catalog instead of hardcoding versions in build scripts.
- Root build logic also enforces token-usage checks and shared verification tasks such as `composeCompilerReports`; preserve those conventions unless there is an explicit reason to change them.

## Component Rules

- Keep components generic; never introduce product-specific names, strings, analytics, networking, or business rules.
- **Localization and a11y strings**: every `String` or `String?` parameter that contributes text to the UI or the accessibility tree must be caller-supplied with no English default. Use `String = ""` when blank is safe; use `String? = null` (or guard on `isNotBlank()`) when a blank value would produce a visible empty node. Never ship `strings.xml` resources in `:components`. See [ADR 0004](docs/adr/0004-localization-and-a11y-string-contract.md).
- **Decorative icons**: set `contentDescription = null` on icons whose state is already conveyed by an adjacent text node or a semantic property (`selected`, `stateDescription`, `heading`). Use `semantics { stateDescription = … }` (with a caller-supplied string) to communicate expand/collapse and other custom interactive states to TalkBack.
- Use `AppTheme` tokens for colors, spacing, typography, shapes, and motion.
- Do not hardcode raw colors or raw `dp` values in `:components`; add semantic tokens first when needed.
- Prefer state-hoisted, slot-based APIs over inheritance or app-specific callbacks.
- Use `public` explicitly for public APIs to match the repo style.
- Add KDoc for public components and non-obvious public types.
- Add or update light/dark previews, font-scale previews, Showkase entries, docs, and tests for visible component changes.
- Keep accessibility first: meaningful content descriptions, readable states, RTL-safe layout, and 200% font-scale behavior.
- Keep actual interactive components at a 48dp minimum touch target in UI behavior and tests. The theme token `AppTheme.spacing.minTapTarget` currently defaults to 44dp for token/back-compat reasons, so do not assume the token alone fully expresses the accessibility bar.
- For media/network-backed components, keep the public API independent of implementation-library types unless exposing that type is the explicit design goal.
- Preserve the current extraction posture: extract patterns from product apps, but strip branding, product strings, analytics, and app-specific dependencies before they enter this repo.

## API And Naming Guardrails

- Public component/file names may be referenced by docs, previews, tests, and Showkase entries. Renames need explicit migration consideration instead of casual cleanup.
- Avoid introducing app-specific terminology, feature flows, or business-state models into reusable component APIs.
- Prefer extending existing token bundles, component APIs, or docs categories over adding near-duplicate concepts.

## Documentation Rules

- Update `docs/components/*.md` and `mkdocs.yml` when adding a new component category or public component.
- Record non-obvious architectural decisions in `docs/adr/`.
- Keep `README.md` high-level; put detailed contributor guidance in `docs/contributing.md`.
- If instructions or docs mention `spec.md`, treat that as stale; the repo currently uses ADRs, docs pages, and the checked-in Gradle/module configuration as the living source of truth.
- If docs dependencies are available, validate docs with `mkdocs build --strict` after docs changes.

## Build And Verification

Use focused checks first, then broader checks when the change is significant.

```bash
./gradlew :components:testDebugUnitTest
./gradlew :components:ktlintCheck :components:detekt :components:lintDebug
./gradlew composeCompilerReports -PenableComposeCompilerReports=true
./gradlew check
./gradlew ktlintCheck detekt check :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assemble
./gradlew check dokkaGenerate :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assemble
mkdocs build --strict
```

For dependency/build-logic changes, also run the affected module assemble/test tasks and inspect generated dependency or build failures before broadening scope.

For release-oriented changes, also account for:

- `apiCheck` and Dokka generation.
- Roborazzi screenshot output under `components/build/outputs/roborazzi`.
- Baseline profile review when startup or sample navigation changes.
- `publishToMavenLocal` when local signing credentials are configured; otherwise rely on the tag-triggered release workflow for the publish step.

## Release Hygiene

- Releases are automated via Release Please. Do NOT manually bump `VERSION_NAME` or create tags.
- Merge PRs to `main` using conventional commit titles (`fix:`, `feat:`, `feat!:`, etc.). Release Please reads those commits and keeps a release PR open and up-to-date.
- When ready to ship, merge the Release Please PR. It bumps `VERSION_NAME` in `gradle.properties`, updates `.release-please-manifest.json`, and pushes the `vX.Y.Z` tag.
- The tag push triggers `.github/workflows/release.yml`, which publishes to Maven Central and attaches APK/screenshot artifacts to the GitHub Release.
- Do not commit signing keys, Maven credentials, local SDK paths, generated build outputs, or personal IDE files.
- Keep `.claude/settings.local.json` ignored.
- Do not update binary compatibility baselines or screenshots casually; only update them when the API/visual change is intentional.

## Accessibility And UX Notes

- Follow `docs/accessibility.md` for manual audit expectations across TalkBack, RTL, edge-to-edge/IME, and 200% font scale.
- Catalog and sample are validation surfaces, not just demos; keep them aligned with reusable-component behavior and edge-to-edge expectations.
- For input-heavy screens in sample/catalog, preserve IME-safe layout behavior and safe-drawing inset handling.

## Extraction From Apps

- Treat Novalingo and other apps as sources of patterns, not code to copy wholesale.
- Strip branding, strings, analytics, feature names, and app-specific dependencies.
- Validate the extracted API in `:sample` or a consumer app before considering it stable.

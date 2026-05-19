# Compose UI Design System

Reusable Kotlin-first Jetpack Compose design system for Android apps.

## Modules

- `:foundation` — pure Kotlin helpers.
- `:tokens` — reference design tokens.
- `:theme` — semantic theme and `AppTheme` contracts.
- `:icons` — curated default icon implementations.
- `:compose-utils` — preview, modifier, and semantics helpers.
- `:components` — public UI components.
- `:testing` — Compose test utilities.
- `:catalog` — exhaustive component browser app.
- `:sample` — consumer-like sample app.
- `:baselineprofile` — sample startup macrobenchmark and baseline profile generator.

See `spec.md` for the implementation plan.

## Documentation

- Docs site: `https://maniramezan.github.io/ComposeUISystemDesign/`.
- Component guides live in `docs/`.
- API reference is generated with Dokka.
- The MkDocs Material site can be built with `mkdocs build --strict` after installing `requirements-docs.txt`.

## Project Hygiene

- License: MIT, see `LICENSE`.
- Security reports: see `SECURITY.md`.
- Contributions: see `docs/contributing.md`.

## Build Artifacts

CI uploads these artifacts on every successful run:

- `catalog-debug-apk` — browsable Showkase catalog APK.
- `sample-debug-apk` — consumer-like sample APK.
- `baselineprofile-debug-apk` — benchmark APK for sample startup/profile generation.
- `component-screenshots` — Roborazzi screenshots and reports.
- `docs-site` — generated MkDocs site.

## Verification

```bash
./gradlew check dokkaGenerate :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assembleDebug
```

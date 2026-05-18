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

See `spec.md` for the implementation plan.

## Documentation

- Component guides live in `docs/`.
- API reference is generated with Dokka.
- The MkDocs Material site can be built with `mkdocs build` after installing `requirements-docs.txt`.

## Verification

```bash
gradle check dokkaGenerate :catalog:assembleDebug :sample:assembleDebug
```

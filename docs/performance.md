# Performance Checklist

Run this checklist before a release and when adding complex components.

- Run `./gradlew check dokkaGenerate :catalog:assembleDebug :sample:assembleDebug`.
- Generate Compose compiler metrics with `./gradlew composeCompilerReports -PenableComposeCompilerReports=true` when investigating recomposition.
- Keep component APIs state-hoisted and stable.
- Prefer `LazyList` for large collections and pass stable item keys in callers.
- Avoid adding heavyweight icon packs or app-specific dependencies to core modules.
- Use the `:sample` app for startup and navigation macrobenchmark scenarios before `1.0.0`.

## Compose Compiler Reports

CI uploads `compose-compiler-reports` whenever the reports task runs. Local outputs are written under each Compose module's `build/compose-metrics` and `build/compose-reports` directories.

## Baseline Profile Scope

Baseline profiles should target consumer-like flows in `:sample`, not exhaustive catalog browsing.

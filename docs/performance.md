# Performance Checklist

Run this checklist before a release and when adding complex components.

- Run `gradle check dokkaGenerate :catalog:assembleDebug :sample:assembleDebug`.
- Enable Compose compiler reports with `-PenableComposeCompilerReports=true` on assemble tasks when investigating recomposition.
- Keep component APIs state-hoisted and stable.
- Prefer `LazyList` for large collections and pass stable item keys in callers.
- Avoid adding heavyweight icon packs or app-specific dependencies to core modules.
- Use the `:sample` app for startup and navigation macrobenchmark scenarios before `1.0.0`.

## Baseline Profile Scope

Baseline profiles should target consumer-like flows in `:sample`, not exhaustive catalog browsing.

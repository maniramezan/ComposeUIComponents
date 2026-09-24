# Performance Checklist

Run this checklist before a release and when adding complex components.

- Run `./gradlew check dokkaGenerate :catalog:assembleDebug :sample:assembleDebug`.
- Record component category screenshots with `./gradlew :components:recordRoborazziDebug` before reviewing visual changes.
- Generate Compose compiler metrics with `./gradlew composeCompilerReports -PenableComposeCompilerReports=true` when investigating recomposition.
- Keep component APIs state-hoisted and stable.
- Use `rememberRecompositionCounter` from `:compose-utils` for local/debug recomposition diagnostics around suspicious composables.
- Prefer `LazyList` for large collections and pass stable item keys in callers.
- Avoid adding heavyweight icon packs or app-specific dependencies to core modules.
- Use the `:sample` app for startup and navigation macrobenchmark scenarios before `1.0.0`.

## Deferring State Reads

Components that animate or track scroll should read fast-changing state in the latest phase
that needs it, so each frame re-lays out or re-draws instead of recomposing:

- Read scroll offsets inside `Modifier.layout { }` (see `TabBar`'s scroll-to-hide) and
  animation values inside `graphicsLayer { }` / `drawWithContent { }` lambdas (see `FlipCard`,
  `DisclosureCard`'s chevron, `Modifier.skeletonShimmer`). Keep the `State` object and pass a
  `() -> Float` lambda to child composables rather than the unwrapped value.
- Derive coarse-grained booleans from per-frame values with `derivedStateOf` so composition
  only reacts when the boolean flips (e.g. which `FlipCard` face is visible).
- In long-lived effects, read caller callbacks through `rememberUpdatedState` instead of
  keying the effect on the lambda; an effect keyed on a fresh lambda restarts on every
  recomposition (see `PaginatedContent`'s `onPageChanged`).
- `AppTheme` memoizes the Material color scheme, typography, and shapes it derives from the
  token bundles, so pass stable (remembered or top-level) token instances to keep that cache
  warm.

## Compose Compiler Reports

CI uploads `compose-compiler-reports` whenever the reports task runs. Local outputs are written under each Compose module's `build/compose-metrics` and `build/compose-reports` directories.

## Recomposition Diagnostics

`rememberRecompositionCounter` exposes a lightweight counter for local debugging. Pass an `onRecompose` callback only while investigating a suspected hot composable, and remove noisy logging before release changes unless the diagnostic is intentionally kept behind debug-only code.

## Screenshot Coverage

Roborazzi captures representative previews for each Phase 1 component category plus dialog and navigation rail variants. Local PNG outputs are written to `components/build/outputs/roborazzi`, and CI uploads them as the `component-screenshots` artifact.

## Baseline Profile Scope

Baseline profiles target consumer-like flows in `:sample`, not exhaustive catalog browsing. The `:baselineprofile` module contains the startup macrobenchmark and baseline profile generator. `:sample` applies the `androidx.baselineprofile` consumer plugin (see `sample/build.gradle.kts`) with `mergeIntoMain = true`, so the generated profile is written directly to `sample/src/main/generated/baselineProfiles/baseline-prof.txt` and is picked up automatically for release builds — no manual copy step. Review and commit that file when startup behavior changes.

Build the benchmark APK locally with:

```bash
./gradlew :baselineprofile:assembleDebug
```

Generate (and write into `sample/src/main/generated/baselineProfiles/baseline-prof.txt`) baseline profile data on a connected device or managed emulator with:

```bash
./gradlew :sample:generateBaselineProfile
```

Run the startup macrobenchmark on a connected device or managed emulator with:

```bash
./gradlew :baselineprofile:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=Macrobenchmark
```

Confirm the profile is actually baked into a release build with:

```bash
./gradlew :sample:assembleRelease
unzip -l sample/build/outputs/apk/release/sample-release-unsigned.apk | grep dexopt
```


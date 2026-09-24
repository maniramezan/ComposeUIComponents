---
name: verify-changes
description: Pick and run the right Gradle/MkDocs verification for a change in this repo (components, theme, sample, build logic, docs, release), and diagnose environment blockers such as a missing Android SDK. Use before committing, before opening or updating a PR, or when a check fails.
---

# Verifying a change

Run the narrowest checks first, then broaden. CI (`.github/workflows/ci.yml`) runs on pull
requests and `main` pushes only — pushing a feature branch without a PR runs nothing.

## By change type

| Change | Minimum local checks |
| --- | --- |
| `:components` code | `./gradlew :components:testDebugUnitTest :components:ktlintCheck :components:detekt checkComponentTokenUsage` |
| New/changed public API | the above + `./gradlew :components:binaryCompatibilityCheck` (see below) |
| `:theme`, `:tokens`, `:compose-utils`, `:foundation` | `./gradlew :<module>:check` and `:components:testDebugUnitTest` (downstream) |
| `:sample` pages | `./gradlew :sample:assembleDebug :sample:ktlintCheck` and `.claude/skills/verify-changes/check-sample-coverage.sh` |
| Build logic / version catalog | `./gradlew help` then the affected modules' `assemble`/`test` |
| Docs | `mkdocs build --strict` (after `pip install -r requirements-docs.txt`) |
| Visual changes | `./gradlew :components:recordRoborazziDebug` and inspect `components/build/outputs/roborazzi` |
| Release prep | `./gradlew check dokkaGenerate :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assemble` |

`checkComponentTokenUsage` is a root-project task (also run by the root `check`) that scans
`components/src/main/kotlin`. Full pre-PR sweep:

```bash
./gradlew ktlintCheck detekt check :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assemble
mkdocs build --strict
```

Auto-fix formatting with `./gradlew ktlintFormat` before re-running `ktlintCheck`.

## Sample coverage

```bash
.claude/skills/verify-changes/check-sample-coverage.sh
```

Lists public composables with no `:sample` usage. Every new public component must be absent
from the output (primitive overloads covered by a data-driven counterpart may appear).

## Binary compatibility

`binaryCompatibilityCheck` downloads the `API_BASELINE_VERSION` AAR from Maven Central and
fails on any binary-incompatible change. Common causes and fixes:

- Added a parameter to an existing public function → restore the old signature as a
  `@Deprecated(level = DeprecationLevel.HIDDEN)` overload forwarding to the new one.
- Removed/renamed a public declaration → keep a deprecated alias.
- A 404 for the baseline means that version is not published yet; the check is skipped.

Only bump `API_BASELINE_VERSION` after a release (see `docs/release.md`).

## Environment blockers

- **No Android SDK / `SDK location not found`**: set `sdk.dir` in `local.properties`
  (git-ignored) or `ANDROID_HOME`. Every Android module needs it, including `ktlintCheck`
  configuration.
- **`dl.google.com` blocked**: AGP, AndroidX, and Compose artifacts are served only from
  `dl.google.com` (`maven.google.com` redirects there). In sandboxed/cloud environments, that
  host must be on the network allowlist; there is no supported workaround. When it is blocked,
  say so, limit verification to what can run (static review, `ktlint` CLI on changed files),
  and rely on PR CI for compilation and tests.
- **Toolchain download fails (foojay)**: the build uses a JDK 17 toolchain. Install a local
  JDK 17 and point Gradle at it with
  `org.gradle.java.installations.paths=/path/to/jdk17` in `~/.gradle/gradle.properties`.
- **HTTP 429 from Maven Central**: transient rate limiting; re-run the build.

Report exactly which checks ran and which could not, with the reason.

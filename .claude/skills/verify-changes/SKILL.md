---
name: verify-changes
description: Select and run focused Gradle and MkDocs checks for changes in this repo, including public API and sample coverage, and diagnose environment blockers.
---

# Verifying a change

Read `AGENTS.md`. Run focused checks first, then broaden when the change warrants it. CI (`.github/workflows/ci.yml`) runs on pull requests and `main` pushes; a feature-branch push without a PR runs no CI.

| Change | Minimum local checks |
| --- | --- |
| `:components` code | `./gradlew :components:testDebugUnitTest :components:ktlintCheck :components:detekt checkComponentTokenUsage` |
| New or changed public API | The above, plus `./gradlew :components:binaryCompatibilityCheck` |
| `:theme`, `:tokens`, `:compose-utils`, `:foundation` | `./gradlew :<module>:check :components:testDebugUnitTest` |
| `:sample` pages | `./gradlew :sample:assembleDebug :sample:ktlintCheck` and `.claude/skills/verify-changes/check-sample-coverage.sh` |
| Build logic or version catalog | `./gradlew help`, then the affected modules' assemble and test tasks |
| Docs | `mkdocs build --strict` after `python -m pip install -r requirements-docs.txt` if needed |
| Visual changes | `./gradlew :components:recordRoborazziDebug`, then inspect `components/build/outputs/roborazzi` |
| Release prep | `./gradlew check dokkaGenerate :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assemble` |

`checkComponentTokenUsage` is a root task included in the root `check`. Format with `./gradlew ktlintFormat` before rerunning `ktlintCheck` when formatting fails.

For a substantial pre-PR sweep, run:

```bash
./gradlew ktlintCheck detekt check :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assemble
mkdocs build --strict
```

## Sample coverage

```bash
.claude/skills/verify-changes/check-sample-coverage.sh
```

The helper lists public component composables with no call in `:sample`. Every new public component should be absent from its output. Existing primitives or overloads represented by a data-driven counterpart may need manual review. Confirm the browser registry entry and live controls separately; a name occurrence alone does not prove the demo is complete.

## Binary compatibility

`binaryCompatibilityCheck` compares the release AAR with the artifact at `API_BASELINE_VERSION` in Maven Central. It is wired into each Android library module's `check`. Adding a parameter to an existing public function can break binary compatibility even when the parameter has a default; preserve the old signature with a forwarding overload when appropriate. A missing published baseline (HTTP 404) makes the comparison skip; treat that as unverified compatibility, not a pass. Do not alter baselines merely to silence a failure. For an intentional breaking change, explain migration in the PR description and follow the release policy in `AGENTS.md` and `docs/release.md`.

## Environment failures

If Gradle cannot find the Android SDK, inspect `ANDROID_HOME`, `ANDROID_SDK_ROOT`, and ignored `local.properties` before changing project files. If dependency resolution fails, identify the failing host and artifact; Android and Compose artifacts may require access to `dl.google.com`. Distinguish SDK, Java toolchain, dependency-network, and project compilation failures in the report. Record the command and its actual result; do not claim checks passed when they were skipped or could not start.

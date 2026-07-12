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

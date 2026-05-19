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
- Keep APIs state-hoisted and slot-based.
- Add or update light/dark previews, 200% font-scale previews, tests, docs, and screenshot coverage for visible changes.
- Check accessibility: 48dp touch targets, meaningful icon descriptions, readable state text, and 200% font scale behavior.

## Verification

Run the focused task for your change first, then the full verification before opening a PR:

```bash
./gradlew ktlintCheck detekt check :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug :baselineprofile:assembleDebug
mkdocs build --strict
```

## Releases

Releases are tag-driven with tags matching `vX.Y.Z`. Do not commit signing keys or Maven credentials; configure them as repository secrets.

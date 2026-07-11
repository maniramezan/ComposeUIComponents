# Release Process

Releases are fully automated via [Release Please](https://github.com/googleapis/release-please). Do not manually bump `VERSION_NAME` or create tags.

## How it works

1. Merge PRs to `main` using [Conventional Commit](https://www.conventionalcommits.org/) titles.
2. After CI passes on `main`, the Release Please workflow opens (or updates) a release PR that bumps `VERSION_NAME` in `gradle.properties`, updates `.release-please-manifest.json`, and drafts release notes.
3. When ready to ship, merge the release PR. Release Please creates the `vX.Y.Z` tag and invokes the release workflow to publish Maven artifacts and create the GitHub Release.

## Controlling the version bump

The bump is determined by the highest-impact commit since the last release:

| Commit prefix | Bump |
|---|---|
| `fix:`, `perf:`, `refactor:` | patch (0.0.x) |
| `feat:` | minor (0.x.0) |
| `feat!:`, `fix!:`, or any commit with `BREAKING CHANGE:` in the footer | major (x.0.0) |
| `chore:`, `docs:`, `test:`, `ci:`, `build:` | none (not released) |

## Triggering manually

The Release Please workflow can be run manually from the Actions tab via `workflow_dispatch`. This is useful if the automatic post-CI trigger was skipped or failed. It performs the same steps as the automatic run.

## Pre-release checklist

Before merging the Release Please PR, verify:

- `apiCheck` and every module's `binaryCompatibilityCheck` pass.
- Dokka generation passes.
- Catalog and sample debug APKs assemble.
- Component Roborazzi screenshots record successfully.
- GitHub Pages deployment succeeds from `main`.
- Maven Central publishing credentials and signing are configured in CI secrets.

After a release, update `API_BASELINE_VERSION` in `gradle.properties` to the released
version. The next release compares each published Android AAR against that baseline.

## 1.0.0 Gate

Additional checks before the first stable release:

- All public APIs reviewed for source and binary compatibility.
- `publishToMavenLocal` passes with local signing configured.
- Baseline profile generation reviewed for startup or sample navigation changes.
- Accessibility checklist complete.
- Performance checklist complete.

## GitHub Release assets

The release workflow attaches these assets automatically:

- `catalog-debug.apk`
- `sample-debug.apk`
- `component-screenshots.tar.gz`

## Versioning policy

After `1.0.0`, follow SemVer strictly. Deprecated APIs must remain for at least one minor release before removal.

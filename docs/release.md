# Release Process

Releases are automated from `main` after `CI` succeeds. Do not manually create release tags from a local machine.

## How it works

1. Merge PRs to `main` using [Conventional Commit](https://www.conventionalcommits.org/) titles.
2. After `CI` passes on `main`, `.github/workflows/release.yml` analyzes commits since the latest `X.Y.Z` tag.
3. If releasable commits are present, the workflow creates the next `X.Y.Z` tag (no `v` prefix), publishes Maven artifacts, and creates the GitHub Release automatically.

Releases through `0.11.0` were tagged as `vX.Y.Z`. Tags from `0.12.0` onward use bare
SemVer with no `v` prefix. The version-computation step recognizes both formats when
finding the latest release, so this transition is automatic and requires no manual
intervention.

## Controlling the version bump

The bump is determined by the highest-impact commit since the last release:

| Commit prefix | Bump |
|---|---|
| `fix:`, `perf:`, `refactor:` | patch (0.0.x) |
| `feat:` | minor (0.x.0) |
| `feat!:`, `fix!:`, or any commit with `BREAKING CHANGE:` in the footer | major (x.0.0) |
| `chore:`, `docs:`, `test:`, `ci:`, `build:` | none (not released) |

## Triggering manually

The Release workflow can be run manually from the Actions tab via `workflow_dispatch`.

- Leave `force_version` empty to use commit analysis.
- Set `force_version` to `0.12.0` to publish that exact version.

## Pre-release checklist

Before merging a releasable PR to `main`, verify:

- `apiCheck` and every module's `binaryCompatibilityCheck` pass.
- Dokka generation passes.
- Catalog and sample debug APKs assemble.
- Component Roborazzi screenshots record successfully.
- GitHub Pages deployment succeeds from `main`.
- Maven Central publishing credentials and signing are configured in CI secrets.

After a release, bump `VERSION_NAME` and `API_BASELINE_VERSION` in `gradle.properties` on `main` to the released version so checked-in metadata stays aligned with the published artifacts and next binary compatibility baseline.

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

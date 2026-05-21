# Release Checklist

## 1.0.0 Gate

- All public APIs reviewed for source and binary compatibility.
- `apiCheck` passes.
- Dokka generation passes.
- Catalog and sample debug APKs assemble.
- Component Roborazzi screenshots record successfully.
- `publishToMavenLocal` passes before pushing a tag when local signing is configured.
- Baseline profile generation is reviewed when startup or sample navigation changes.
- Accessibility checklist is complete.
- Performance checklist is complete.
- GitHub Pages deployment succeeds from `main` before cutting a release.
- Maven Central publishing credentials and signing are configured outside the repository.

## Verification

```bash
./gradlew check :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug publishToMavenLocal
python -m pip install -r requirements-docs.txt
mkdocs build --strict
```

If local signing is not configured, run the same verification without `publishToMavenLocal` and let the tag-triggered release workflow perform the signed publish in CI.

Before pushing the release tag:

1. Set `VERSION_NAME` in `gradle.properties` to the release version.
2. Commit and push that version bump to `main`.
3. Create and push a tag with the same release version and the `v` prefix, for example `v0.2.0`.

## GitHub Release

Pushing a tag matching `v*.*.*` runs `.github/workflows/release.yml`, publishes Maven artifacts, and creates the GitHub Release automatically.

The workflow currently attaches these assets to the GitHub Release:

- `catalog/build/outputs/apk/debug/catalog-debug.apk`
- `sample/build/outputs/apk/debug/sample-debug.apk`
- `component-screenshots.tar.gz`

The published Maven artifact version comes from `VERSION_NAME` in `gradle.properties`, so keep `v0.2.0`-style tags aligned with `0.2.0` in `VERSION_NAME`.

## Versioning

After `1.0.0`, follow SemVer and keep deprecated APIs for at least one minor release before removal.

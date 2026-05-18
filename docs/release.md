# Release Checklist

## 1.0.0 Gate

- All public APIs reviewed for source and binary compatibility.
- `apiCheck` passes.
- Dokka generation passes.
- Catalog and sample debug APKs assemble.
- Component Roborazzi screenshots record successfully.
- `publishToMavenLocal` passes before pushing a tag.
- Accessibility checklist is complete.
- Performance checklist is complete.
- Maven Central publishing credentials and signing are configured outside the repository.

## Verification

```bash
./gradlew check :components:recordRoborazziDebug :catalog:assembleDebug :sample:assembleDebug publishToMavenLocal
python -m pip install -r requirements-docs.txt
mkdocs build --strict
```

## GitHub Release

Pushing a tag matching `v*.*.*` runs `.github/workflows/release.yml`, publishes Maven artifacts, and attaches the catalog APK, sample APK, docs site archive, and component screenshot archive to the GitHub Release.

## Versioning

After `1.0.0`, follow SemVer and keep deprecated APIs for at least one minor release before removal.

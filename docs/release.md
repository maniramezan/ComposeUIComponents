# Release Checklist

## 1.0.0 Gate

- All public APIs reviewed for source and binary compatibility.
- `apiCheck` passes.
- Dokka generation passes.
- Catalog and sample debug APKs assemble.
- Accessibility checklist is complete.
- Performance checklist is complete.
- Maven Central publishing credentials and signing are configured outside the repository.

## Verification

```bash
gradle check dokkaGenerate :catalog:assembleDebug :sample:assembleDebug
```

## Versioning

After `1.0.0`, follow SemVer and keep deprecated APIs for at least one minor release before removal.

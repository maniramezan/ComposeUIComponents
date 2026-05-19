## Summary

- Describe the change and why it is needed.

## Verification

- [ ] `./gradlew ktlintCheck detekt check`
- [ ] `./gradlew :components:recordRoborazziDebug` when UI changes are visible
- [ ] `mkdocs build --strict` when docs change

## Checklist

- [ ] Public API changes are intentional and documented
- [ ] Components use `AppTheme` tokens and shared helpers instead of raw colors/dimensions
- [ ] Accessibility is covered for touch targets, labels, readable state text, and 200% font scale
- [ ] Screenshots, previews, tests, and docs are updated for visible behavior changes

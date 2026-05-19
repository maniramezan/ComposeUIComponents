# Migration Guide

The library is pre-`1.0.0`, so APIs can still change while the design system stabilizes.

## Before 1.0.0

- Pin an explicit version in every app module.
- Read release notes before upgrading.
- Re-run screenshot and accessibility checks for any component API changes.
- Treat `@Experimental` APIs as unstable until promoted.

## After 1.0.0

- SemVer applies to public APIs.
- Deprecated APIs remain available for at least one minor release before removal.
- Binary compatibility is checked for stable public modules.

## From Raw Material Components

- Wrap app content in `AppTheme`.
- Replace hardcoded colors and dimensions with semantic tokens from `AppTheme`.
- Prefer design-system components for shared UI patterns before adding app-local variants.
- Use slot APIs for content customization instead of forking components.

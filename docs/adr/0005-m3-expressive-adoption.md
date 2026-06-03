# ADR 0005: Material 3 Expressive Adoption Strategy

## Status

Proposed

## Context

Material 3 Expressive APIs were introduced as experimental in the Compose BOM around early
2026 and are targeting stable promotion in late 2026. They introduce several capabilities
relevant to a design system library:

- **Shape morphing**: 35 built-in shapes with native `ShapeMorph` animation support,
  enabling animated shape transitions (e.g., FAB expand, button press) with no custom
  drawing code.
- **Button groups and toggle buttons**: new composables for grouped, mutually-exclusive
  or multi-select button patterns.
- **Expressive FAB and menus**: richer motion defaults and larger surface area variants.
- **Motion defaults**: physics-based spring curves baked into M3 component transitions.

The current `AppTheme` token system (ADR 0001) exposes semantic shape tokens via
`AppTheme.shapes`. Adopting M3 Expressive shapes means expanding that surface.

## Decision

1. **Track experimental M3 Expressive APIs in `:catalog` and `:sample` only** until they
   are promoted to stable in a non-`alpha`/`beta` Compose BOM release.

2. **When stable, extend `AppTheme.shapes`** with a `morphable` sub-bundle (or equivalent
   token names) that exposes `ShapeMorph`-compatible shape tokens alongside the existing
   `CornerBasedShape` tokens. Keep both surfaces so consumers on older BOM versions are
   not broken.

3. **Adopt expressive button and FAB variants in `:components`** when stable, exposed as
   new composables (not replacements for existing ones) to preserve API stability per the
   library's `1.0.0` posture.

4. **Do not adopt experimental M3 Expressive APIs in `:components` public API** before
   they are stable. Experimental APIs may change between alpha releases, which would force
   binary-breaking updates.

5. **Shape token names** must remain implementation-independent (`AppTheme.shapes.small`,
   not `AppTheme.shapes.expressiveSmall`) to decouple the token surface from M3 version
   specifics.

## Consequences

- `:catalog` gains previews for expressive shapes and button groups as they ship in alpha
  BOM releases, giving early visibility without API commitment.
- The `AppTheme.shapes` token bundle will expand with a new morph-capable surface once
  M3 Expressive is stable; that is an additive, non-breaking change.
- New expressive component composables will be added alongside existing ones rather than
  replacing them; existing consumers see no breakage.
- Tracking alpha adoption in `:catalog`/`:sample` requires opting in to the
  `@ExperimentalMaterial3ExpressiveApi` annotation; that annotation must not leak into
  `:components` public API.

# ADR 0006: Adaptive Layout Breakpoint System

## Status

Proposed

## Context

Jetpack Compose is moving from the three-tier `WindowSizeClass` breakpoint model
(Compact / Medium / Expanded) to a five-tier model announced at Google I/O 2026:

| Tier | Target surface |
|------|---------------|
| Compact | phones in portrait |
| Medium | large phones / small foldables unfolded |
| Expanded | tablets, large foldables unfolded |
| Large | desktop-class or large tablets |
| Extra-large | XR and very large displays |

This system ships in `androidx.compose.material3.adaptive` and is intended to replace
`WindowSizeClass` over time. The current stable `WindowSizeClass` API only has three
classes; the five-tier replacement is in alpha as of 2026.

The library currently does not expose an adaptive layout token or breakpoint abstraction
— consumers are expected to apply `WindowSizeClass` in their own scaffold or navigation
layer. However, some components (e.g., bottom sheets, navigation rails, dialogs) may
benefit from breakpoint-aware defaults.

## Decision

1. **Do not expose a breakpoint token or type in `:components` public API** until the
   five-tier system (or a clear stable successor to `WindowSizeClass`) is promoted to
   stable in the AndroidX adaptive library.

2. **When stable, introduce a `ScreenClass` sealed type in `:tokens`** (or equivalent
   name agreed with the consuming team) that maps to the five tiers and replaces any
   internal use of `WindowSizeClass`. The type must not depend on the AndroidX adaptive
   library directly; it is a pure-Kotlin mirror that `:theme` or `:components` maps to
   from the platform type.

3. **Existing three-tier consumers are not broken.** The `ScreenClass` type will include
   a `from(WindowSizeClass)` factory mapping Compact/Medium/Expanded to the three lower
   tiers, with Large/ExtraLarge unreachable from that factory until consumers migrate.

4. **No adaptive layout helpers in `:components` before this ADR reaches Accepted
   status.** If a component genuinely needs breakpoint-aware behavior before then, use
   a `(WindowWidthSizeClass) -> Unit` lambda slot pattern so callers control the logic.

## Consequences

- Components remain surface-agnostic until the five-tier model is stable; no premature
  API commitment.
- When `:tokens` gains `ScreenClass`, a follow-on ADR or migration guide will cover
  upgrading components that use the lambda slot pattern to the token-based API.
- The `from(WindowSizeClass)` compatibility factory ensures the migration is additive for
  consumers already on the three-tier model.

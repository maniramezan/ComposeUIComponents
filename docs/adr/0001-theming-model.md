# ADR 0001: Theming Model

## Status

Accepted

## Decision

Extend Material 3 with immutable semantic token data classes exposed through `CompositionLocal`s and `AppTheme`.

## Consequences

Consumers override tokens with `copy()` and slots instead of inheritance. Component defaults derive from semantic tokens rather than hardcoded values.

# ADR 0002: Module Boundaries

## Status

Accepted

## Decision

Separate pure Kotlin helpers, reference tokens, theme contracts, icons, Compose utilities, components, testing, catalog, and sample modules.

## Consequences

The dependency graph prevents theme/icon cycles and keeps component implementation dependencies out of test utilities.

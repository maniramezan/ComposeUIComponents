# Architecture Decision Records

ADRs capture decisions that shape the design system and are expected to outlive a single implementation change.

## Accepted Decisions

- [ADR 0001: Theming Model](0001-theming-model.md): extend Material 3 with immutable semantic tokens and `CompositionLocal`s.
- [ADR 0002: Module Boundaries](0002-module-boundaries.md): keep pure Kotlin helpers, tokens, theme contracts, icons, utilities, components, testing, catalog, and sample modules separate.
- [ADR 0003: Testing Strategy](0003-testing-strategy.md): combine JVM/domain tests, Compose UI tests, and Roborazzi screenshot coverage.

## Writing New ADRs

Add new records for decisions that affect public APIs, module boundaries, publishing, test strategy, or long-term compatibility. Use the next sequential number and keep each record focused on one decision.

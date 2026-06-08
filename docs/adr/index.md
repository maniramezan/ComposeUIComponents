# Architecture Decision Records

ADRs capture decisions that shape the design system and are expected to outlive a single implementation change.

## Accepted Decisions

- [ADR 0001: Theming Model](0001-theming-model.md): extend Material 3 with immutable semantic tokens and `CompositionLocal`s.
- [ADR 0002: Module Boundaries](0002-module-boundaries.md): keep pure Kotlin helpers, tokens, theme contracts, icons, utilities, components, testing, catalog, and sample modules separate.
- [ADR 0003: Testing Strategy](0003-testing-strategy.md): combine JVM/domain tests, Compose UI tests, and Roborazzi screenshot coverage.
- [ADR 0004: Localization and A11y String Contract](0004-localization-and-a11y-string-contract.md): all user-visible and accessibility strings are caller-supplied; the library ships no string resources and uses no English defaults.
- [ADR 0005: Material 3 Expressive Adoption Strategy](0005-m3-expressive-adoption.md): track M3 Expressive APIs in catalog/sample until stable, then extend `AppTheme.shapes` and add new expressive components additively.
- [ADR 0006: Adaptive Layout Breakpoint System](0006-adaptive-layout-breakpoints.md): defer exposing a breakpoint abstraction until the five-tier adaptive system is stable; use lambda slot pattern in components in the interim.
- [ADR 0007: Secure Storage Module](0007-secure-storage-module.md): ship a standalone Tink + Android Keystore `:secure-storage` module with an interface surface and an `InMemorySecureStorage` test double.

## Writing New ADRs

Add new records for decisions that affect public APIs, module boundaries, publishing, test strategy, or long-term compatibility. Use the next sequential number and keep each record focused on one decision.

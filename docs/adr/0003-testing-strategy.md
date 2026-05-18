# ADR 0003: Testing Strategy

## Status

Accepted

## Decision

Use JVM/domain tests for pure logic, Compose UI tests for interaction semantics, and Roborazzi for JVM screenshot verification.

## Consequences

Visual regressions are reviewed as artifacts, while reusable semantics helpers live outside component implementations.

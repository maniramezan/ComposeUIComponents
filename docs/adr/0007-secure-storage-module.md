# ADR 0007: Secure Storage Module

## Status

Accepted

## Context

Apps consuming this library repeatedly need a Keychain-equivalent place to keep small
secrets (auth tokens, session metadata, feature flags) encrypted at rest and surviving
process death. On Android the historical answer, `androidx.security:security-crypto`
(`EncryptedSharedPreferences`), was **deprecated in April 2024** with no drop-in successor,
so each app re-solves this with bespoke Tink/Keystore code.

Two tensions shaped this decision:

1. This library is a **Compose design system**; a security/storage utility is off-theme
   relative to tokens, theme, icons, and components (see
   [ADR 0002](0002-module-boundaries.md)).
2. Crypto backed by the Android Keystore is hard to unit-test on the JVM, which conflicts
   with the layered approach in [ADR 0003](0003-testing-strategy.md) and risks shipping an
   API consumers cannot test against.

## Decision

1. **Ship a standalone `:secure-storage` module**, published as
   `io.github.maniramezan.compose:secure-storage` under the shared `VERSION_NAME`. It has
   **no dependency on the Compose or theme modules**, so non-Compose consumers can adopt it
   alone and Compose consumers pay nothing extra.

2. **Back it with Google Tink AEAD (AES-256-GCM) sealed by an Android Keystore master
   key**, not the deprecated `EncryptedSharedPreferences`. Encrypted values live in a
   private `SharedPreferences` file; each entry binds its key as associated data.

3. **Expose a narrow `SecureStorage` interface** as the public surface, with a
   `SecureStorage.create()` factory for the Keystore-backed implementation. The crypto
   implementation (`AeadSecureStorage`) and persistence seam (`KeyValueStore`) stay
   `internal`.

4. **Ship `InMemorySecureStorage` in the main artifact** as the supported test double.
   Consumers depend on the interface and inject it to test on a plain JVM — no Keystore,
   Robolectric, or instrumentation. The `KeyValueStore` seam keeps the crypto wrapper
   itself unit-testable with a software AEAD; the persistence seam is covered with
   Robolectric. `create()`'s Keystore wiring is left to instrumented testing.

5. **Reads never throw on a decryption failure** — a tampered, corrupt, or unreadable
   value is reported as `null`, so callers treat "undecryptable" as "absent".

## Consequences

- The published artifact set gains a module that is intentionally outside the Compose
  dependency graph; [ADR 0002](0002-module-boundaries.md)'s boundary list is extended to
  include a security utility that depends only on Tink and the Android SDK.
- Consumers get a tested, non-deprecated Keychain equivalent and a first-class testing
  story out of the box.
- `InMemorySecureStorage` lives in the production artifact (not a separate `-testing`
  artifact); it is documented as test/preview-only and never to be used in production.
- The on-device `create()` path is not covered by JVM unit tests; device coverage would
  require adding an instrumented test, which CI does not currently run.

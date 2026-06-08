# Secure Storage

`io.github.maniramezan.compose:secure-storage` is a small, Keychain-equivalent encrypted
key–value store for Android. It is the Android counterpart to the iOS Keychain for small
secrets — auth tokens, session metadata, feature flags — that must survive process death.

It is a standalone module: it does **not** depend on the Compose or theme modules.

## Install

```kotlin
implementation("io.github.maniramezan.compose:secure-storage:<version>")
```

Minimum SDK 26.

## Quick start

```kotlin
val storage = SecureStorage.create(context)

storage.putString("auth.token", token)
val token: String? = storage.getString("auth.token")   // null if never stored

storage.remove("auth.token")
```

!!! tip "Create once, reuse"
    Building the keyset is comparatively expensive. Create one instance per store `name`
    (e.g. an injected singleton) and reuse it — don't call `create()` per access.

## How it works

Values are encrypted with an **AES-256-GCM** AEAD primitive (Google Tink). The keyset that
performs the encryption is itself sealed by a master key held in the **Android Keystore**
(hardware-backed where the device supports it). The encrypted values live in a private
`SharedPreferences` file, so the bytes on disk are useless without the device's Keystore.

Each entry is encrypted with its own key as *associated data*, so ciphertext cannot be
replayed under a different key.

!!! warning "What it protects — and what it doesn't"
    - ✅ **Data at rest.** An attacker with the raw `SharedPreferences` file cannot decrypt
      values without the Keystore-held master key.
    - ❌ **A compromised process.** Any code that can call this API can read the values; the
      store does not add biometric / user-presence gating.
    - It is for confidentiality of *small* secrets at rest, not a full secrets-management
      solution. There is no hard size limit, but large blobs are a poor fit.

## Multiple stores

`name` namespaces a store. Reuse the same `name` to reopen the same data; use distinct
names for fully isolated stores:

```kotlin
val authStore = SecureStorage.create(context, name = "auth")
val cacheStore = SecureStorage.create(context, name = "secure_cache")
```

`masterKeyAlias` controls which Android Keystore key seals the keyset; the default is
usually fine. Stores that share an alias still keep separate keysets and values.

## Bytes

Besides strings, you can store raw bytes. Stored and returned arrays are defensively
copied, so mutating them afterwards never affects what's persisted:

```kotlin
storage.putBytes("key", bytes)
val out: ByteArray? = storage.getBytes("key")
```

## Error handling

- `create()` can throw `IOException` / `GeneralSecurityException` if the Keystore or keyset
  is unusable (rare; broken-keystore devices). Decide whether to fail fast or fall back.
- Reads (`getString` / `getBytes`) never throw on a decryption failure — a tampered,
  corrupt, or unreadable value is reported as `null`.

## Testing

Depend on the `SecureStorage` **interface** in your code (not `create()`), then inject the
provided `InMemorySecureStorage` in tests. It is public, dependency-free, and runs on a
plain JVM — no Android Keystore, Robolectric, or instrumentation required.

```kotlin
// Production code depends on the interface:
class TokenRepository(private val storage: SecureStorage) {
    fun save(token: String) = storage.putString("auth.token", token)
    fun load(): String? = storage.getString("auth.token")
}

// Unit test — no Android needed:
@Test
fun `persists the token`() {
    val storage = InMemorySecureStorage()
    val repo = TokenRepository(storage)

    repo.save("token-123")

    assertEquals("token-123", repo.load())
    assertEquals("token-123", storage.getString("auth.token")) // inspect directly
}
```

`InMemorySecureStorage` keeps values in memory, unencrypted, for the lifetime of the
instance — never use it in production.

## API summary

| Member | Description |
| --- | --- |
| `SecureStorage.create(context, name?, masterKeyAlias?)` | Build a persistent, Keystore-backed store. |
| `putString(key, value)` / `getString(key)` | Store / read a string; read returns `null` if absent or undecryptable. |
| `putBytes(key, value)` / `getBytes(key)` | Store / read raw bytes (defensively copied). |
| `contains(key)` | Whether a value is stored. |
| `remove(key)` | Delete one entry (no-op if absent). |
| `clear()` | Delete all entries; the store stays usable. |
| `InMemorySecureStorage()` | In-memory test double / preview implementation. |

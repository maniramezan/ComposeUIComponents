package io.github.maniramezan.compose.securestorage

import java.util.concurrent.ConcurrentHashMap

/**
 * A non-persistent, non-encrypted [SecureStorage] for tests and previews.
 *
 * Drop this in wherever your production code depends on [SecureStorage] so unit tests can
 * run on a plain JVM — no Android Keystore, Robolectric, or instrumentation required:
 *
 * ```kotlin
 * val storage: SecureStorage = InMemorySecureStorage()
 * val repository = TokenRepository(storage)   // your class under test
 *
 * repository.save("token-123")
 * assertEquals("token-123", storage.getString("auth.token"))
 * ```
 *
 * Values live only in memory for the lifetime of the instance and are **not** encrypted,
 * so never use this in production. Behaviour otherwise matches the real store, including
 * defensive copies of byte arrays so callers cannot mutate stored values.
 */
public class InMemorySecureStorage : SecureStorage {
    private val values = ConcurrentHashMap<String, ByteArray>()

    override fun putString(
        key: String,
        value: String,
    ): Unit = putBytes(key, value.toByteArray(Charsets.UTF_8))

    override fun getString(key: String): String? = getBytes(key)?.toString(Charsets.UTF_8)

    override fun putBytes(
        key: String,
        value: ByteArray,
    ) {
        values[key] = value.copyOf()
    }

    override fun getBytes(key: String): ByteArray? = values[key]?.copyOf()

    override fun contains(key: String): Boolean = values.containsKey(key)

    override fun remove(key: String) {
        values.remove(key)
    }

    override fun clear(): Unit = values.clear()
}

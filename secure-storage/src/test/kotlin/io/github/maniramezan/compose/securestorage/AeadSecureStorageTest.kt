package io.github.maniramezan.compose.securestorage

import com.google.common.truth.Truth.assertThat
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Exercises [AeadSecureStorage] against a software AEAD and an in-memory store, so the
 * crypto round-trip is covered without the Android Keystore or framework.
 */
class AeadSecureStorageTest {
    private lateinit var backing: InMemoryKeyValueStore
    private lateinit var storage: AeadSecureStorage

    @BeforeEach
    fun setUp() {
        AeadConfig.register()
        val aead: Aead =
            KeysetHandle
                .generateNew(KeyTemplates.get("AES256_GCM"))
                .getPrimitive(RegistryConfiguration.get(), Aead::class.java)
        backing = InMemoryKeyValueStore()
        storage = AeadSecureStorage(aead, backing)
    }

    @Test
    fun `round trips a string value`() {
        storage.putString("token", "secret-value")

        assertThat(storage.getString("token")).isEqualTo("secret-value")
        assertThat(storage.contains("token")).isTrue()
    }

    @Test
    fun `returns null for a missing key`() {
        assertThat(storage.getString("absent")).isNull()
        assertThat(storage.contains("absent")).isFalse()
    }

    @Test
    fun `persists ciphertext rather than plaintext`() {
        storage.putString("token", "secret-value")

        val stored = backing.snapshot().getValue("token")
        assertThat(stored).doesNotContain("secret-value")
    }

    @Test
    fun `overwrites an existing value`() {
        storage.putString("token", "first")
        storage.putString("token", "second")

        assertThat(storage.getString("token")).isEqualTo("second")
    }

    @Test
    fun `remove deletes the value`() {
        storage.putString("token", "secret-value")

        storage.remove("token")

        assertThat(storage.contains("token")).isFalse()
        assertThat(storage.getString("token")).isNull()
    }

    @Test
    fun `clear removes every value`() {
        storage.putString("a", "1")
        storage.putString("b", "2")

        storage.clear()

        assertThat(storage.contains("a")).isFalse()
        assertThat(storage.contains("b")).isFalse()
    }

    @Test
    fun `tampered ciphertext decrypts to null instead of throwing`() {
        storage.putString("token", "secret-value")
        backing.put("token", "not-valid-ciphertext")

        assertThat(storage.getString("token")).isNull()
    }

    @Test
    fun `value bound to its key cannot be read under another key`() {
        storage.putString("token", "secret-value")

        // Copy the ciphertext to a different key; the key is used as associated data,
        // so decryption under the new key must fail.
        backing.put("copy", backing.snapshot().getValue("token"))

        assertThat(storage.getString("copy")).isNull()
    }
}

private class InMemoryKeyValueStore : KeyValueStore {
    private val map = mutableMapOf<String, String>()

    override fun put(
        key: String,
        value: String,
    ) {
        map[key] = value
    }

    override fun get(key: String): String? = map[key]

    override fun contains(key: String): Boolean = map.containsKey(key)

    override fun remove(key: String) {
        map.remove(key)
    }

    override fun clear() {
        map.clear()
    }

    fun snapshot(): Map<String, String> = map.toMap()
}

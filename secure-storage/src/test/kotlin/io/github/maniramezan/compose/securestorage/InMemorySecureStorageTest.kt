package io.github.maniramezan.compose.securestorage

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Verifies the test double behaves like the real store, so consumers can rely on it.
 */
class InMemorySecureStorageTest {
    private val storage = InMemorySecureStorage()

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
    fun `overwrites an existing value`() {
        storage.putString("token", "first")
        storage.putString("token", "second")

        assertThat(storage.getString("token")).isEqualTo("second")
    }

    @Test
    fun `remove and clear delete values`() {
        storage.putString("a", "1")
        storage.putString("b", "2")

        storage.remove("a")
        assertThat(storage.contains("a")).isFalse()
        assertThat(storage.contains("b")).isTrue()

        storage.clear()
        assertThat(storage.contains("b")).isFalse()
    }

    @Test
    fun `defensively copies bytes so callers cannot mutate stored values`() {
        val input = byteArrayOf(1, 2, 3)
        storage.putBytes("key", input)
        input[0] = 9

        val stored = storage.getBytes("key")
        assertThat(stored).isEqualTo(byteArrayOf(1, 2, 3))

        stored!![0] = 7
        assertThat(storage.getBytes("key")).isEqualTo(byteArrayOf(1, 2, 3))
    }
}

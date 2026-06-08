package io.github.maniramezan.compose.securestorage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Covers the persistence seam against a real [android.content.SharedPreferences] via
 * Robolectric, so the store survives independent reads/writes the way it will on device.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
class SharedPreferencesKeyValueStoreTest {
    private fun newStore(): SharedPreferencesKeyValueStore {
        val prefs =
            RuntimeEnvironment
                .getApplication()
                .getSharedPreferences("test_values", android.content.Context.MODE_PRIVATE)
        return SharedPreferencesKeyValueStore(prefs)
    }

    @Test
    fun `persists and reads back a value`() {
        val store = newStore()
        store.put("k", "ciphertext")

        assertThat(store.get("k")).isEqualTo("ciphertext")
        assertThat(store.contains("k")).isTrue()
    }

    @Test
    fun `returns null for a missing key`() {
        assertThat(newStore().get("absent")).isNull()
    }

    @Test
    fun `remove and clear delete values`() {
        val store = newStore()
        store.put("a", "1")
        store.put("b", "2")

        store.remove("a")
        assertThat(store.contains("a")).isFalse()
        assertThat(store.contains("b")).isTrue()

        store.clear()
        assertThat(store.contains("b")).isFalse()
    }
}

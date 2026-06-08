package io.github.maniramezan.compose.securestorage

import android.content.SharedPreferences

/**
 * Persistence seam for already-encrypted, Base64-encoded values.
 *
 * Kept separate from the crypto so [AeadSecureStorage] can be unit-tested against an
 * in-memory store without touching the Android framework or the Keystore.
 */
internal interface KeyValueStore {
    fun put(
        key: String,
        value: String,
    )

    fun get(key: String): String?

    fun contains(key: String): Boolean

    fun remove(key: String)

    fun clear()
}

/** [KeyValueStore] backed by a [SharedPreferences] file holding ciphertext only. */
internal class SharedPreferencesKeyValueStore(
    private val prefs: SharedPreferences,
) : KeyValueStore {
    override fun put(
        key: String,
        value: String,
    ) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(key: String): String? = prefs.getString(key, null)

    override fun contains(key: String): Boolean = prefs.contains(key)

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    override fun clear() {
        prefs.edit().clear().apply()
    }
}

package io.github.maniramezan.compose.securestorage

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import java.util.Base64

/**
 * [SecureStorage] implementation that AEAD-encrypts each value before persisting it.
 *
 * Each value is encrypted with the entry's key as associated data, so ciphertext cannot
 * be replayed under a different key. Ciphertext is Base64-encoded (URL-safe, no padding)
 * for storage as a string.
 */
internal class AeadSecureStorage(
    private val aead: Aead,
    private val store: KeyValueStore,
) : SecureStorage {
    override fun putString(
        key: String,
        value: String,
    ): Unit = putBytes(key, value.toByteArray(Charsets.UTF_8))

    override fun getString(key: String): String? = getBytes(key)?.toString(Charsets.UTF_8)

    override fun putBytes(
        key: String,
        value: ByteArray,
    ) {
        val ciphertext = aead.encrypt(value, key.toByteArray(Charsets.UTF_8))
        store.put(key, encoder.encodeToString(ciphertext))
    }

    override fun getBytes(key: String): ByteArray? {
        val encoded = store.get(key) ?: return null
        // A failed decrypt (tampered, key rotated, or corrupt) is treated as "absent"
        // rather than crashing the caller.
        return runCatching {
            aead.decrypt(decoder.decode(encoded), key.toByteArray(Charsets.UTF_8))
        }.getOrNull()
    }

    override fun contains(key: String): Boolean = store.contains(key)

    override fun remove(key: String): Unit = store.remove(key)

    override fun clear(): Unit = store.clear()

    internal companion object {
        private val encoder: Base64.Encoder = Base64.getUrlEncoder().withoutPadding()
        private val decoder: Base64.Decoder = Base64.getUrlDecoder()

        /**
         * Builds a Keystore-backed [AeadSecureStorage]. The AEAD keyset is generated on
         * first use and sealed under the Android Keystore master key at [masterKeyAlias].
         *
         * @throws java.io.IOException if the keyset cannot be read or written.
         * @throws java.security.GeneralSecurityException if the keyset or master key is unusable.
         */
        fun create(
            context: Context,
            name: String,
            masterKeyAlias: String,
        ): AeadSecureStorage {
            AeadConfig.register()
            val appContext = context.applicationContext
            val keysetHandle =
                AndroidKeysetManager
                    .Builder()
                    .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
                    .withSharedPref(appContext, "${name}__keyset", "${name}__keyset_prefs")
                    .withMasterKeyUri("android-keystore://$masterKeyAlias")
                    .build()
                    .keysetHandle
            val aead = keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
            val prefs = appContext.getSharedPreferences("${name}__values", Context.MODE_PRIVATE)
            return AeadSecureStorage(aead, SharedPreferencesKeyValueStore(prefs))
        }
    }
}

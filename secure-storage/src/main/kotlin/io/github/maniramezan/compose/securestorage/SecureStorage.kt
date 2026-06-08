package io.github.maniramezan.compose.securestorage

import android.content.Context

/**
 * A small, Keychain-equivalent secure key–value store for Android.
 *
 * Values are encrypted with an AEAD primitive whose key material is sealed by a master
 * key held in the Android Keystore (hardware-backed where the device supports it), so
 * the ciphertext stored on disk is useless without the device's Keystore. This mirrors
 * the role of iOS Keychain for storing small secrets such as tokens, session metadata,
 * or feature flags that must survive process death.
 *
 * Obtain an instance with [SecureStorage.create]. Treat the returned instance as a
 * singleton per [name]: building the underlying keyset is comparatively expensive and is
 * not meant to be repeated per access.
 *
 * Implementations are safe to call from any thread.
 *
 * **Testing:** depend on this interface rather than [create], and use
 * [InMemorySecureStorage] as the test double so unit tests run on a plain JVM without the
 * Android Keystore. You can also supply your own implementation of this interface.
 */
public interface SecureStorage {
    /** Encrypts and stores [value] under [key], replacing any existing value. */
    public fun putString(
        key: String,
        value: String,
    )

    /** Returns the decrypted value for [key], or `null` if absent or undecryptable. */
    public fun getString(key: String): String?

    /** Encrypts and stores raw [value] bytes under [key], replacing any existing value. */
    public fun putBytes(
        key: String,
        value: ByteArray,
    )

    /** Returns the decrypted bytes for [key], or `null` if absent or undecryptable. */
    public fun getBytes(key: String): ByteArray?

    /** Returns `true` if a value is stored under [key]. */
    public fun contains(key: String): Boolean

    /** Removes the value stored under [key], if any. */
    public fun remove(key: String)

    /** Removes every value held by this store. */
    public fun clear()

    public companion object {
        /** Default backing store name when none is supplied to [create]. */
        public const val DEFAULT_NAME: String = "secure_storage"

        /** Default Android Keystore alias for the master key sealing the keyset. */
        public const val DEFAULT_MASTER_KEY_ALIAS: String = "compose_secure_storage_master_key"

        /**
         * Creates a [SecureStorage] backed by Android Keystore + Tink.
         *
         * @param context any [Context]; the application context is used internally.
         * @param name namespaces the on-disk keyset and values, allowing independent stores.
         * @param masterKeyAlias Android Keystore alias for the AEAD master key.
         */
        public fun create(
            context: Context,
            name: String = DEFAULT_NAME,
            masterKeyAlias: String = DEFAULT_MASTER_KEY_ALIAS,
        ): SecureStorage = AeadSecureStorage.create(context, name, masterKeyAlias)
    }
}

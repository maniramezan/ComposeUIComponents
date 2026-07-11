package io.github.maniramezan.compose.securestorage

import android.content.Context

/**
 * A small, Keychain-equivalent secure key–value store for Android.
 *
 * Values are encrypted with an AES-256-GCM AEAD primitive whose key material (the
 * "keyset") is itself sealed by a master key held in the **Android Keystore**
 * (hardware-backed on devices that support it). The encrypted values are persisted in a
 * private `SharedPreferences` file, so the bytes on disk are useless to anyone who cannot
 * unwrap the keyset via the device's Keystore. This mirrors the role of the iOS Keychain
 * for storing small secrets — auth tokens, session metadata, feature flags — that must
 * survive process death.
 *
 * ### Usage
 * ```kotlin
 * val storage = SecureStorage.create(context)
 * storage.putString("auth.token", token)
 * val token = storage.getString("auth.token")   // null if never stored
 * ```
 *
 * ### Security model
 * - Protects data at rest: an attacker with the raw `SharedPreferences` file cannot
 *   decrypt values without the Keystore-held master key.
 * - Does **not** protect against a compromised process that can call this API, nor does
 *   it add user-presence (biometric) gating. It is for confidentiality of small secrets
 *   at rest, not a full secrets-management solution.
 * - Intended for small values. There is no hard size limit, but large blobs are encrypted
 *   and Base64-encoded in `SharedPreferences` and are a poor fit.
 *
 * ### Lifetime & threading
 * Obtain an instance with [create] and reuse it as a singleton per `name`: building the
 * keyset is comparatively expensive and is not meant to be repeated per access.
 * Implementations are safe to call from any thread.
 *
 * ### Testing
 * Depend on this interface rather than [create], and use [InMemorySecureStorage] as the
 * test double so unit tests run on a plain JVM without the Android Keystore. You may also
 * supply your own implementation of this interface.
 *
 * @see InMemorySecureStorage
 */
public interface SecureStorage {
    /**
     * Encrypts [value] and stores it under [key], replacing any existing value.
     *
     * @param key non-empty identifier; reused verbatim on [getString].
     * @param value plaintext to encrypt; stored as UTF-8 bytes.
     */
    public fun putString(
        key: String,
        value: String,
    )

    /**
     * Decrypts and returns the value previously stored under [key].
     *
     * @return the plaintext, or `null` if nothing is stored under [key] or the stored
     * value cannot be decrypted (e.g. it was tampered with, or the keyset/master key is
     * no longer available). A decryption failure is reported as `null`, never thrown.
     */
    public fun getString(key: String): String?

    /**
     * Encrypts raw [value] bytes and stores them under [key], replacing any existing value.
     *
     * The bytes are copied defensively, so later mutation of [value] does not affect what
     * was stored.
     *
     * @param key non-empty identifier; reused verbatim on [getBytes].
     */
    public fun putBytes(
        key: String,
        value: ByteArray,
    )

    /**
     * Decrypts and returns the bytes previously stored under [key].
     *
     * @return a fresh copy of the plaintext bytes (safe for the caller to mutate), or
     * `null` if nothing is stored under [key] or the value cannot be decrypted.
     */
    public fun getBytes(key: String): ByteArray?

    /** @return `true` if any value is currently stored under [key]. */
    public fun contains(key: String): Boolean

    /** Removes the value stored under [key]. No-op if [key] is absent. */
    public fun remove(key: String)

    /**
     * Removes every value held by this store. Does not delete the underlying keyset, so
     * the store remains usable for subsequent writes.
     */
    public fun clear()

    public companion object {
        /** Default backing store name when none is supplied to [create]. */
        public const val DEFAULT_NAME: String = "secure_storage"

        /** Default Android Keystore alias for the master key sealing the keyset. */
        public const val DEFAULT_MASTER_KEY_ALIAS: String = "compose_secure_storage_master_key"

        /**
         * Creates a Keystore-backed [SecureStorage]. The AEAD keyset is generated on first
         * use and reused thereafter; values persist across app launches.
         *
         * Reuse the returned instance (e.g. as an injected singleton). Calling [create]
         * with the same [name] returns a store over the same persisted data; distinct
         * [name]s give fully isolated stores. Stores that share a [masterKeyAlias] are
         * sealed by the same Keystore master key but keep separate keysets and values.
         *
         * @param context any [Context]; only the application context is retained.
         * @param name namespaces the on-disk keyset and values, allowing independent stores.
         * @param masterKeyAlias Android Keystore alias for the AEAD master key.
         * @return a ready-to-use, persistent secure store.
         * @throws java.io.IOException if the keyset cannot be read or written.
         * @throws java.security.GeneralSecurityException if the keyset or master key is unusable.
         */
        public fun create(
            context: Context,
            name: String = DEFAULT_NAME,
            masterKeyAlias: String = DEFAULT_MASTER_KEY_ALIAS,
        ): SecureStorage = AeadSecureStorage.create(context, name, masterKeyAlias)
    }
}

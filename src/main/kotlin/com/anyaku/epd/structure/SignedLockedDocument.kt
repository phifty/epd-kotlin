package com.anyaku.epd.structure

import com.anyaku.crypt.asymmetric.Key
import com.anyaku.crypt.asymmetric.Signature
import com.anyaku.crypt.PasswordEncryptedKey

/**
 * Contains all the data of a signed and locked document. This includes an id, the public key, the locked contacts and
 * sections, a signature and optionally an encrypted private key. All data is meant to be exposed to the public.
 */
public class SignedLockedDocument(
        id: String,
        publicKey: Key,
        privateKey: PasswordEncryptedKey?,
        val signature: Signature,
        factory: Factory
) : LockedDocument(id, publicKey, privateKey, factory)

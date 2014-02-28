package com.anyaku.epd.structure

import com.anyaku.crypt.PasswordEncryptedKey
import com.anyaku.crypt.asymmetric.Key
import com.anyaku.crypt.asymmetric.Signature

/**
 * Contains all the data of a locked document. This includes an id, the public key, the locked contacts and sections
 * and optionally an encrypted private key.
 */
public open class LockedDocument(
        id: String,
        publicKey: Key,
        privateKey: PasswordEncryptedKey?,
        val factory: Factory
) : Contactable {

    override val id: String = id

    override val publicKey: Key = publicKey

    val privateKey: PasswordEncryptedKey? = privateKey

    val contacts = LockedContactsMap(this)

    val sections = LockedSectionsMap()

}

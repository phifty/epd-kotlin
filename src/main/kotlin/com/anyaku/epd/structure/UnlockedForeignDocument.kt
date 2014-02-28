package com.anyaku.epd.structure

import com.anyaku.crypt.asymmetric.Key

/**
 * Contains all the data of an unlocked foreign document. This includes an id, the public key, the own contact and all
 * sections that could have been unlocked.
 */
public class UnlockedForeignDocument(
        id: String,
        publicKey: Key,
        val factory: Factory
) : Contactable {

    override val id = id

    override val publicKey = publicKey

    val contacts = UnlockedContactsMap(this, factory)

    val sections = UnlockedSectionsMap(contacts, factory)

}

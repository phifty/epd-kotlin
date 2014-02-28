package com.anyaku.epd.structure

import com.anyaku.crypt.asymmetric.Key
import java.util.HashMap

/**
 * Contains all the data of an unlocked document. This includes an id, public and private keys, contacts and sections
 * with the payload data. All data is meant to stay private.
 */
public class UnlockedDocument(
        id: String,
        publicKey: Key,
        var privateKey: Key,
        val factory: Factory
) : Contactable {

    override var id = id

    override var publicKey = publicKey

    val contacts = UnlockedContactsMap(this, factory)

    val sections = UnlockedSectionsMap(contacts, factory)

}

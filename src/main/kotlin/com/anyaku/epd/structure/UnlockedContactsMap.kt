package com.anyaku.epd.structure

import com.anyaku.epd.structure.base.StrictMutableMap
import com.anyaku.epd.structure.base.StrictMutableHashMap
import com.anyaku.crypt.asymmetric.Key

class UnlockedContactsMap(
        private val ownContactable: Contactable,
        private val factory: Factory,
        map: StrictMutableMap<String, UnlockedContact> = StrictMutableHashMap()
) : StrictMutableMap<String, UnlockedContact> by map {

    var ownContact: UnlockedContact
        get() = get(ownContactable.id)
        set(value) { set(ownContactable.id, value) }

    val ownContactId: String
        get() = ownContactable.id

    val ownContactPublicKey: Key
        get() = ownContactable.publicKey

    fun add(contactable: Contactable): String =
            add(contactable.id)

    fun add(contactId: String): String {
        set(contactId, UnlockedContact(factory))
        return contactId
    }

}

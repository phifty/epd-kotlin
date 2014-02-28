package com.anyaku.epd.structure

import com.anyaku.epd.structure.base.StrictMutableMap
import com.anyaku.epd.structure.base.StrictMutableHashMap

class LockedContactsMap(
        contactable: Contactable,
        map: StrictMutableMap<String, LockedContact> = StrictMutableHashMap()
) : StrictMutableMap<String, LockedContact> by map {

    val ownContact: LockedContact
        get() = get(contactable.id)

    private val contactable = contactable

}

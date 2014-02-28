package com.anyaku.epd.structure

import com.anyaku.crypt.coder.decode
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.symmetric.Key
import com.anyaku.epd.structure.base.StrictMutableMap
import com.anyaku.epd.structure.base.StrictMutableHashMap

class UnlockedKeysMap(
        private val factory: Factory,
        map: StrictMutableMap<String, Key> = StrictMutableHashMap()
) : StrictMutableMap<String, Key> by map {

    val privateSectionKey: Key
        get() = get(Sections.PRIVATE_SECTION_ID)

    fun add(id: String): Key {
        val key = factory.buildKey()
        set(id, key)
        return key
    }

}

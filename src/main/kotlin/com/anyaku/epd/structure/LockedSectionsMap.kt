package com.anyaku.epd.structure

import com.anyaku.crypt.coder.decode
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.symmetric.AESEncryptedData
import com.anyaku.epd.structure.base.StrictMutableMap
import com.anyaku.epd.structure.base.StrictMutableHashMap
import com.anyaku.epd.structure.base.StrictMap

class LockedSectionsMap(
        map: StrictMutableMap<String, LockedSection> = StrictMutableHashMap()
) : StrictMutableMap<String, LockedSection> by map {

    var privateSection: LockedSection
        get() = get(Sections.PRIVATE_SECTION_ID)
        set(value) { set(Sections.PRIVATE_SECTION_ID, value) }

    var publicSection: UnlockedSection? = null

    override fun setAll(other: StrictMap<String, LockedSection>) {
        delegationMap.setAll(other)

        if (other is LockedSectionsMap)
            publicSection = other.publicSection
    }

    private val delegationMap = map

}

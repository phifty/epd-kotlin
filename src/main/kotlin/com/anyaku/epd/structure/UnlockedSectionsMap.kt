package com.anyaku.epd.structure

import com.anyaku.util.generateId
import com.anyaku.epd.structure.base.StrictMutableHashMap
import com.anyaku.epd.structure.base.StrictMutableMap

class UnlockedSectionsMap(
        private val contacts: UnlockedContactsMap,
        private val factory: Factory,
        map: StrictMutableMap<String, UnlockedSection> = StrictMutableHashMap()
) : StrictMutableMap<String, UnlockedSection> by map {

    var privateSection: UnlockedSection
        get() = get(Sections.PRIVATE_SECTION_ID)
        set(value) { set(Sections.PRIVATE_SECTION_ID, value) }

    var publicSection: UnlockedSection
        get() = get(Sections.PUBLIC_SECTION_ID)
        set(value) { set(Sections.PUBLIC_SECTION_ID, value) }

    fun addPrivateSection(): UnlockedSection =
            add(Sections.PRIVATE_SECTION_ID, null)

    fun addPublicSection(): UnlockedSection =
            add(Sections.PUBLIC_SECTION_ID, null)

    fun add(title: String): String {
        val id = generateId(8)
        add(id, title)
        return id
    }

    private fun add(id: String, title: String?): UnlockedSection {
        val section = UnlockedSection(id, title, contacts, factory)

        set(id, section)

        if (!Sections.PUBLIC_SECTION_ID.equals(id))
            contacts.ownContact.keys.add(id)

        return section
    }

}

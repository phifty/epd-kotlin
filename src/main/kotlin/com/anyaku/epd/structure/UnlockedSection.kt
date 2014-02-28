package com.anyaku.epd.structure

import java.util.HashMap

class UnlockedSection(
        id: String,
        title: String?,
        private val contacts: UnlockedContactsMap? = null,
        private val factory: Factory
) {

    val id = id

    var title: String? = title

    val modules = UnlockedModulesMap(factory)

    val isPublicSection: Boolean
        get() = Sections.PUBLIC_SECTION_ID.equals(id)

    val isPrivateSection: Boolean
        get() = Sections.PRIVATE_SECTION_ID.equals(id)

    fun addMember(contactId: String) {
        if (contacts == null)
            throw Exception("the section $id is not changable")

        val contact = contacts[contactId]
        contact.keys[id] = contacts.ownContact.keys[id]
        contact.sections.add(id)
    }

    fun equals(other: Any?): Boolean =
            other is UnlockedSection && id == other.id && title == other.title && modules == other.modules

}

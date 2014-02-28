package com.anyaku.epd

import com.anyaku.crypt.asymmetric.Key as AsymmetricKey
import com.anyaku.crypt.combined.decryptAny
import com.anyaku.crypt.symmetric.Key as SymmetricKey
import com.anyaku.crypt.symmetric.decryptAny as symmetricDecryptAny
import com.anyaku.epd.structure.UnlockedDocument
import com.anyaku.epd.structure.LockedDocument
import com.anyaku.epd.structure.Factory
import com.anyaku.epd.structure.UnlockedForeignDocument
import com.anyaku.epd.structure.LockedContact
import com.anyaku.epd.structure.UnlockedContact
import com.anyaku.epd.structure.LockedSectionsMap
import com.anyaku.epd.structure.UnlockedSectionsMap
import com.anyaku.epd.structure.UnlockedKeysMap
import com.anyaku.epd.structure.UnlockedSection
import com.anyaku.epd.structure.LockedSection
import com.anyaku.epd.structure.UnlockedMapper
import com.anyaku.epd.structure.LockedContactsMap
import com.anyaku.epd.structure.UnlockedContactsMap
import com.anyaku.epd.structure.Contactable

/**
 * The ForeignLocker provides functions to partially unlock a [[LockedDocument]] without haveing its private key. In
 * order to do that, the own [[UnlockedDocument]] has to be passed to the constructor. The ForeignLocker will use the
 * private key of that document and unlocks anything that can be unlocked with it.
 */
public class ForeignLocker(private val document: UnlockedDocument) {

    /**
     * Partial unlocks the given document. The function will use the private key of the constructor's
     * [[UnlockedDocument]] to unlock anything possible. The returned [[UnlockedForeignDocument]] will contains all
     * sections of the given [[LockedDocument]] that could have been unlocked plus the public section that don't need
     * to be unlocked.
     */
    fun unlock(lockedDocument: LockedDocument): UnlockedForeignDocument {
        val result = UnlockedForeignDocument(lockedDocument.id, lockedDocument.publicKey, lockedDocument.factory)

        result.contacts.setAll(unlockContacts(lockedDocument.contacts, lockedDocument, lockedDocument.factory))
        result.sections.setAll(unlockSections(lockedDocument.sections, result.contacts, lockedDocument.factory))

        return result
    }

    /*
     * Unlocks the given contacts.
     */
    [ suppress("UNCHECKED_CAST") ]
    private fun unlockContacts(
            contacts: LockedContactsMap,
            contactable: Contactable,
            factory: Factory
    ): UnlockedContactsMap {
        val result = UnlockedContactsMap(contactable, factory)

        if (contacts.containsKey(document.id))
            result[ document.id ] =
                unlockContact(contacts[ document.id ], document.privateKey, factory)

        return result
    }

    /*
     * Unlocks the given contact with the given private key.
     */
    [ suppress("UNCHECKED_CAST") ]
    private fun unlockContact(
            lockedContact: LockedContact,
            privateKey: AsymmetricKey,
            factory: Factory
    ): UnlockedContact {
        val result = UnlockedContact(factory)
        val unlockedMapper = factory.buildUnlockedMapper()

        result.keys.setAll(
                unlockedMapper.keysFromMap(decryptAny(lockedContact.keys, privateKey) as MutableMap<String, Any?>))
        result.sections = result.keys.keySet()

        return result
    }

    /*
     * Unlocks the given sections.
     */
    private fun unlockSections(
            sections: LockedSectionsMap,
            contacts: UnlockedContactsMap,
            factory: Factory
    ): UnlockedSectionsMap {
        val unlockedMapper = factory.buildUnlockedMapper()
        val result = UnlockedSectionsMap(contacts, factory)

        result.publicSection = sections.publicSection as UnlockedSection

        if (contacts.containsKey(document.id))
            for (entry in contacts[ document.id ].keys.entrySet())
                result[ entry.key ] =
                    unlockSection(sections[ entry.key ], entry.key, entry.value, unlockedMapper)

        return result
    }

    /*
     * Unlocks the given section with the given key.
     */
    [ suppress("UNCHECKED_CAST") ]
    private fun unlockSection(
            lockedSection: LockedSection,
            id: String,
            key: SymmetricKey,
            unlockedMapper: UnlockedMapper
    ): UnlockedSection =
            unlockedMapper.sectionFromMap(
                    id,
                    symmetricDecryptAny(lockedSection.encrypted, key) as Map<String, Any?>,
                    null)

}

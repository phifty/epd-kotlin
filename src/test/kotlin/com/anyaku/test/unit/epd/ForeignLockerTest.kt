package com.anyaku.test.unit.epd

import com.anyaku.epd.Generator
import com.anyaku.epd.ForeignLocker
import com.anyaku.epd.Locker
import kotlin.properties.Delegates
import org.junit.Assert.assertEquals
import org.junit.Test
import com.anyaku.epd.PublicKeyResolver
import com.anyaku.crypt.asymmetric.Key

class ForeignLockerTest {

    val generator = Generator()
    val locker = Locker(object : PublicKeyResolver {
        override fun resolve(documentId: String): Key? =
                when (documentId) {
                    document.id -> document.publicKey
//                    foreignDocument.id -> foreignDocument.publicKey
//                    otherForeignDocument.id -> otherForeignDocument.publicKey
                    else -> throw UnsupportedOperationException()
                }
    })

    val document by Delegates.lazy { generator.document() }
    val foreignDocument by Delegates.lazy {
        val result = generator.document()
        val contactId = result.contacts.add(document)
        val sectionId = result.sections.add("Friends")
        result.sections[sectionId].addMember(contactId)
        result
    }
    val otherForeignDocument by Delegates.lazy { generator.document() }

    val foreignLocker by Delegates.lazy { ForeignLocker(document) }

    val friendsSectionId by Delegates.lazy {
        foreignDocument.sections.entrySet().filter { (entry)->
            !entry.value.isPublicSection && !entry.value.isPrivateSection
        }.first?.key as String
    }

    val password by Delegates.lazy { generator.password("Password123") }
    val lockedForeignDocument by Delegates.lazy {
        val encryptedPrivateKey = locker.lock(foreignDocument.privateKey, password)
        locker.lock(foreignDocument, encryptedPrivateKey)
    }
    val otherLockedForeignDocument by Delegates.lazy {
        val encryptedPrivateKey = locker.lock(otherForeignDocument.privateKey, password)
        locker.lock(otherForeignDocument, encryptedPrivateKey)
    }

    val unlockedForeignDocument by Delegates.lazy { foreignLocker.unlock(lockedForeignDocument) }
    val otherUnlockedForeignDocument by Delegates.lazy { foreignLocker.unlock(otherLockedForeignDocument) }

    [ Test ]
    fun testIfUnlockedDocumentHasTheRightId() {
        assertEquals(foreignDocument.id, unlockedForeignDocument.id)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasTheRightPublicKey() {
        assertEquals(foreignDocument.publicKey, unlockedForeignDocument.publicKey)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasTheRightContactKeys() {
        assertEquals(
                foreignDocument.contacts.ownContact.keys[friendsSectionId],
                unlockedForeignDocument.contacts[document.id].keys[friendsSectionId])
    }

    [ Test ]
    fun testIfUnlockedDocumentHasThePublicSection() {
        assertEquals(foreignDocument.sections.publicSection, unlockedForeignDocument.sections.publicSection)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasTheFriendsSection() {
        assertEquals(foreignDocument.sections[friendsSectionId], unlockedForeignDocument.sections[friendsSectionId])
    }

    [ Test ]
    fun testIfItCanHandleDocumentsThatDoNotHaveAnythingToUnlock() {
        assertEquals(1, otherUnlockedForeignDocument.sections.size())
    }

}

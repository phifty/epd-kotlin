package com.anyaku.test.unit.epd

import com.anyaku.crypt.asymmetric.Key
import com.anyaku.epd.Generator
import com.anyaku.epd.Locker
import com.anyaku.epd.PublicKeyResolver
import com.anyaku.epd.structure.Sections
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LockerTest {

    val generator = Generator()
    val locker = Locker(object : PublicKeyResolver {
        override fun resolve(documentId: String): Key? {
            throw UnsupportedOperationException()
        }
    })
    val document = { ->
        val result = generator.document()
        result.sections.add("Friends")
        result
    }()
    val friendsSectionId = document.sections.entrySet().filter { (entry)->
        !entry.value.isPublicSection && !entry.value.isPrivateSection
    }.first?.key!!

    val password = generator.password("Password123")

    val encryptedPrivateKey = locker.lock(document.privateKey, password)
    val lockedDocument = locker.lock(document, encryptedPrivateKey)
    val lockedDocumentWithoutPrivateKey = locker.lock(document)

    val decryptedPrivateKey = locker.unlock(encryptedPrivateKey, password)
    val unlockedDocument = locker.unlock(lockedDocument, decryptedPrivateKey)

    [ Test ]
    fun testIfLockedDocumentHasTheRightId() {
        assertEquals(document.id, lockedDocument.id)
    }

    [ Test ]
    fun testIfLockedDocumentHasTheRightPublicKey() {
        assertEquals(document.publicKey, lockedDocument.publicKey)
    }

    [ Test ]
    fun testIfLockedDocumentHasTheAnEncryptedPrivateKey() {
        assertEquals(720, lockedDocument.privateKey!!.encrypted.data.size)
        assertNull(lockedDocumentWithoutPrivateKey.privateKey)
    }

    [ Test ]
    fun testIfLockedDocumentHasThePublicSection() {
        assertEquals(document.sections.publicSection, lockedDocument.sections.publicSection)
    }

    [ Test ]
    fun testIfLockedDocumentHasThePrivateSection() {
        assertTrue(lockedDocument.sections.keySet().contains(Sections.PRIVATE_SECTION_ID))
    }

    [ Test ]
    fun testIfLockedDocumentHasTheOtherSections() {
        assertTrue(lockedDocument.sections.keySet().contains(friendsSectionId))
    }

    [ Test ]
    fun testIfLockedDocumentHasOwnContact() {
        assertTrue(lockedDocument.contacts.keySet().contains(document.id))
    }

    [ Test ]
    fun testIfLockedDocumentHasSignature() {
        assertEquals(256, lockedDocument.signature.data.size)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasTheRightId() {
        assertEquals(document.id, unlockedDocument.id)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasTheRightPublicKey() {
        assertEquals(document.publicKey, unlockedDocument.publicKey)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasTheRightPrivateKey() {
        assertEquals(document.privateKey, unlockedDocument.privateKey)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasTheRightOwnContact() {
        assertEquals(document.contacts.ownContact, unlockedDocument.contacts.ownContact)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasThePublicSection() {
        assertEquals(document.sections.publicSection, unlockedDocument.sections.publicSection)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasThePrivateSection() {
        assertEquals(document.sections.privateSection, unlockedDocument.sections.privateSection)
    }

    [ Test ]
    fun testIfUnlockedDocumentHasTheFriendsSection() {
        assertEquals(document.sections[friendsSectionId], unlockedDocument.sections[friendsSectionId])
    }

}

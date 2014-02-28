package com.anyaku.test.unit.epd

import com.anyaku.epd.Generator
import com.anyaku.epd.structure.modules.Basic
import com.anyaku.epd.structure.Sections
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import com.anyaku.crypt.HashParameters
import com.anyaku.crypt.coder.base64Encode

class GeneratorTest {

    val generator = Generator()
    val generatedDocument = generator.document()

    [ Test ]
    fun testIfGeneratedDocumentHasAnId() {
        assertEquals(32, generatedDocument.id.length())
    }

    [ Test ]
    fun testIfGeneratedDocumentHasAllKeys() {
        assertNotNull(generatedDocument.publicKey)
        assertNotNull(generatedDocument.privateKey)
    }

    [ Test ]
    fun testIfGeneratedDocumentHasOwnContact() {
        assertTrue(generatedDocument.contacts.keySet().contains(generatedDocument.id))
    }

    [ Test ]
    fun testIfGeneratedDocumentHasPrivateSection() {
        assertTrue(generatedDocument.sections.keySet().contains(Sections.PRIVATE_SECTION_ID))
    }

    [ Test ]
    fun testIfGeneratedDocumentHasAKeyForThePrivateSection() {
        assertTrue(generatedDocument.contacts.ownContact.keys.keySet().contains(Sections.PRIVATE_SECTION_ID))
    }

    [ Test ]
    fun testIfGeneratedDocumentHasPublicSection() {
        assertTrue(generatedDocument.sections.keySet().contains(Sections.PUBLIC_SECTION_ID))
    }

    [ Test ]
    fun testIfGeneratedDocumentHasBasicModuleInPublicSection() {
        assertTrue(generatedDocument.sections.publicSection.modules.keySet().contains(Basic.id))
    }

    [ Test ]
    fun testIfGeneratedPasswordHasTheRightSize() {
        val generatedPassword = generator.password("Password123")
        assertEquals(40, generatedPassword.hash.size)
    }

    [ Test ]
    fun testIfGeneratedPasswordForASpecificSecurityLevel() {
        val generatedPassword = generator.password("Password123", HashParameters.forSecurityLevel(1))
        assertEquals(40, generatedPassword.hash.size)
    }

}

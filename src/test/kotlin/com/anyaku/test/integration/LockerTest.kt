package com.anyaku.test.integration

import com.anyaku.epd.Generator
import com.anyaku.epd.Locker
import com.anyaku.epd.Locker.InvalidPasswordException
import com.anyaku.epd.json.JsonBuilder
import com.anyaku.test.fixtures.Documents
import kotlin.test.assertEquals
import kotlin.test.failsWith
import org.junit.Test

class LockerTest {

    val generator = Generator()
    val builder = JsonBuilder()
    val locker = Locker(Documents.publicKeyResolver)
    val sampleDocument = builder.buildLockedDocument(Documents.json["jenny"])

    [ Test ]
    fun testUnlockingADocument() {
        val password = generator.password("Password123", sampleDocument.privateKey!!.hashParameters)
        val privateKey = locker.unlock(sampleDocument.privateKey, password)
        val unlockedDocument = locker.unlock(sampleDocument, privateKey)
        assertEquals(sampleDocument.id, unlockedDocument.id)
        assertEquals(sampleDocument.publicKey, unlockedDocument.publicKey)
        assertEquals(2, unlockedDocument.sections.size())
    }

    [ Test ]
    fun testIfInvalidPasswordCausesRightException() {
        val password = generator.password("invalid")
        failsWith(javaClass<InvalidPasswordException>()) {
            locker.unlock(sampleDocument.privateKey!!, password)
        }
    }

}

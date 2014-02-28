package com.anyaku.test.epd

import com.anyaku.crypt.decrypt
import com.anyaku.epd.Generator
import com.anyaku.epd.Signer
import com.anyaku.test.fixtures.Documents
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SignerTest() {

    val generator = Generator()
    val signer = Signer()
    val sampleDocument = Documents.locked["jenny"]

    [ Test ]
    fun testIfAValidDocumentIsDetectedAsValid() {
        assertTrue(signer.verify(sampleDocument))
    }

    [ Test ]
    fun testIfTheSignatureGetsReproduced() {
        val encryptedPrivateKey = sampleDocument.privateKey!!
        val password = generator.password("Password123",
                encryptedPrivateKey.hashParameters)
        val privateKey = decrypt(encryptedPrivateKey, password)

        val oldSignature = sampleDocument.signature
        signer.sign(sampleDocument, privateKey)
        assertEquals(oldSignature, sampleDocument.signature)
    }

}

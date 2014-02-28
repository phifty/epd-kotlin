package com.anyaku.test.integration

import com.anyaku.crypt.asymmetric.decrypt
import com.anyaku.crypt.asymmetric.RSAEncryptedData
import com.anyaku.crypt.asymmetric.RSAKeyPair
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.coder.decode
import com.anyaku.test.integration.javascript.runInWorker
import org.junit.Assert.assertEquals
import org.junit.Test

class RSAEncryptionTest() {

    val keySize = 2048
    val keyPair = RSAKeyPair(keySize)
    val message = "message"

    [ Test ]
    fun testKeyPairGeneration() {
        runInWorker("var publicKey = epdRoot.Crypt.Coder.decode('${encode(keyPair.publicKey)}');")
        runInWorker("var encryptedMessage = epdRoot.Crypt.Asymmetric.encrypt('$message', publicKey);")

        val encryptedMessage =
                decode(runInWorker("epdRoot.Crypt.Coder.encode(encryptedMessage);") as String) as RSAEncryptedData

        val decryptedMessage = String(decrypt(encryptedMessage, keyPair.privateKey))
        assertEquals(message, decryptedMessage)
    }

}

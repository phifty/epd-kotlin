package com.anyaku.test.unit.crypt.asymmetric

import com.anyaku.crypt.asymmetric.RSAKeyPair
import com.anyaku.crypt.asymmetric.encrypt
import com.anyaku.crypt.asymmetric.decrypt
import org.junit.Test
import org.junit.Assert.assertEquals

class asymmetricTest {

    val keyPair = RSAKeyPair()
    val message = "test test test"

    [ Test ]
    fun testEncryption() {
        val encrypted = encrypt(message.getBytes(), keyPair.publicKey)
        assertEquals(256, encrypted.data.size)
    }

    [ Test ]
    fun testDecryption() {
        val encrypted = encrypt(message.getBytes(), keyPair.publicKey)
        val decrypted = decrypt(encrypted, keyPair.privateKey)
        assertEquals(message, String(decrypted))
    }

}

package com.anyaku.test.crypt

import org.junit.Assert.assertEquals
import org.junit.Test
import com.anyaku.crypt.generatePassword
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.asymmetric.RSAKeyPair
import com.anyaku.crypt.encrypt
import com.anyaku.crypt.decrypt
import com.anyaku.crypt.randomBytes
import com.anyaku.crypt.coder.base64Encode
import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.crypt.HashParameters

class cryptTest {

    val hashParameters = HashParameters("salt".getBytes(), 1000, 256)
    val password = generatePassword("test", hashParameters)
    val keyPair = RSAKeyPair()

    [ Test ]
    fun testRandomBytes() {
        assertEquals(14, randomBytes(14).size)
    }

    [ Test ]
    fun testPasswordHash() {
        assertEquals("tiLwdeXRFpygmps/Ctuofgb0OWSuFIWcfThlwIbH7kgNA6YcaOGiqA==", base64Encode(password.hash))
    }

    [ Test ]
    fun testEncryptKeyWithPassword() {
        val encryptedKey = encrypt(keyPair.privateKey, password)
        assertEquals(720, encryptedKey.encrypted.data.size)
    }

    [ Test ]
    fun testDecryptKeyWithPassword() {
        val encryptedKey = encrypt(keyPair.privateKey, password)
        val decryptedKey = decrypt(encryptedKey, password) as RSAKey
        assertEquals(keyPair.privateKey.modulus, decryptedKey.modulus)
        assertEquals(keyPair.privateKey.exponent, decryptedKey.exponent)
    }

}

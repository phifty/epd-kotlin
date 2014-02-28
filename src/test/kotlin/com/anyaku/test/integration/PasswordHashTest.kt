package com.anyaku.test.integration

import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.coder.decode
import com.anyaku.crypt.PasswordSalt
import com.anyaku.crypt.symmetric.AESKey
import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.crypt.decrypt
import com.anyaku.crypt.HashParameters
import com.anyaku.epd.Generator
import com.anyaku.test.fixtures.Documents
import com.anyaku.test.integration.javascript.runInWorker
import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordHashTest {

    val generator = Generator()
    val sampleProfile = Documents.locked["helen"]
    val iterations = 1000
    val keySize = 160

    {
        runInWorker("password = epdRoot.Crypt.Password.hash('Password123', { iterations: $iterations, keySize: ${keySize / 32} });")
    }

    [ Test ]
    fun testPasswordHashing() {
        val javascriptHash = runInWorker("epdRoot.Crypt.Coder.encode(password.hash);") as String
        val javascriptSalt = runInWorker("epdRoot.Crypt.Coder.encode(password.salt);") as String
        val javascriptIterations = runInWorker("password.iterations;")
        val javascriptKeySize = runInWorker("password.keySize;")

        assertEquals(javascriptIterations, iterations)
        assertEquals(javascriptKeySize, keySize / 32)

        val password = generator.password("Password123",
                                          HashParameters(
                                                  (decode(javascriptSalt) as PasswordSalt).data,
                                                  iterations,
                                                  keySize))
        assertEquals(javascriptHash, encode(AESKey(password.hash)));
    }

    [ Test ]
    fun testPrivateKeyDecryption() {
        val encryptedPrivateKey = sampleProfile.privateKey!!
        val password = generator.password("Password123",
                encryptedPrivateKey.hashParameters)

        val decryptedPrivateKey = decrypt(encryptedPrivateKey, password) as RSAKey
        assertEquals(181, decryptedPrivateKey.toPrivateKey().getEncoded()?.size)
    }

}

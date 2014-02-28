package com.anyaku.test.integration

import com.anyaku.crypt.coder.decode
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.symmetric.Key
import com.anyaku.crypt.symmetric.EncryptedData
import com.anyaku.crypt.symmetric.decrypt
import com.anyaku.crypt.symmetric.encrypt
import com.anyaku.test.integration.javascript.runInWorker
import org.junit.Assert.assertEquals
import org.junit.Test

class SymmetricEncryptionTest {

    val encodedKey = runInWorker(
            "var key = epdRoot.Crypt.Symmetric.generateKey();" +
            "var encode = epdRoot.Crypt.Coder.encode;" +
            "var decode = epdRoot.Crypt.Coder.decode;" +
            "var encrypt = epdRoot.Crypt.Symmetric.encrypt;" +
            "var decrypt = epdRoot.Crypt.Symmetric.decrypt;" +
            "encode(key);") as String

    val key = decode(encodedKey) as Key

    [ Test ]
    fun testEncryptionWithAKey() {
        val encryptedMessage = encrypt("message".getBytes(), key)
        val decryptedMessage = runInWorker("decrypt(decode('${encode(encryptedMessage)}'), key);") as String

        assertEquals("message", decryptedMessage)
    }

    [ Test ]
    fun testDecryptionWithAKey() {
        val encryptedMessage = runInWorker("encode(encrypt('message', key));") as String
        val decryptedMessage = String(decrypt(decode(encryptedMessage) as EncryptedData, key))

        assertEquals("message", decryptedMessage)
    }

}

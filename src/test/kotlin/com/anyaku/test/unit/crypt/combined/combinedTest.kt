package com.anyaku.test.unit.crypt.combined

import com.anyaku.crypt.asymmetric.RSAKeyPair
import com.anyaku.crypt.combined.encrypt
import com.anyaku.crypt.combined.decrypt
import kotlin.test.assertEquals
import java.util.HashMap
import org.junit.Test as test
import com.anyaku.crypt.combined.encryptAny
import com.anyaku.crypt.combined.decryptAny

class combinedTest {

    val keyPair = RSAKeyPair()
    val message = "test test test"
    val map = { (): Map<String, Any?> ->
        val result = HashMap<String, Any?>()
        result.put("test", "value")
        result
    }()

    test fun testDataEncryption() {
        val encrypted = encrypt(message.getBytes(), keyPair.publicKey)
        assertEquals(256, encrypted.key.data.size)
        assertEquals(32, encrypted.data.data.size)
    }

    test fun testDataDecryption() {
        val encrypted = encrypt(message.getBytes(), keyPair.publicKey)
        val decrypted = decrypt(encrypted, keyPair.privateKey)
        assertEquals(message, String(decrypted))
    }

    test fun testMapEncryption() {
        val encrypted = encryptAny(map, keyPair.publicKey)
        assertEquals(256, encrypted.key.data.size)
        assertEquals(48, encrypted.data.data.size)
    }

    [ suppress("UNCHECKED_CAST") ]
    test fun testMapDecryption() {
        val encrypted = encryptAny(map, keyPair.publicKey)
        val decrypted = decryptAny(encrypted, keyPair.privateKey) as Map<String, Any?>
        assertEquals(map.get("test"), decrypted.get("test"))
    }

}

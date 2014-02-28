package com.anyaku.test.unit.crypt.symmetric

import com.anyaku.crypt.symmetric.generateKey
import com.anyaku.crypt.symmetric.encrypt
import com.anyaku.crypt.symmetric.decrypt
import com.anyaku.crypt.symmetric.decryptAny
import com.anyaku.crypt.symmetric.encryptAny
import java.util.HashMap
import kotlin.test.assertEquals
import org.junit.Test as test

class symmetricTest {

    val key = generateKey()
    val message = "test test test test test"
    val map = { (): Map<String, Any?> ->
        val result = HashMap<String, Any?>()
        result.put("test", "value")
        result
    }()

    test fun testKeyGeneration() {
        assertEquals(16, key.data.size)
    }

    test fun testDataEncryption() {
        val encrypted = encrypt(message.getBytes(), key)
        assertEquals(48, encrypted.data.size)
    }

    test fun testDataDecryption() {
        val encrypted = encrypt(message.getBytes(), key)
        val decrypted = decrypt(encrypted, key)
        assertEquals(message, String(decrypted))
    }

    test fun testMapEncryption() {
        val encrypted = encryptAny(map, key)
        assertEquals(48, encrypted.data.size)
    }

    test fun testMapDecryption() {
        val encrypted = encryptAny(map, key)
        val decrypted = decryptAny(encrypted, key)
        assertEquals(map, decrypted)
    }

}

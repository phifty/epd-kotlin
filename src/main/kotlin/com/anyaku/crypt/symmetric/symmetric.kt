package com.anyaku.crypt.symmetric

import com.anyaku.json.writeJson
import com.anyaku.json.parseJson
import com.anyaku.crypt.randomBytes
import com.anyaku.crypt.coder.base64Encode
import java.security.spec.AlgorithmParameterSpec
import java.security.MessageDigest
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun generateKey(): Key {
    keyGenerator.init(128)
    val key = keyGenerator.generateKey()
    return AESKey(key!!.getEncoded() as ByteArray)
}

fun encrypt(data: ByteArray, key: Key): EncryptedData =
        when {
            key is AESKey -> {
                val salt = randomBytes(8)
                val keyAndIv = deriveKeyAndIv(base64Encode(key.data).getBytes(), salt, 32, 16)

                cipher.init(Cipher.ENCRYPT_MODE,
                        SecretKeySpec(keyAndIv.first, "AES"),
                        IvParameterSpec(keyAndIv.second))

                val payload = cipher.doFinal(data) as ByteArray

                val result = AESEncryptedData(ByteArray(openSSLHeader.size + salt.size + payload.size))
                result.header = true
                result.salt = salt
                result.payload = payload
                result
            }
            else -> throw Exception("key type is not supported")
        }

fun encryptAny(data: Any?, key: Key): EncryptedData =
        encrypt(writeJson(data).getBytes(), key)

fun decrypt(encrypted: EncryptedData, key: Key): ByteArray =
        when {
            encrypted is AESEncryptedData && key is AESKey -> {
                if (!encrypted.header)
                    throw Exception("OpenSSL header can't be found")

                val keyAndIv = deriveKeyAndIv(base64Encode(key.data).getBytes(), encrypted.salt, 32, 16)

                cipher.init(Cipher.DECRYPT_MODE,
                        SecretKeySpec(keyAndIv.first, "AES"),
                        IvParameterSpec(keyAndIv.second))

                cipher.doFinal(encrypted.payload) as ByteArray
            }
            else -> throw Exception("data or key type is not supported")
        }

fun decryptAny(encrypted: EncryptedData, key: Key): Any? =
        parseJson(String(decrypt(encrypted, key)))

private fun deriveKeyAndIv(password: ByteArray, salt: ByteArray, keySize: Int, ivSize: Int): Pair<ByteArray, ByteArray> {
    var result = ByteArray(0)
    var block = ByteArray(0)

    while (result.size < keySize + ivSize) {
        block = messageDigest.digest(combine(block, password, salt)) as ByteArray
        result = combine(result, block)
    }

    return Pair(Arrays.copyOfRange(result, 0, keySize), Arrays.copyOfRange(result, keySize, keySize + ivSize))
}

private fun combine(one: ByteArray, two: ByteArray, three: ByteArray): ByteArray {
    return combine(combine(one, two), three)
}

private fun combine(one: ByteArray, two: ByteArray): ByteArray {
    val result = ByteArray(one.size + two.size)
    System.arraycopy(one, 0, result, 0, one.size)
    System.arraycopy(two, 0, result, one.size, two.size)
    return result
}

private val keyGenerator = KeyGenerator.getInstance("AES") as KeyGenerator

private val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding") as Cipher

private val messageDigest = MessageDigest.getInstance("MD5")

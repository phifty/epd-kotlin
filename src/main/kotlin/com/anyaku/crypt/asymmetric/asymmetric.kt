package com.anyaku.crypt.asymmetric

import java.security.KeyFactory
import javax.crypto.Cipher
import java.security.spec.RSAPublicKeySpec
import java.security.spec.RSAPrivateKeySpec

fun encrypt(data: ByteArray, key: Key): EncryptedData =
        when {
            key is RSAKey -> {
                val keySpec = RSAPublicKeySpec(key.modulus, key.exponent)
                val publicKey = keyFactory.generatePublic(keySpec)
                cipher.init(Cipher.ENCRYPT_MODE, publicKey)
                RSAEncryptedData(cipher.doFinal(data) as ByteArray)
            }
            else -> throw Exception("key type is not supported")
        }

fun decrypt(encrypted: EncryptedData, key: Key): ByteArray =
        when {
            encrypted is RSAEncryptedData && key is RSAKey -> {
                val keySpec = RSAPrivateKeySpec(key.modulus, key.exponent)
                val privateKey = keyFactory.generatePrivate(keySpec)
                cipher.init(Cipher.DECRYPT_MODE, privateKey)
                cipher.doFinal(encrypted.data) as ByteArray
            }
            else -> throw Exception("data or key type is not supported")
        }

private val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding") as Cipher

private val keyFactory = KeyFactory.getInstance("RSA")

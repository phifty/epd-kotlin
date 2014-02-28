package com.anyaku.crypt

import com.anyaku.crypt.PasswordEncryptedKey
import com.anyaku.crypt.symmetric.AESKey
import com.anyaku.crypt.symmetric.encrypt as symmetricEncrypt
import com.anyaku.crypt.symmetric.decrypt as symmetricDecrypt
import com.anyaku.crypt.asymmetric.Key as AsymmetricKey
import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.coder.decode
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.SecretKey
import javax.crypto.spec.PBEKeySpec
import com.anyaku.crypt.coder.base64Encode

fun randomBytes(count: Int): ByteArray {
    val result = ByteArray(count)
    secureRandom.nextBytes(result)
    return result
}

fun generatePassword(value: String): Password =
        generatePassword(value, HashParameters.default)

fun generatePassword(value: String, hashParameters: HashParameters): Password {
    // the key size has to be corrected, because the CryptoJS PBKDF2 function result was handled in a wrong ways
    val correctedKeySize = Math.ceil((hashParameters.keySize / 32) / 5.0).toInt() * 5 * 32

    val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1") as SecretKeyFactory
    val spec = PBEKeySpec(value.toCharArray(), hashParameters.salt, hashParameters.iterations, correctedKeySize)
    val secretKey = secretKeyFactory.generateSecret(spec) as SecretKey
    return Password(secretKey.getEncoded() as ByteArray, hashParameters)
}

fun encrypt(key: AsymmetricKey, password: Password): PasswordEncryptedKey {
    val data = symmetricEncrypt(encode(key).getBytes(), AESKey(password.hash))
    return PasswordEncryptedKey(data, password.hashParameters)
}

fun decrypt(key: PasswordEncryptedKey, password: Password): AsymmetricKey {
    val data = symmetricDecrypt(key.encrypted, AESKey(password.hash))
    return decode(String(data)) as RSAKey
}

private val secureRandom = SecureRandom()

package com.anyaku.crypt.combined

import com.anyaku.crypt.asymmetric.encrypt as asymmetricEncrypt
import com.anyaku.crypt.asymmetric.decrypt as asymmetricDecrypt
import com.anyaku.crypt.asymmetric.Key
import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.crypt.asymmetric.RSAEncryptedData
import com.anyaku.crypt.combined.RSAAESEncryptedData
import com.anyaku.crypt.symmetric.generateKey
import com.anyaku.crypt.symmetric.encrypt as symmetricEncrypt
import com.anyaku.crypt.symmetric.decrypt as symmetricDecrypt
import com.anyaku.crypt.symmetric.AESEncryptedData
import com.anyaku.crypt.symmetric.Key as SymmetricKey
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.coder.decode
import com.anyaku.json.parseJson
import com.anyaku.json.writeJson

fun encrypt(data: ByteArray, key: Key): EncryptedData =
        when {
            key is RSAKey -> {
                val symmetricKey = generateKey()
                val encryptedKey = asymmetricEncrypt(encode(symmetricKey).getBytes(), key)
                val encryptedData = symmetricEncrypt(data, symmetricKey)
                RSAAESEncryptedData(encryptedKey, encryptedData)
            }
            else -> throw Exception("key type is not supported")
        }

fun encryptAny(data: Any?, key: Key): EncryptedData =
        encrypt(writeJson(data).getBytes(), key)

fun decrypt(encrypted: EncryptedData, key: Key): ByteArray =
        when {
            encrypted is RSAAESEncryptedData && key is RSAKey -> {
                val symmetricKey = decode(String(asymmetricDecrypt(encrypted.key, key))) as SymmetricKey
                symmetricDecrypt(encrypted.data, symmetricKey)
            }
            else -> throw Exception("data or key type is not supported")
        }

fun decryptAny(encrypted: EncryptedData, key: Key): Any? =
        parseJson(String(decrypt(encrypted, key)))

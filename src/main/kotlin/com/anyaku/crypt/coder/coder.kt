package com.anyaku.crypt.coder

import com.anyaku.crypt.asymmetric.RSAEncryptedData
import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.crypt.asymmetric.RSASignature
import com.anyaku.crypt.combined.RSAAESEncryptedData
import com.anyaku.crypt.symmetric.AESKey
import com.anyaku.crypt.symmetric.AESEncryptedData
import com.anyaku.crypt.PasswordSalt
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Method
import java.math.BigInteger
import java.util.Arrays

/**
 * Decodes a string into different kinds of binary objects. The type of the object
 * is determined by the first character of the string.
 *
 * Mapping:
 * 'A' - RSAKey                 base64 encoded modulus and exponent of a RSA key
 * 'B' - RSAEncryptedData       hexadecimal encoded RSA encrypted data
 * 'C' - RSASignature           base64 encoded RSA signature
 * 'D' - AESKey                 base64 encoded AES key
 * 'E' - AESEncryptedData       base64 encoded AES encrypted data
 * 'F' - PasswordSalt           base64 encoded password salt
 * 'G' - RSAAESEncryptedData    a RSA encrypted AES key and AES encrypted data separated with a '|' character
 */
fun decode(encoded: String): Any {
    val code = encoded.substring(0, 1)
    val data = encoded.substring(1)
    return when (code) {
        "A" -> decodeRSAKey(data)
        "B" -> decodeRSAEncryptedData(data)
        "C" -> decodeRSASignature(data)
        "D" -> decodeAESKey(data)
        "E" -> decodeAESEncryptedData(data)
        "F" -> decodePasswordSalt(data)
        "G" -> decodeRSAAESEncryptedData(data)
        else -> throw Exception("unknown type to decode '$code'")
    }
}

/**
 * Encodes a binary object into a string. The type information of the object is placed
 * at the beginning of the string.
 *
 * For information about the mapping see the documentation of the @see decode method.
 */
fun encode(value: Any): String =
        when (value) {
            is RSAKey -> "A" + encodeRSAKey(value)
            is RSAEncryptedData -> "B" + encodeRSAEncryptedData(value)
            is RSASignature -> "C" + encodeRSASignature(value)
            is AESKey -> "D" + encodeAESKey(value)
            is AESEncryptedData -> "E" + encodeAESEncryptedData(value)
            is PasswordSalt -> "F" + encodePasswordSalt(value)
            is RSAAESEncryptedData -> "G" + encodeRSAAESEncryptedData(value)
            else -> throw Exception("unknown type to encode '${value.javaClass}'")
        }

fun base64Encode(value: ByteArray): String =
        base64Coder.encode(value)

fun base64Decode(encoded: String): ByteArray =
        base64Coder.decode(encoded)

fun hexEncode(value: ByteArray): String =
        hexCoder.encode(value)

fun hexDecode(encoded: String): ByteArray =
        hexCoder.decode(encoded)

private fun decodeRSAKey(encoded: String): RSAKey {
    val stream = ByteArrayInputStream(base64Decode(encoded))

    val modulus = BigInteger(1, readBlock(stream))
    val exponent = BigInteger(1, readBlock(stream))

    return RSAKey(modulus, exponent)
}

private fun encodeRSAKey(key: RSAKey): String {
    val stream = ByteArrayOutputStream()

    val modulusData = fillLeadingZeros(removeLeadingZeros(key.modulus.toByteArray()))
    val exponentData = fillLeadingZeros(removeLeadingZeros(key.exponent.toByteArray()))

    writeBlock(stream, modulusData)
    writeBlock(stream, exponentData)

    return base64Encode(stream.toByteArray())
}

private fun decodeRSAEncryptedData(encoded: String): RSAEncryptedData =
        RSAEncryptedData(hexDecode(encoded))

private fun encodeRSAEncryptedData(encrypted: RSAEncryptedData): String =
        hexEncode(encrypted.data)

private fun decodeRSASignature(encoded: String): RSASignature =
        RSASignature(base64Decode(encoded))

private fun encodeRSASignature(signature: RSASignature): String =
        base64Encode(signature.data)

private fun decodeAESKey(encoded: String): AESKey =
        AESKey(base64Decode(encoded))

private fun encodeAESKey(key: AESKey): String =
        base64Encode(key.data)

private fun decodeAESEncryptedData(encoded: String): AESEncryptedData =
        AESEncryptedData(base64Decode(encoded))

private fun encodeAESEncryptedData(encrypted: AESEncryptedData): String =
        base64Encode(encrypted.data)

private fun decodePasswordSalt(encoded: String): PasswordSalt =
        PasswordSalt(base64Decode(encoded))

private fun encodePasswordSalt(salt: PasswordSalt): String =
        base64Encode(salt.data)

private fun decodeRSAAESEncryptedData(encoded: String): RSAAESEncryptedData {
    val separateAt = encoded.indexOf('|')
    val key = decode(encoded.substring(0, separateAt)) as RSAEncryptedData
    val data = decode(encoded.substring(separateAt + 1)) as AESEncryptedData
    return RSAAESEncryptedData(key, data)
}

private fun encodeRSAAESEncryptedData(encrypted: RSAAESEncryptedData): String =
        encode(encrypted.key) + "|" + encode(encrypted.data)

private fun readBlock(stream: InputStream): ByteArray {
    val dataStream = DataInputStream(stream)
    val length = dataStream.readInt() * 4
    val data = ByteArray(length)
    dataStream.read(data)
    return data
}

private fun writeBlock(stream: OutputStream, data: ByteArray) {
    val dataStream = DataOutputStream(stream)
    dataStream.writeInt(data.size / 4)
    dataStream.write(data)
    dataStream.flush()
}

private fun removeLeadingZeros(value: ByteArray): ByteArray {
    var result = value
    while (result[0] == 0.toByte()) {
        result = Arrays.copyOfRange(result, 1, result.size)
    }
    return result
}

private fun fillLeadingZeros(value: ByteArray): ByteArray {
    val padding = 4 - (value.size % 4)
    if (padding == 4)
        return value

    val stream = ByteArrayOutputStream()
    padding.times { () ->
        stream.write(0)
    }
    stream.write(value)
    return stream.toByteArray()
}

private object base64Coder {

    fun encode(value: ByteArray): String =
            when {
                jdkEncodeMethod != null -> jdkEncodeMethod.invoke(jdkClass, value) as String
                androidEncodeMethod != null -> (androidEncodeMethod.invoke(androidClass, value, 2) as String)
                else -> throw Exception("no base64 coder class found")
            }

    fun decode(encoded: String): ByteArray =
            when {
                jdkDecodeMethod != null -> jdkDecodeMethod.invoke(jdkClass, encoded) as ByteArray
                androidDecodeMethod != null -> androidDecodeMethod.invoke(androidClass, encoded, 2) as ByteArray
                else -> throw Exception("no base64 coder class found")
            }

    private val jdkClass: Class<*>? = { ()->
        try {
            classLoader.loadClass("javax.xml.bind.DatatypeConverter")
        } catch (exception: ClassNotFoundException) {
            null
        }
    }()

    private val jdkEncodeMethod: Method? =
            if (jdkClass == null) null else jdkClass.getMethod("printBase64Binary", javaClass<ByteArray>())

    private val jdkDecodeMethod: Method? =
            if (jdkClass == null) null else jdkClass.getMethod("parseBase64Binary", javaClass<String>())

    private val androidClass: Class<*>? = { ()->
        try {
            classLoader.loadClass("android.util.Base64")
        } catch (exception: ClassNotFoundException) {
            null
        }
    }()

    private val androidEncodeMethod: Method? =
            if (androidClass == null) null else androidClass.getMethod("encodeToString", javaClass<ByteArray>(), javaClass<Int>())

    private val androidDecodeMethod: Method? =
            if (androidClass == null) null else androidClass.getMethod("decode", javaClass<String>(), javaClass<Int>())

}

private object hexCoder {

    fun encode(input: ByteArray): String =
            when {
                jdkEncodeMethod != null -> (jdkEncodeMethod.invoke(jdkClass, input) as String).toLowerCase()
                else -> nativeEncode(input)
            }

    fun decode(input: String): ByteArray =
            when {
                jdkDecodeMethod != null -> jdkDecodeMethod.invoke(jdkClass, input) as ByteArray
                else -> nativeDecode(input)
            }

    private val jdkClass: Class<*>? = { ()->
        try {
            classLoader.loadClass("javax.xml.bind.DatatypeConverter")
        } catch (exception: ClassNotFoundException) {
            null
        }
    }()

    private val jdkEncodeMethod: Method? =
            if (jdkClass == null) null else jdkClass.getMethod("printHexBinary", javaClass<ByteArray>())

    private val jdkDecodeMethod: Method? =
            if (jdkClass == null) null else jdkClass.getMethod("parseHexBinary", javaClass<String>())

    private fun nativeEncode(input: ByteArray): String {
        val HEXES = "0123456789abcdef";

        val output = StringBuilder(2 * input.size)
        for (value in input) {
            output.append(HEXES.charAt((value.toInt() and 0xF0) shr 4)).append(HEXES.charAt((value.toInt() and 0x0F)));
        }
        return output.toString();
    }

    private fun nativeDecode(input: String): ByteArray {
        if (input.size < 2) {
            return ByteArray(0)
        } else {
            val len = input.size / 2;
            val output = ByteArray(len)

            var index = 0
            while (index < len) {
                output[index] = Integer.parseInt(input.substring(index * 2, index * 2 + 2), 16).toByte();
                index++
            }
            return output
        }
    }

}

private val classLoader = ClassLoader.getSystemClassLoader() as ClassLoader

package com.anyaku.test.crypt

import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.crypt.asymmetric.RSASignature
import com.anyaku.crypt.coder.decode
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.symmetric.AESEncryptedData
import com.anyaku.crypt.PasswordSalt
import com.anyaku.crypt.combined.RSAAESEncryptedData
import com.anyaku.crypt.coder.base64Decode
import com.anyaku.crypt.coder.base64Encode
import com.anyaku.crypt.coder.hexDecode
import com.anyaku.crypt.coder.hexEncode
import java.math.BigInteger
import org.junit.Test
import org.junit.Assert.assertEquals

class coderTest() {

    val encodedRSAKey = "AAAAAIJLackVsoZ2a60f96667/TD4knyiT/jRaJ76BKQaPRk94bFfI6ygq/wspvGZQ4vUEr0JMVfbIw+rU2BffHd8zB02dulSWH2qpAxfn8JGM7XWL0tkA866nbE22umv2+1BVJ1pgJuw47oITKk7vpt7r/NP2dmanNi9d3Ll2syIVR4fAAAAAQAAAAM="
    val encodedRSASignature = "CQ8DtNssxF21tLub57yYoMM5vPV5Hd3PIC31Y1py5wJZmdw52klTZ5WEbFo1fvxTsjaUxui99Z1wq45xnzKU6SA=="
    val encodedAESEncryptedData = "EU2FsdGVkX1/R+WNT/LD/Xj2OKVWKm5Q5EK8oM/Fil+kEaSuFL7zFYmZFMvFgqvVlBiEH0+NF4YL01PcR0UKjTuo8cAKh1qLqhv7UaGXWCqCk3gkr2CeVGu/QwhmuUph0"
    val encodedPasswordSalt = "FleGX0ZhZGol5ifCPk6MowA=="
    val encodedRSAAESEncryptedData = "GB6de2dbfd1cc02c18c721b1083addd8b3bc6429602af75c26c36822ebcc23a4656f01157bc1d005f4b046e420aecc205351803f39674cf992b06389e0815a72ad|EU2FsdGVkX1+nvS1dEVh2Xwh9/2yWQ8VP9GZyqfnM9pCEoEM/BYdP+nfGG6bmgv/yNk6+/51x3nm/cj493MsSbFOVMdx8bmwMYudSErcOsg/aPhoeXQ62MpTV+gcNkT2M"

    val decodedRSAKeyModulus = "92da72456ca19d9aeb47fdebaebbfd30f8927ca24ff8d1689efa04a41a3d193de1b15f23aca0abfc2ca6f199438bd412bd093157db230fab53605f7c777ccc1d3676e952587daaa40c5f9fc24633b5d62f4b6403ceba9db136dae9afdbed41549d69809bb0e3ba084ca93bbe9b7baff34fd9d99a9cd8bd7772e5dacc88551e1f"
    val decodedRSAKeyExponent = "3"
    val decodedAESEncryptedData = "U2FsdGVkX1/R+WNT/LD/Xj2OKVWKm5Q5EK8oM/Fil+kEaSuFL7zFYmZFMvFgqvVlBiEH0+NF4YL01PcR0UKjTuo8cAKh1qLqhv7UaGXWCqCk3gkr2CeVGu/QwhmuUph0"
    val decodedPasswordSalt = "leGX0ZhZGol5ifCPk6MowA=="
    val decodedRSAAESEncryptedDataKey = "beLb/RzALBjHIbEIOt3Ys7xkKWAq91wmw2gi68wjpGVvARV7wdAF9LBG5CCuzCBTUYA/OWdM+ZKwY4nggVpyrQ=="
    val decodedRSAAESEncryptedDataData = "U2FsdGVkX1+nvS1dEVh2Xwh9/2yWQ8VP9GZyqfnM9pCEoEM/BYdP+nfGG6bmgv/yNk6+/51x3nm/cj493MsSbFOVMdx8bmwMYudSErcOsg/aPhoeXQ62MpTV+gcNkT2M"

    [ Test ]
    fun testBase64Decoding() {
        val decoded = base64Decode("AQI=")
        assertEquals(2, decoded.size)
    }

    [ Test ]
    fun testBase64Encoding() {
        val encoded = base64Encode(byteArray(0x01.toByte(), 0x02.toByte()))
        assertEquals("AQI=", encoded)
    }

    [ Test ]
    fun testHexDecoding() {
        val decoded = hexDecode("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f")
        assertEquals(32, decoded.size)
    }

    [ Test ]
    fun testHexEncoding() {
        val encoded = hexEncode(byteArray(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf, 0x10, 0x11, 0x1f))
        assertEquals("000102030405060708090a0b0c0d0e0f10111f", encoded)
    }

    [ Test ]
    fun testRSAKeyDecoding() {
        val decoded = decode(encodedRSAKey) as RSAKey
        assertEquals(decodedRSAKeyModulus, decoded.modulus.toString(16))
        assertEquals(decodedRSAKeyExponent, decoded.exponent.toString(16))
    }

    [ Test ]
    fun testRSAKeyEncoding() {
        val modulus = BigInteger(decodedRSAKeyModulus, 16)
        val exponent = BigInteger(decodedRSAKeyExponent, 16)
        val key = RSAKey(modulus, exponent)
        val encoded = encode(key)

        assertEquals(encodedRSAKey, encoded)
    }

    [ Test ]
    fun testRSASignatureDecoding() {
        val decoded = decode(encodedRSASignature) as RSASignature
        assertEquals("RSASignature Q8DtNssxF21tLub57yYoMM5vPV5Hd3PIC31Y1py5wJZmdw52klTZ5WEbFo1fvxTsjaUxui99Z1wq45xnzKU6SA==", decoded.toString())
    }

    [ Test ]
    fun testRSASignatureEncoding() {
        val decoded = decode(encodedRSASignature) as RSASignature
        val encoded = encode(decoded)
        assertEquals(encodedRSASignature, encoded)
    }

    [ Test ]
    fun testAESEncryptedDataDecoding() {
        val decoded = decode(encodedAESEncryptedData) as AESEncryptedData
        assertEquals(decodedAESEncryptedData, base64Encode(decoded.data))
    }

    [ Test ]
    fun testAESEncryptedDataEncoding() {
        val decoded = decode(encodedAESEncryptedData) as AESEncryptedData
        val encoded = encode(decoded)
        assertEquals(encodedAESEncryptedData, encoded)
    }

    [ Test ]
    fun testPasswordSaltDecoding() {
        val decoded = decode(encodedPasswordSalt) as PasswordSalt
        assertEquals(decodedPasswordSalt, base64Encode(decoded.data))
    }

    [ Test ]
    fun testPasswordSaltEncoding() {
        val decoded = decode(encodedPasswordSalt) as PasswordSalt
        val encoded = encode(decoded)
        assertEquals(encodedPasswordSalt, encoded)
    }

    [ Test ]
    fun testRSAAESEncryptedDataDecoding() {
        val decoded = decode(encodedRSAAESEncryptedData) as RSAAESEncryptedData
        assertEquals(decodedRSAAESEncryptedDataKey, base64Encode(decoded.key.data))
        assertEquals(decodedRSAAESEncryptedDataData, base64Encode(decoded.data.data))
    }

    [ Test ]
    fun testRSAAESEncryptedDataEncoding() {
        val decoded = decode(encodedRSAAESEncryptedData) as RSAAESEncryptedData
        val encoded = encode(decoded)
        assertEquals(encodedRSAAESEncryptedData, encoded)
    }

}

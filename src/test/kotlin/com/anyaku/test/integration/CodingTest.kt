package com.anyaku.test.integration

import org.junit.Test as test
import kotlin.test.assertEquals
import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.debug
import com.anyaku.test.asByteArray
import kotlin.test.assertTrue
import java.util.Arrays
import com.anyaku.test.integration.javascript.runInWorker
import com.anyaku.crypt.coder.base64Encode
import com.anyaku.crypt.coder.base64Decode
import com.anyaku.crypt.coder.decode

class CodingTest {

    {
        runInWorker("function _toArray(hexString) { return CryptoJS.enc.Hex.parse(hexString).words; }; function _toHex(array) { return CryptoJS.enc.Hex.stringify(CryptoJS.lib.WordArray.create(array)); };")
    }

    test fun testBase64Coding() {
        val encoded = runInWorker("CryptoJS.enc.Base64.stringify(CryptoJS.lib.WordArray.create([ 0x01020304, 0x05060708 ]));") as String
        assertEquals(base64Encode(asByteArray(1, 2, 3, 4, 5, 6, 7, 8)), encoded)
        assertTrue(Arrays.equals(asByteArray(1, 2, 3, 4, 5, 6, 7, 8), base64Decode(encoded)))
    }

    test fun testSimpleRecoding() {
        val modulus = "abcd1234"
        val exponent = "1234abcd"

        val encodedKey = runInWorker("epdRoot.Crypt.Coder.encode({ type: 'rsaKey', modulus: _toArray('$modulus'), exponent: _toArray('$exponent') });") as String
        val decodedKey = decode(encodedKey) as RSAKey

        assertEquals(modulus, decodedKey.modulus.toString(16), "should keep the key's modulus")
        assertEquals(exponent, decodedKey.exponent.toString(16), "should keep the key's exponent")
    }

    test fun testRecodingOfAKeyPair() {
        runInWorker("var keyPair = epdRoot.Crypt.Asymmetric.generateKeyPair(512);")

        val modulus = runInWorker("_toHex(keyPair.publicKey.modulus);") as String
        val privateExponent = runInWorker("_toHex(keyPair.privateKey.exponent);") as String

        val encodedPublicKey = runInWorker("epdRoot.Crypt.Coder.encode(keyPair.publicKey);") as String
        val encodedPrivateKey = runInWorker("epdRoot.Crypt.Coder.encode(keyPair.privateKey);") as String

        val decodedPublicKey = decode(encodedPublicKey) as RSAKey
        val decodedPrivateKey = decode(encodedPrivateKey) as RSAKey

        assertEquals(modulus, decodedPublicKey.modulus.toString(16), "should keep the public key's modulus")
        assertEquals("3", decodedPublicKey.exponent.toString(16), "should keep the public key's exponent")

        assertEquals(modulus, decodedPrivateKey.modulus.toString(16), "should keep the private key's modulus")
        assertEquals(privateExponent, decodedPrivateKey.exponent.toString(16), "should keep the private key's exponent")
    }

}

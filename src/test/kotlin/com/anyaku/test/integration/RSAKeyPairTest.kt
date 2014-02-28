package com.anyaku.test.integration

import com.anyaku.test.integration.javascript.runInWorker
import java.math.BigInteger
import java.security.KeyFactory
import java.security.spec.RSAPublicKeySpec
import java.security.spec.RSAPrivateKeySpec
import java.security.interfaces.RSAPublicKey
import java.security.interfaces.RSAPrivateKey
import org.junit.Assert.assertEquals
import org.junit.Test

class RSAKeyPairTest() {

    [ Test ]
    fun testKeyPairGeneration() {
        runInWorker("var rsa = new epdRoot.Crypt.Asymmetric.RSA.Key(); rsa.generate(512, '00000003');")

        val modulus = runInWorker("rsa.n.toString(16);") as String
        val publicExponent = "00000003"
        val privateExponent = runInWorker("rsa.d.toString(16);") as String

        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(RSAPublicKeySpec(BigInteger(modulus, 16), BigInteger(publicExponent, 16))) as RSAPublicKey
        val privateKey = keyFactory.generatePrivate(RSAPrivateKeySpec(BigInteger(modulus, 16), BigInteger(privateExponent, 16))) as RSAPrivateKey

        assertEquals(modulus, publicKey.getModulus()!!.toString(16))
        assertEquals("3", publicKey.getPublicExponent()!!.toString(16))

        assertEquals(modulus, privateKey.getModulus()!!.toString(16))
        assertEquals(privateExponent, privateKey.getPrivateExponent()!!.toString(16))
    }

}

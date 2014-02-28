package com.anyaku.crypt.asymmetric

import java.math.BigInteger
import java.security.KeyFactory
import java.security.spec.RSAPrivateKeySpec
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec

class RSAKey(val modulus: BigInteger, val exponent: BigInteger) : Key {

    fun toPrivateKey(): PrivateKey {
        return keyFactory.generatePrivate(RSAPrivateKeySpec(modulus, exponent)) as PrivateKey
    }

    fun toPublicKey(): PublicKey {
        return keyFactory.generatePublic(RSAPublicKeySpec(modulus, exponent)) as PublicKey
    }

    fun equals(other: Any?): Boolean {
        return other is RSAKey && modulus.equals(other.modulus) && exponent.equals(other.exponent)
    }

    class object {

        val keyFactory = KeyFactory.getInstance("RSA")

    }

}

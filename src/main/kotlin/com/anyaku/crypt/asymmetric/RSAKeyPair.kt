package com.anyaku.crypt.asymmetric

import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.interfaces.RSAPrivateKey

class RSAKeyPair(keySize: Int = 2048): KeyPair {

    override val publicKey: RSAKey
        get() {
            val rsaKey = keyPair.getPublic() as RSAPublicKey
            return RSAKey(rsaKey.getModulus() as BigInteger, rsaKey.getPublicExponent() as BigInteger)
        }

    override val privateKey: RSAKey
        get() {
            val rsaKey = keyPair.getPrivate() as RSAPrivateKey
            return RSAKey(rsaKey.getModulus() as BigInteger, rsaKey.getPrivateExponent() as BigInteger)
        }

    private val keyPair: java.security.KeyPair
    {
        keyPairGenerator.initialize(keySize)
        keyPair = keyPairGenerator.generateKeyPair() as java.security.KeyPair
    }

}

private val keyPairGenerator = KeyPairGenerator.getInstance("RSA")

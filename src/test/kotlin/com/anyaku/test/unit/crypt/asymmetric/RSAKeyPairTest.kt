package com.anyaku.test.crypt.asymmetric

import org.junit.Test as test
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
import com.anyaku.crypt.asymmetric.RSAKeyPair

class RSAKeyPairTest() {

    test fun testKeyPairGeneration() {
        val keyPair = RSAKeyPair()
        assertNotNull(keyPair.publicKey, "should have a public key")
        assertNotNull(keyPair.privateKey, "should have a private key")
    }

}

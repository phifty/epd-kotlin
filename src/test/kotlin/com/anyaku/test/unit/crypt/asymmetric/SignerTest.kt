package com.anyaku.test.crypt.asymmetric

import com.anyaku.crypt.asymmetric.Signer

import org.junit.Test as test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.debug
import com.anyaku.crypt.coder.decode

class SignerTest() {

    val signer = Signer("test")
    val privateKey = decode("AAAAAIJLackVsoZ2a60f96667/TD4knyiT/jRaJ76BKQaPRk94bFfI6ygq/wspvGZQ4vUEr0JMVfbIw+rU2BffHd8zB02dulSWH2qpAxfn8JGM7XWL0tkA866nbE22umv2+1BVJ1pgJuw47oITKk7vpt7r/NP2dmanNi9d3Ll2syIVR4fAAAAIGHm9tjzFmkR8i/+nR8n/iCltv3BiqXg8Gn8AxgRfhDT68uUwnMVx/1zGfZmLQfityiwy4/nbLUc4kA/qE+oiBJwyosCct029JzKNX0LexJIP5jqDE2LUyhnFLQY4Es2pgUDIqCYNpNW8tN1+5i//GsHPAW7euZF1R+vjnAH9BNb") as RSAKey
    val publicKey = decode("AAAAAIJLackVsoZ2a60f96667/TD4knyiT/jRaJ76BKQaPRk94bFfI6ygq/wspvGZQ4vUEr0JMVfbIw+rU2BffHd8zB02dulSWH2qpAxfn8JGM7XWL0tkA866nbE22umv2+1BVJ1pgJuw47oITKk7vpt7r/NP2dmanNi9d3Ll2syIVR4fAAAAAQAAAAM=") as RSAKey

    test fun testSignatureCreation() {
        val signature = signer.sign(privateKey)
        assertEquals("RSASignature VlWva0ADULowzjzJyfKCWF5tvmRMOgeTPSL2GbOdiHLkM1g/Vr5p4dGch7zTsBfNQlawJQ0vaB4R3NoIiiLjs28f3qUhf6iTjQyik0KJ4a2YSkLwxB8EF3Xy6GI0+tzbbxzPFHANLqi52Dzlx1nbzxKqCDgRsMe2CR98su8+JuM=", signature.toString())
    }

    test fun testSignatureVerification() {
        val signature = signer.sign(privateKey)
        assertTrue(signer.verify(publicKey, signature))
    }

}

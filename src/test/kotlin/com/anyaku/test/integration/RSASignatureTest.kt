package com.anyaku.test.integration

import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.crypt.asymmetric.RSASignature
import com.anyaku.crypt.asymmetric.Signer
import com.anyaku.crypt.coder.decode
import com.anyaku.test.integration.javascript.runInWorker
import org.junit.Assert.assertTrue
import org.junit.Test

class RSASignatureTest() {

    val encodedPublicKey = "AAAAAIJLackVsoZ2a60f96667/TD4knyiT/jRaJ76BKQaPRk94bFfI6ygq/wspvGZQ4vUEr0JMVfbIw+rU2BffHd8zB02dulSWH2qpAxfn8JGM7XWL0tkA866nbE22umv2+1BVJ1pgJuw47oITKk7vpt7r/NP2dmanNi9d3Ll2syIVR4fAAAAAQAAAAM="
    val encodedPrivateKey = "AAAAAIJLackVsoZ2a60f96667/TD4knyiT/jRaJ76BKQaPRk94bFfI6ygq/wspvGZQ4vUEr0JMVfbIw+rU2BffHd8zB02dulSWH2qpAxfn8JGM7XWL0tkA866nbE22umv2+1BVJ1pgJuw47oITKk7vpt7r/NP2dmanNi9d3Ll2syIVR4fAAAAIGHm9tjzFmkR8i/+nR8n/iCltv3BiqXg8Gn8AxgRfhDT68uUwnMVx/1zGfZmLQfityiwy4/nbLUc4kA/qE+oiBJwyosCct029JzKNX0LexJIP5jqDE2LUyhnFLQY4Es2pgUDIqCYNpNW8tN1+5i//GsHPAW7euZF1R+vjnAH9BNb"
    val publicKey = decode(encodedPublicKey) as RSAKey
    val privateKey = decode(encodedPrivateKey) as RSAKey
    val sampleContent = "test"

    {
        runInWorker("var publicKey = epdRoot.Crypt.Coder.decode('$encodedPublicKey'), privateKey = epdRoot.Crypt.Coder.decode('$encodedPrivateKey');")
    }

    [ Test ]
    fun testSignatureVerification() {
        val encodedSignature = runInWorker("epdRoot.Crypt.Coder.encode(epdRoot.Crypt.Asymmetric.sign('$sampleContent', publicKey, privateKey));") as String
        val signature = decode(encodedSignature) as RSASignature
        val signer = Signer(sampleContent)
        assertTrue(signer.verify(publicKey, signature))
    }

}

package com.anyaku.crypt.asymmetric

import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.crypt.coder

class Signer(val content: String) {

    private val signature = java.security.Signature.getInstance("SHA256withRSA")

    public fun sign(key: RSAKey): RSASignature {
        signature.initSign(key.toPrivateKey())
        signature.update(content.getBytes())
        return RSASignature(signature.sign() as ByteArray)
    }

    public fun verify(key: Key, encodedSignature: Signature): Boolean {
        return if (key is RSAKey && encodedSignature is RSASignature) {
            signature.initVerify(key.toPublicKey())
            signature.update(content.getBytes())
            signature.verify(encodedSignature.data)
        } else false
    }

}

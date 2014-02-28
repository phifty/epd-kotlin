package com.anyaku.crypt.asymmetric

import com.anyaku.crypt.coder
import com.anyaku.crypt.coder.base64Encode
import java.util.Arrays

class RSASignature(data: ByteArray) : Signature {

    override val data = data

    fun toString(): String {
        return "RSASignature " + base64Encode(data)
    }

    fun equals(other: Any): Boolean {
        return other is RSASignature && Arrays.equals(data, other.data)
    }

}

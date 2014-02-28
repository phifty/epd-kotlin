package com.anyaku.crypt.symmetric

import javax.crypto.SecretKey
import java.util.Arrays

/**
 * User: phifty
 */
class AESKey(data: ByteArray) : Key {

    override val data = data
    
    fun equals(other: Any): Boolean =
            other is AESKey && Arrays.equals(data, other.data)

}

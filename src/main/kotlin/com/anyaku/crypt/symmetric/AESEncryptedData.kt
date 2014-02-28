package com.anyaku.crypt.symmetric

import java.util.Arrays

class AESEncryptedData(data: ByteArray) : EncryptedData {

    override val data = data

    var header: Boolean
        get() = Arrays.equals(openSSLHeader, Arrays.copyOfRange(data, 0, 8))
        set(value) {
            if (value)
                System.arraycopy(openSSLHeader, 0, data, 0, openSSLHeader.size)
        }

    var salt: ByteArray
        get() = Arrays.copyOfRange(data, 8, 16)
        set(value) { System.arraycopy(value, 0, data, 8, 8) }

    var payload: ByteArray
        get() = Arrays.copyOfRange(data, 16, data.size)
        set(value) { System.arraycopy(value, 0, data, 16, value.size) }

}

private val openSSLHeader = { (): ByteArray ->
    val values = array(0x53, 0x61, 0x6c, 0x74, 0x65, 0x64, 0x5f, 0x5f)
    val result = ByteArray(values.size)
    for (index in values.indices) result[index] = values[index].toByte()
    result
}()

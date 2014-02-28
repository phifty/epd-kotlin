package com.anyaku.map

import com.anyaku.crypt.asymmetric.Key
import com.anyaku.crypt.asymmetric.RSAKey
import java.util.HashMap
import java.math.BigInteger

fun fromKey(key: Key): Map<String, ByteArray> {
    val result = HashMap<String, ByteArray>()
    if (key is RSAKey) {
        result.put("modulus", key.modulus.toByteArray())
        result.put("exponent", key.exponent.toByteArray())
    }
    return result
}

fun toKey(map: Map<String, ByteArray>): Key {
    val modulus = map.get("modulus") as ByteArray
    val exponent = map.get("exponent") as ByteArray
    return RSAKey(BigInteger(modulus), BigInteger(exponent))
}

fun stringify(value: Any): String =
        when (value) {
            is Number -> value.toString()
            is String -> value
            is Map<*, *> -> {
                val result = StringBuilder()
                val keys = value.keySet().copyToArray()
                java.util.Arrays.sort(keys)
                for (key in keys) {
                    result.append(key.toString())
                    val nestedValue = value.get(key)
                    if (nestedValue != null) {
                        result.append(stringify(nestedValue))
                    }
                }
                result.toString()
            }
            is List<*> -> {
                val result = StringBuilder()
                for (index in 0..value.size - 1) {
                    result.append(index.toString())
                    val item = value[index]
                    if (item != null) {
                        result.append(stringify(item))
                    }
                }
                result.toString()
            }
            else -> ""
        }

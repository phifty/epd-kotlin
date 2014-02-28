package com.anyaku.test

import java.util.HashMap
import java.util.ArrayList

[ suppress("UNCHECKED_CAST") ]
fun asMap<K, V>(vararg arguments: Any?): MutableMap<K, V> {
    val result = HashMap<K, V>()
    val iterator = arguments.iterator()
    while (iterator.hasNext()) {
        val key = iterator.next() as K
        val value = iterator.next() as V
        result.put(key, value)
    }
    return result
}

fun asList<T>(vararg arguments: T): MutableList<T> {
    val result = ArrayList<T>()
    for (item in arguments) {
        result.add(item)
    }
    return result
}

fun asByteArray(vararg arguments: Byte): ByteArray {
    val result = ByteArray(arguments.size)
    for (index in 0..arguments.size - 1) {
        result.set(index, arguments[index])
    }
    return result
}

package com.anyaku.epd.structure.base

import java.util.HashMap

open class StrictMutableHashMap<K, V> : StrictMutableMap<K, V> {

    override val map: MutableMap<K, V> = HashMap<K, V>()

    override fun size(): Int =
            map.size

    override fun isEmpty(): Boolean =
            map.isEmpty()

    override fun containsKey(key: K): Boolean =
            map.containsKey(key)

    override fun containsValue(value: V): Boolean =
            map.containsValue(value)

    override fun get(key: K): V {
        val value = map.get(key)
        if (value == null) throw Exception("no item found at key $key")
        return value
    }

    override fun put(key: K, value: V): V =
            map.put(key, value) as V

    override fun set(key: K, value: V): V =
            put(key, value)

    override fun remove(key: K): V =
            map.remove(key) as V

    override fun putAll(other: StrictMap<K, V>) {
        for (entry in other.entrySet()) {
            map[entry.key] = entry.value
        }
    }

    override fun setAll(other: StrictMap<K, V>) {
        map.clear()
        putAll(other)
    }

    override fun clear() =
            map.clear()

    override fun keySet(): MutableSet<K> =
            map.keySet()

    override fun values(): MutableCollection<V> =
            map.values()

    override fun entrySet(): MutableSet<MutableMap.MutableEntry<K, V>> =
            map.entrySet()

    override fun equals(other: Any): Boolean =
            other is StrictMap<*, *> && map.equals(other.map)

}

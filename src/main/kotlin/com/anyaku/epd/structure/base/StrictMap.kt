package com.anyaku.epd.structure.base

trait StrictMap<K, out V> {

    val map: Map<K, V>

    // Query Operations
    fun size(): Int

    fun isEmpty(): Boolean

    fun containsKey(key: K): Boolean

    fun containsValue(value: V): Boolean

    fun get(key: K): V

    fun equals(other: Any): Boolean

    // Views
    fun keySet(): Set<K>

    fun values(): Collection<V>

    fun entrySet(): Set<Map.Entry<K, V>>

    trait Entry<out K, out V>: Hashable {
        fun getKey(): K
        fun getValue(): V
    }

}

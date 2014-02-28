package com.anyaku.epd.structure.base

trait StrictMutableMap<K, V> : StrictMap<K, V> {

    override val map: MutableMap<K, V>

    // Modification Operations
    fun put(key: K, value: V): V

    fun set(key: K, value: V): V

    fun remove(key: K) : V

    // Bulk Modification Operations
    fun putAll(other: StrictMap<K, V>)

    fun setAll(other: StrictMap<K, V>)

    fun clear()

    // Views
    override fun keySet(): MutableSet<K>

    override fun values(): MutableCollection<V>

    override fun entrySet(): MutableSet<MutableMap.MutableEntry<K, V>>

    trait MutableEntry<K, V> : Map.Entry<K, V>, Hashable {

        fun setValue(value: V): V

    }

}

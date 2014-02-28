package com.anyaku.epd.structure

import java.util.HashSet

class UnlockedContact(factory: Factory) {

    val keys = UnlockedKeysMap(factory)

    var sections: MutableCollection<String> = HashSet<String>()

    fun equals(other: Any?): Boolean =
            other is UnlockedContact && keys == other.keys && sections == other.sections

}

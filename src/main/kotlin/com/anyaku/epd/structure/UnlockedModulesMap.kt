package com.anyaku.epd.structure

import com.anyaku.epd.structure.base.StrictMutableMap
import com.anyaku.epd.structure.base.StrictMutableHashMap

class UnlockedModulesMap(
        private val factory: Factory,
        map: StrictMutableMap<String, Module> = StrictMutableHashMap()
) : StrictMutableMap<String, Module> by map {

    fun add(id: String): Module {
        val module = factory.buildModule(id)
        set(id, module)
        return module
    }

}

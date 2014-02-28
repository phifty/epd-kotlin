package com.anyaku.epd.structure

import java.util.HashMap
import com.anyaku.epd.structure.modules.Unknown
import com.anyaku.map.Mapable

trait Module : Mapable {

    class object {

        fun register(id: String, moduleClass: Class<out Module>) {
            moduleClasses[id] = moduleClass
        }

        fun get(id: String): Class<out Module> =
                if (moduleClasses.containsKey(id)) moduleClasses[id]!! else javaClass<Unknown>()

        private val moduleClasses = HashMap<String, Class<out Module>>()

    }

}

package com.anyaku.epd.structure.modules

import com.anyaku.epd.structure.Module
import java.util.HashMap

class Unknown(val id: String) : Module {

    override fun toMap(): Map<String, Any?> =
            map

    override fun fromMap(map: Map<String, Any?>) {
        this.map = map
    }

    private var map: Map<String, Any?> = HashMap()

}

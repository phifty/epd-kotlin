package com.anyaku.epd.structure.v1

import com.anyaku.crypt.coder.decode
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.symmetric.Key
import com.anyaku.epd.structure.Factory as FactoryBase
import com.anyaku.epd.structure.UnlockedKeysMap
import com.anyaku.epd.structure.UnlockedSection as UnlockedSectionBase
import com.anyaku.epd.structure.UnlockedModulesMap
import com.anyaku.epd.structure.UnlockedContactsMap
import com.anyaku.epd.structure.UnlockedMapper as UnlockedMapperTrait
import com.anyaku.epd.structure.modules.Basic
import java.util.HashMap

class UnlockedMapper(private val factory: FactoryBase) : UnlockedMapperTrait {

    override fun keysToMap(keys: UnlockedKeysMap): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        for (entry in keys.entrySet())
            result[ entry.key ] = encode(entry.value)

        return result
    }

    override fun keysFromMap(map: Map<String, Any?>): UnlockedKeysMap {
        val result = UnlockedKeysMap(factory)

        for (entry in map.entrySet())
            result[entry.key] = decode(entry.value as String) as Key

        return result
    }

    override fun sectionToMap(unlockedSection: UnlockedSectionBase): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        if (unlockedSection.title != null)
            result["title"] = unlockedSection.title

        result["modules"] = modulesToMap(unlockedSection.modules)

        return result
    }

    [ suppress("UNCHECKED_CAST") ]
    override fun sectionFromMap(
            id: String, map: Map<String, Any?>,
            contacts: UnlockedContactsMap?
    ): UnlockedSectionBase {
        val result = UnlockedSectionBase(id, map["title"] as String?, contacts, factory)

        result.modules.setAll(modulesFromMap(map["modules"] as Map<String, Any?>))

        return result
    }

    [ suppress("UNCHECKED_CAST") ]
    fun modulesFromMap(map: Map<String, Any?>): UnlockedModulesMap {
        val result = UnlockedModulesMap(factory)

        for (entry in map.entrySet()) {
            val moduleMap = entry.value as Map<String, Any?>
            val moduleContentMap = moduleMap["content"] as Map<String, Any?>
            result[ entry.key ] = factory.buildModule(entry.key, moduleContentMap)
        }

        return result
    }

    fun modulesToMap(unlockedModulesMap: UnlockedModulesMap): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        for (entry in unlockedModulesMap.entrySet()) {
            val moduleMap = HashMap<String, Any?>()
            moduleMap["content"] = entry.value.toMap()
            result[ entry.key ] = moduleMap
        }

        return result
    }

}

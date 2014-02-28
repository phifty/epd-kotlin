package com.anyaku.epd.structure

trait UnlockedMapper {

    fun keysToMap(keys: UnlockedKeysMap): Map<String, Any?>

    fun keysFromMap(map: Map<String, Any?>): UnlockedKeysMap

    fun sectionToMap(unlockedSection: UnlockedSection): Map<String, Any?>

    fun sectionFromMap(id: String, map: Map<String, Any?>, contacts: UnlockedContactsMap?): UnlockedSection

}

package com.anyaku.map

trait Mapable {

    fun toMap(): Map<String, Any?>

    fun fromMap(map: Map<String, Any?>)

}

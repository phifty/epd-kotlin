package com.anyaku.epd.structure.modules

import com.anyaku.crypt.coder.hexEncode
import com.anyaku.epd.structure.Factory
import com.anyaku.epd.structure.Module
import java.security.MessageDigest
import java.util.HashMap

class Basic : Module {

    var name: String? = null

    var country: String? = null

    var gravatar: String? = null
        set(value) {
            $gravatar = if (value == null) null else
                hexEncode(messageDigest.digest(value.trim().toLowerCase().getBytes("UTF-8"))!!)
        }
        get() = $gravatar

    override fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        if (name != null) result["name"] = name
        if (country != null) result["country"] = country
        if (gravatar != null) result["gravatar"] = gravatar

        return result
    }

    override fun fromMap(map: Map<String, Any?>) {
        name = map["name"] as String?
        country = map["country"] as String?
        gravatar = map["gravatar"] as String?
    }

    fun gravatarUrl(size: Int = 100): String? =
        if (gravatar == null) null else
            "http://www.gravatar.com/avatar/$gravatar?size=$size&default=retro"

    fun equals(other: Any): Boolean =
            other is Basic && name == other.name && country == other.country && gravatar == other.gravatar

    class object {

        val id = "build_in:com.anyaku.Basic"

        {
            Module.register(id, javaClass<Basic>())
        }

    }

}

private val messageDigest = MessageDigest.getInstance("MD5")

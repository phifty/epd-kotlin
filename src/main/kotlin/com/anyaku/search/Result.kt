package com.anyaku.search

import com.anyaku.crypt.asymmetric.Key
import com.anyaku.crypt.coder.decode
import com.anyaku.crypt.coder.encode
import com.anyaku.epd.structure.Contactable
import com.anyaku.epd.structure.SignedLockedDocument
import com.anyaku.epd.structure.modules.Basic
import com.anyaku.map.Mapable
import java.util.HashMap

class Result(
        override var id: String,
        override var publicKey: Key,
        var name: String? = null,
        var country: String? = null,
        var gravatar: String? = null
) : Contactable, Mapable {

    override fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        result["id"] = id
        result["publicKey"] = encode(publicKey)
        if (name != null) result["name"] = name
        if (country != null) result["country"] = country
        if (gravatar != null) result["gravatar"] = gravatar

        return result
    }

    override fun fromMap(map: Map<String, Any?>) {
        id = map["id"] as String
        publicKey = decode(map["publicKey"] as String) as Key
        name = map["name"] as? String
        country = map["country"] as? String
        gravatar = map["gravatar"] as? String
    }

    fun equals(other: Any): Boolean =
            other is Result &&
            id == other.id &&
            publicKey == other.publicKey &&
            name == other.name &&
            country == other.country &&
            gravatar == other.gravatar

    class object {

        fun from(document: SignedLockedDocument): Result {
            val basicModule = document.sections.publicSection?.modules?.get(Basic.id) as? Basic
            return Result(document.id,
                          document.publicKey,
                          basicModule?.name,
                          basicModule?.country,
                          basicModule?.gravatar)
        }

    }

}

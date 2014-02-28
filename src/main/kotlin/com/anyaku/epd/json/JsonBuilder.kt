package com.anyaku.epd.json

import com.anyaku.epd.Builder
import com.anyaku.epd.structure.Factory
import com.anyaku.epd.structure.SignedLockedDocument
import com.anyaku.json.parseJson
import com.anyaku.json.writeJson

class JsonBuilder: Builder {

    [ suppress("UNCHECKED_CAST") ]
    override fun buildLockedDocument(input: String): SignedLockedDocument =
            buildLockedDocument(parseJson(input) as Map<String, Any?>)

    override fun buildLockedDocument(map: Map<String, Any?>): SignedLockedDocument {
        val version = map.get("version").toString().toInt()
        val factory = when (version) {
            1 -> com.anyaku.epd.structure.v1.Factory()
            else -> throw Exception("unknown epd version $version")
        }
        val mapper = factory.buildLockedMapper()
        return mapper.signedDocumentFromMap(map)
    }

    override fun buildOutput(input: SignedLockedDocument): String {
        val factory = Factory.latest
        val mapper = factory.buildLockedMapper()
        return writeJson(mapper.signedDocumentToMap(input))
    }

}

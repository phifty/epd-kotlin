package com.anyaku.stream

import com.anyaku.epd.structure.SignedLockedDocument
import com.anyaku.epd.json.JsonBuilder

class JsonDocumentStream {

    fun toStream(documents: Collection<SignedLockedDocument>): String =
            jsonStream.toStream(documents.map { document -> jsonBuilder.buildOutput(document) })

    fun fromStream(stream: String): List<SignedLockedDocument> =
            jsonStream.fromStream(stream).map { chunk -> jsonBuilder.buildLockedDocument(chunk) }

    private val jsonStream = JsonStream()

    private val jsonBuilder = JsonBuilder()

}

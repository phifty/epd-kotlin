package com.anyaku.test.unit.stream

import com.anyaku.stream.JsonDocumentStream
import com.anyaku.test.fixtures.Documents
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.properties.Delegates
import com.anyaku.epd.json.JsonBuilder

class JsonDocumentStreamTest {

    val jsonDocumentStream = JsonDocumentStream()

    val jsonBuilder = JsonBuilder()

    val documentJennyJson by Delegates.lazy { jsonBuilder.buildOutput(Documents.locked["jenny"]) }

    val documentMatJson by Delegates.lazy { jsonBuilder.buildOutput(Documents.locked["mat"]) }

    [ Test ]
    fun testJsonStreamFromAListOfResults() {
        val result = jsonDocumentStream.toStream(array(Documents.locked["jenny"], Documents.locked["mat"]).toList())
        assertEquals("[\n$documentJennyJson,\n$documentMatJson\n]", result)
    }

    [ Test ]
    fun testJsonStreamToAListOfResults() {
        val results = jsonDocumentStream.fromStream("[\n$documentJennyJson,\n$documentMatJson\n]")
        assertEquals(2, results.size)
        assertEquals(Documents.ids["jenny"], results[0].id)
        assertEquals(Documents.ids["mat"], results[1].id)
    }

}

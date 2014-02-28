package com.anyaku.test.unit.stream

import com.anyaku.stream.JsonStream
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonStreamTest {

    val jsonStream = JsonStream()

    [ Test ]
    fun testJsonStreamFromAnEmptyList() {
        val result = jsonStream.toStream(array<String>().toList())
        assertEquals("[]", result)
    }

    [ Test ]
    fun testJsonStreamFromAnOneItemListOfStrings() {
        val result = jsonStream.toStream(array("\"one\"").toList())
        assertEquals("[\n\"one\"\n]", result)
    }

    [ Test ]
    fun testJsonStreamFromAListOfStrings() {
        val result = jsonStream.toStream(array("\"one\"", "\"two\"").toList())
        assertEquals("[\n\"one\",\n\"two\"\n]", result)
    }

    [ Test ]
    fun testJsonStreamToAnEmptyList() {
        val result = jsonStream.fromStream("[]")
        assertEquals(0, result.size)
    }

    [ Test ]
    fun testJsonStreamToAnOneItemListOfStrings() {
        val result = jsonStream.fromStream("[\n\"one\"\n]")
        assertEquals(1, result.size)
        assertEquals("\"one\"", result[0])
    }

    [ Test ]
    fun testJsonStreamToAListOfStrings() {
        val result = jsonStream.fromStream("[\n\"one\",\n\"two\"\n]")
        assertEquals(2, result.size)
        assertEquals("\"one\"", result[0])
        assertEquals("\"two\"", result[1])
    }

}

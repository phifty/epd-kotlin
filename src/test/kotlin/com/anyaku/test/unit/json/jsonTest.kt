package com.anyaku.test.json

import org.junit.Test as test
import kotlin.test.assertEquals
import com.anyaku.json.parseJson

class jsonTest() {

    [ suppress("UNCHECKED_CAST") ]
    test fun testJsonParsing() {
        val result = parseJson("{ \"test\": -12.3456789 }") as MutableMap<String, Any?>
        assertEquals(-12.3456789, result.get("test"))
    }

}

package com.anyaku.test.map

import org.junit.Test as test
import com.anyaku.test.asMap
import com.anyaku.test.asList
import kotlin.test.assertEquals
import com.anyaku.map.stringify

class mapTest() {

    val sampleMap = asMap<String, Any?>("test", "value", "nested", asList(1, 2, 3))

    test fun testStringification() {
        assertEquals("nested011223testvalue", stringify(sampleMap))
    }

    test fun testFloatStringification() {
        assertEquals("latitude52.485271999999995longitude13.4372103",
            stringify(asMap<String, Any?>("latitude", 52.485271999999995, "longitude", 13.4372103)))

        assertEquals("latitude-52.485271999999995longitude-13.4372103",
            stringify(asMap<String, Any?>("latitude", -52.485271999999995, "longitude", -13.4372103)))
    }

}

package com.anyaku.test.util

import com.anyaku.util.generateId
import org.junit.Assert.assertEquals
import org.junit.Test

class utilTest {

    [ Test ]
    fun testGenerateId() {
        val id = generateId()
        assertEquals(32, id.length())
    }

    [ Test ]
    fun testGenerateSmallId() {
        val id = generateId(8)
        assertEquals(8, id.length())
    }

}

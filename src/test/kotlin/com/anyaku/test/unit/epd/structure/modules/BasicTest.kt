package com.anyaku.test.unit.epd.structure.modules

import com.anyaku.epd.structure.modules.Basic
import java.util.HashMap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BasicTest {

    val basicModule = Basic()

    [ Test ]
    fun testToMap() {
        basicModule.name = "Test"
        val map = basicModule.toMap()
        assertEquals("Test", map.get("name"))
    }

    [ Test ]
    fun testFromMap() {
        val map = HashMap<String, Any?>()
        map.put("name", "Another Test")
        basicModule.fromMap(map)

        assertEquals("Another Test", basicModule.name)
    }

    [ Test ]
    fun testIfGravatarHashIsCalculated() {
        basicModule.gravatar = "b.phifty@gmail.com"
        assertEquals("0d0f57a6c9c5f2059045da55cb33f982", basicModule.gravatar)

        basicModule.gravatar = " b.phifty@gmail.com "
        assertEquals("0d0f57a6c9c5f2059045da55cb33f982", basicModule.gravatar)

        basicModule.gravatar = "b.Phifty@gmail.com"
        assertEquals("0d0f57a6c9c5f2059045da55cb33f982", basicModule.gravatar)

        basicModule.gravatar = null
        assertNull(basicModule.gravatar)
    }

    [ Test ]
    fun testIfGravatarUrlIsCalculated() {
        basicModule.gravatar = "b.phifty@gmail.com"
        assertEquals("http://www.gravatar.com/avatar/0d0f57a6c9c5f2059045da55cb33f982?size=100&default=retro",
                     basicModule.gravatarUrl())
    }

}

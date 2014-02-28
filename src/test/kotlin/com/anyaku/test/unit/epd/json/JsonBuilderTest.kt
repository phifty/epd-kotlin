package com.anyaku.test.unit.epd.json

import com.anyaku.epd.json.JsonBuilder
import com.anyaku.epd.structure.UnlockedSection
import com.anyaku.epd.structure.modules.Basic
import com.anyaku.test.fixtures.Documents
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JsonBuilderTest {

    val testProfile = Documents.locked["helen"]

    val builder = JsonBuilder()
    val buildDocument = builder.buildLockedDocument(builder.buildOutput(testProfile))

    [ Test ]
    fun testBuildDocumentFromJson() {
        assertEquals(Documents.ids["helen"], buildDocument.id)
    }

    [ Test ]
    fun testIfBuildDocumentHasBasicModuleInPublicSection() {
        assertTrue((buildDocument.sections.publicSection as UnlockedSection).modules.keySet().contains(Basic.id))
    }

    [ Test ]
    fun testBuildJsonFromDocument() {
        val json = builder.buildOutput(testProfile)
        assertTrue(json.matches("^.*\"publicKey\".*$"))
    }

}

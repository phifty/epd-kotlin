package com.anyaku.test.integration

import com.anyaku.epd.structure.Factory
import com.anyaku.map.stringify
import com.anyaku.test.fixtures.Documents
import com.anyaku.test.integration.javascript.runInWorker
import org.junit.Assert.assertEquals
import org.junit.Test

class MapStringificationTest() {

    val sampleProfile = Documents.locked["helen"]
    val sampleProfileJson = Documents.json["helen"]

    val mapper = Factory.latest.buildLockedMapper()

    [ Test ]
    fun testMapStringification() {
        val result = runInWorker("var profile = $sampleProfileJson; epdRoot.Object.stringify(profile);") as String
        assertEquals(result, stringify(mapper.signedDocumentToMap(sampleProfile)))
    }

}

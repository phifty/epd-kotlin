package com.anyaku.test.unit.search

import com.anyaku.epd.Generator
import com.anyaku.epd.Locker
import com.anyaku.epd.structure.modules.Basic
import com.anyaku.search.Result
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.properties.Delegates
import com.anyaku.epd.PublicKeyResolver
import com.anyaku.crypt.asymmetric.Key

class ResultTest {

    val generator = Generator()
    val locker = Locker(object : PublicKeyResolver {
        override fun resolve(documentId: String): Key? {
            throw UnsupportedOperationException()
        }
    })

    val document by Delegates.lazy {
        val document = generator.document()
        val basicModule = document.sections.publicSection.modules[Basic.id] as Basic
        basicModule.name = "Test name"
        basicModule.country = "DEU"
        basicModule.gravatar = "Test gravatar"
        locker.lock(document)
    }

    [ Test ]
    fun testResultFromDocumentCreation() {
        val result = Result.from(document)
        assertEquals(document.id, result.id)
        assertEquals("Test name", result.name)
        assertEquals("DEU", result.country)
        assertEquals("d3c459baafe80729e0a51addd496cfcc", result.gravatar)
    }

}

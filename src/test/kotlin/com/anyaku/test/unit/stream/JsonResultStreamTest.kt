package com.anyaku.test.unit.stream

import com.anyaku.search.Result
import com.anyaku.stream.JsonResultStream
import com.anyaku.test.fixtures.Documents
import kotlin.properties.Delegates
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonResultStreamTest {

    val jsonResultStream = JsonResultStream()

    val resultOne by Delegates.lazy {
        Result("one", dummyPublicKey)
    }

    val resultTwo by Delegates.lazy {
        Result("two", dummyPublicKey, "test name", "DEU", "test gravatar")
    }

    [ Test ]
    fun testJsonStreamFromAListOfResults() {
        val result = jsonResultStream.toStream(array(resultOne, resultTwo).toList())
        assertEquals(expectedJsonResultStream, result)
    }

    [ Test ]
    fun testJsonStreamToAListOfResults() {
        val results = jsonResultStream.fromStream(expectedJsonResultStream)
        assertEquals(2, results.size)
        assertEquals(resultOne, results[0])
        assertEquals(resultTwo, results[1])
    }

    private val dummyPublicKey = Documents.locked["helen"].publicKey

    private val expectedJsonResultStream =
            "[\n" +
            "{\"publicKey\":\"AAAAAEJLpt9vdBR2corK+9Ee49O9MTnBDzOygVDSsHhFOuRu8sbwCRC4WKXW37c2XVnaXjpbOGBqQnYKdCUUWnDLzouUAAAABAAAAAw==\",\"id\":\"one\"},\n" +
            "{\"publicKey\":\"AAAAAEJLpt9vdBR2corK+9Ee49O9MTnBDzOygVDSsHhFOuRu8sbwCRC4WKXW37c2XVnaXjpbOGBqQnYKdCUUWnDLzouUAAAABAAAAAw==\",\"id\":\"two\",\"name\":\"test name\",\"gravatar\":\"test gravatar\",\"country\":\"DEU\"}" +
            "\n]"

}

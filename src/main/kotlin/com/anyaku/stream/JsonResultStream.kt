package com.anyaku.stream

import com.anyaku.search.Result
import com.anyaku.json.writeJson
import com.anyaku.json.parseJson
import com.anyaku.crypt.asymmetric.Key

class JsonResultStream {

    fun toStream(results: Collection<Result>): String =
            jsonStream.toStream(results.map { result -> writeJson(result.toMap()) })

    [ suppress("UNCHECKED_CAST") ]
    fun fromStream(stream: String): List<Result> =
            jsonStream.fromStream(stream).map { chunk ->
                val result = Result(dummyId, dummyPublicKey)
                result.fromMap(parseJson(chunk) as Map<String, Any?>)
                result
            }

    private val dummyId = ""
    private val dummyPublicKey = object : Key { }

    private val jsonStream = JsonStream()

}

package com.anyaku.json

import org.json.simple.JSONValue
import java.io.InputStream
import java.io.InputStreamReader

fun parseJson(content: String): Any? =
        JSONValue.parse(content)

fun parseJson(inputStream: InputStream): Any? =
        JSONValue.parse(InputStreamReader(inputStream))

fun writeJson(value: Any?): String =
        JSONValue.toJSONString(value) as? String ?: ""

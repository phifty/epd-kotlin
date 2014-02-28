package com.anyaku.util

import java.io.InputStream
import java.util.Scanner
import java.util.UUID

fun generateId(length: Int = 32): String =
        UUID.randomUUID().toString().replaceAll("-", "").substring(0, length)

fun readStringFromInputStream(inputStream: InputStream): String {
    val scanner = Scanner(inputStream).useDelimiter("\\A")
    return if (scanner.hasNext()) scanner.next() else ""
}

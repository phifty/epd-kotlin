package com.anyaku

import java.io.PrintStream
import java.io.FileOutputStream
import java.io.FileDescriptor

fun debug(message: String) {
    var output = PrintStream(FileOutputStream(FileDescriptor.out))
    output.println(message)
}

fun debug(values: ByteArray) {
    val message = StringBuilder()
    for (value in values) {
        message.append(value.toString())
        message.append(", ")
    }
    debug(message.toString())
}

/**
 * Debug method to print out a table of time measurements.
 */
fun debug(values: Map<Int, Long>) {
    val message = StringBuilder()
    for (value in values) {
        message.append(value.key)
        message.append(" iterations for ")
        message.append(value.value)
        message.append(" ms\n")
    }
    debug(message.toString())
}

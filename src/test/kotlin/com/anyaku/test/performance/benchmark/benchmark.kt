package com.anyaku.test.performance.benchmark

import java.util.ArrayList
import kotlin.util.measureTimeMillis
import java.util.HashMap

/**
 * User: phifty
 */

fun measure(block: () -> Unit): Long {
    return measureTimeMillis(block)
}

fun measure(values: IntArray, block: (value: Int) -> Unit): Map<Int, Long> {
    val durations = HashMap<Int, Long>()
    for (value in values) {

        durations.put(value, measureTimeMillis {
            block(value)
        })
    }
    return durations
}

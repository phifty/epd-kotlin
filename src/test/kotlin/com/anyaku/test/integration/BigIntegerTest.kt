package com.anyaku.test.integration

import org.junit.Test as test
import kotlin.test.assertEquals
import java.math.BigInteger
import kotlin.dom.NodeListAsList
import com.anyaku.test.asList
import com.anyaku.test.integration.javascript.runInWorker

class BigIntegerTest() {

    test fun testBigIntegerHexRepresentation() {
        assertEquals(BigInteger("5db7b170c06ca69d75ecbb881a937087", 16).toString(16),
            runInWorker("(new BigInteger('5db7b170c06ca69d75ecbb881a937087', 16)).toString(16);"))
    }

    test fun testBigIntegerBitLength() {
        assertEquals(BigInteger("5db7b170c06ca69d75ecbb881a937087", 16).bitLength(),
            runInWorker("(new BigInteger('5db7b170c06ca69d75ecbb881a937087', 16)).bitLength();"))
    }

    test fun testBigIntegerConvertToInteger() {
        for (value in asList("1", "2", "abc", "5db7b170c06ca69d75ecbb881a937087")) {
            assertEquals(BigInteger(value, 16).intValue(),
                runInWorker("(new BigInteger('$value', 16)).intValue();"))
        }
    }

}

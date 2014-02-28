package com.anyaku.test.unit.crypt

import com.anyaku.crypt.HashParameters
import org.junit.Assert.assertTrue
import org.junit.Test

class HashParametersTest {

    [ Test ]
    fun testBenchmark() {
        val hashParameters = HashParameters.forSecurityLevel(1)
        val duration = hashParameters.benchmark()
        assertTrue(duration < 100)
    }

    [ Test ]
    fun testDetectionOfMaximalSecurityLevel() {
        val maximalSecurityLevel = HashParameters.maximalSecurityLevel
        assertTrue(maximalSecurityLevel > 1)
    }

    [ Test ]
    fun testIfDetectedMaximalSecurityLevelHashesFasterThanTheMaximalHashDuration() {
        val hashParameters = HashParameters.forSecurityLevel(HashParameters.maximalSecurityLevel)
        val duration = hashParameters.benchmark()
        assertTrue(duration < HashParameters.maximalHashDuration)
    }

}

package com.anyaku.crypt

import com.anyaku.epd.Generator
import kotlin.properties.Delegates

/**
 * Contains all parameters that are needed to create a hashed version of a password.
 */
public class HashParameters(val salt: ByteArray, val iterations: Int, val keySize: Int) {

    /**
     * Runs a benchmark and returns how many milliseconds are needed to hash a sample password with this hash
     * parameters.
     */
    fun benchmark(): Long {
        val startAt = System.currentTimeMillis()
        generator.password(passwordSample, this)
        return System.currentTimeMillis() - startAt
    }

    class object {

        val maximalHashDuration = 500

        val minimalIterations = 1000

        val default: HashParameters
            get() = forSecurityLevel(1)

        val maximalSecurityLevel by Delegates.lazy {
            forSecurityLevel(1).benchmark() // warm up

            var result = 2
            while (forSecurityLevel(result).benchmark() < maximalHashDuration) result += 5
            result--
            while (forSecurityLevel(result).benchmark() > maximalHashDuration) result--
            result
        }

        fun forSecurityLevel(level: Int) =
                HashParameters(randomBytes(16),
                               Math.max(minimalIterations, minimalIterations + ((level - 1) * 14000)),
                               256)

    }
}

private val passwordSample = "Password123"

private val generator by Delegates.lazy { Generator() }

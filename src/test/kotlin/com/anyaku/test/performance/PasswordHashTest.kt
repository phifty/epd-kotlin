package com.anyaku.test.performance

import com.anyaku.epd.Generator
import com.anyaku.debug
import com.anyaku.test.performance.benchmark.measure
import org.junit.Test
import com.anyaku.crypt.HashParameters

/**
 * User: phifty
 */
class PasswordHashTest() {

    val generator = Generator()
    val password = "test"
    val passwordSalt = "salt"
    val keySize = 640

    [ Test ]
    fun testPasswordHashingSpeed() {
        val durations = measure(intArray(1, 10, 100, 1000, 10000)) { (iterations: Int) ->
            generator.password(password,
                               HashParameters(
                                       passwordSalt.getBytes(),
                                       iterations,
                                       keySize))
        }
        debug(durations)
    }

}

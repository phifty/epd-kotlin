package com.anyaku.test.integration.javascript

import java.io.InputStreamReader
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import java.io.InputStream
import com.anyaku.debug
import javax.script.ScriptException

fun runInWorker(code: String): Any? {
    try {
        val result = scriptEngine.eval(code)

        return if (result is Double) {
            result.toInt()
        } else {
            result
        }
    } catch (exception: ScriptException) {
        debug(exception.toString())
        throw exception
    }
}

private val scriptEngine = {(): ScriptEngine ->
    val scriptEngineManager = ScriptEngineManager()
    val scriptEngine = scriptEngineManager.getEngineByName("JavaScript") as ScriptEngine
    try {
        scriptEngine.eval("var navigator = { }, profile;")
        scriptEngine.eval(InputStreamReader(ClassLoader.getSystemResourceAsStream("epd.js") as InputStream))
        scriptEngine.eval("var password = epdRoot.Crypt.Password.hash('test');")
    } catch (exception: Exception) {
        debug(exception.toString())
    }
    scriptEngine
}()

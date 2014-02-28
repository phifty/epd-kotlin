package com.anyaku.stream

import java.util.ArrayList

class JsonStream {

    fun toStream(chunks: Collection<String>): String {
        val result = StringBuffer("[")

        var first = true
        for (chunk in chunks) {
            if (first) first = false else result.append(',')
            result.append('\n')
            result.append(chunk)
        }
        if (!first) result.append('\n')

        result.append(']')
        return result.toString()
    }

    fun fromStream(stream: String): List<String> {
        val result = ArrayList<String>()

        val lines = stream.split('\n')
        for (line in lines)
            if (!"[".equals(line) && !"]".equals(line) && !"[]".equals(line)) {
                val choppedLine = if (line.endsWith(',')) line.substring(0, line.length - 1) else line
                result.add(choppedLine)
            }

        return result
    }

}

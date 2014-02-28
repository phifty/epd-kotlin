package com.anyaku.epd

import com.anyaku.epd.structure.SignedLockedDocument

trait Builder {

    fun buildLockedDocument(input: String): SignedLockedDocument

    fun buildLockedDocument(map: Map<String, Any?>): SignedLockedDocument

    fun buildOutput(input: SignedLockedDocument): String

}

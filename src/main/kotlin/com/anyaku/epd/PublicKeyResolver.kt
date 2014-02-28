package com.anyaku.epd

import com.anyaku.crypt.asymmetric.Key

trait PublicKeyResolver {

    fun resolve(documentId: String): Key?

}

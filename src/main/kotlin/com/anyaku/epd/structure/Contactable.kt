package com.anyaku.epd.structure

import com.anyaku.crypt.asymmetric.Key

trait Contactable {

    val id: String

    val publicKey: Key

}

package com.anyaku.epd.structure

import com.anyaku.crypt.asymmetric.Key as AsymmetricKey
import com.anyaku.crypt.asymmetric.KeyPair as AsymmetricKeyPair
import com.anyaku.crypt.symmetric.EncryptedData as SymmetricEncryptedData
import com.anyaku.crypt.symmetric.Key as SymmetricKey

trait Factory {

    fun buildUnlockedMapper(): UnlockedMapper

    fun buildLockedMapper(): LockedMapper

    fun buildKey(): SymmetricKey

    fun buildKeyPair(): AsymmetricKeyPair

    fun buildModule(id: String): Module

    fun buildModule(id: String, map: Map<String, Any?>): Module

    class object {

        val latest = com.anyaku.epd.structure.v1.Factory()

    }

}

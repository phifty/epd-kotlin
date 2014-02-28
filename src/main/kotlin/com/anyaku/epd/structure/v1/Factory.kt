package com.anyaku.epd.structure.v1

import com.anyaku.crypt.randomBytes
import com.anyaku.crypt.asymmetric.Key as AsymmetricKey
import com.anyaku.crypt.asymmetric.KeyPair as AsymmetricKeyPair
import com.anyaku.crypt.asymmetric.RSAKeyPair
import com.anyaku.crypt.symmetric.EncryptedData as SymmetricEncryptedData
import com.anyaku.crypt.symmetric.Key as SymmetricKey
import com.anyaku.crypt.symmetric.AESKey
import com.anyaku.epd.structure.Factory as FactoryTrait
import com.anyaku.epd.structure.LockedMapper as LockedMapperTrait
import com.anyaku.epd.structure.UnlockedMapper as UnlockedMapperTrait
import com.anyaku.epd.structure.Module
import com.anyaku.epd.structure.modules.Basic
import java.util.HashMap
import com.anyaku.epd.structure.modules.Unknown

class Factory() : FactoryTrait {

    override fun buildUnlockedMapper(): UnlockedMapperTrait =
            UnlockedMapper(this)

    override fun buildLockedMapper(): LockedMapperTrait =
            LockedMapper(this)

    override fun buildKey(): SymmetricKey =
            AESKey(randomBytes(16))

    override fun buildKeyPair(): AsymmetricKeyPair =
            RSAKeyPair()

    override fun buildModule(id: String): Module =
            buildModule(id, HashMap())

    override fun buildModule(id: String, map: Map<String, Any?>): Module {
        val moduleClass = Module[id]
        val module = try {
            moduleClass.getConstructor(javaClass<String>()).newInstance(id)
        } catch (exception: NoSuchMethodException) {
            moduleClass.newInstance()
        }
        module.fromMap(map)
        return module
    }

}

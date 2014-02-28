package com.anyaku.test.fixtures

import com.anyaku.crypt.Password
import com.anyaku.epd.Generator
import com.anyaku.epd.Locker
import com.anyaku.epd.json.JsonBuilder
import com.anyaku.epd.structure.UnlockedDocument
import com.anyaku.epd.structure.SignedLockedDocument
import com.anyaku.util.readStringFromInputStream
import com.anyaku.epd.PublicKeyResolver
import com.anyaku.crypt.asymmetric.Key
import java.util.HashMap

object Documents {

    object ids {

        fun get(name: String): String =
                locked[name].id

    }

    object json {

        fun get(name: String): String {
            val inputStream = javaClass<Documents>().getResourceAsStream("/fixtures/documents/$name.json")

            if (inputStream == null)
                throw Exception("cannot find profile fixtures with id $name")

            return readStringFromInputStream(inputStream)
        }

    }

    object locked {

        val password: Password = generator.password("Password123")

        val cache = HashMap<String, SignedLockedDocument>()

        fun get(name: String): SignedLockedDocument {
            val result = jsonBuilder.buildLockedDocument(json[name])
            cache[result.id] = result
            return result
        }

        fun generate(unlockedProfile: UnlockedDocument = unlocked.generate()): SignedLockedDocument {
            val encryptedPrivateKey = locker.lock(unlockedProfile.privateKey, password)
            return locker.lock(unlockedProfile, encryptedPrivateKey)
        }

    }

    object unlocked {

        fun get(name: String): UnlockedDocument {
            val lockedProfile = locked[name]
            val password = generator.password("Password123", lockedProfile.privateKey!!.hashParameters)
            val privateKey = locker.unlock(lockedProfile.privateKey, password)
            return locker.unlock(lockedProfile, privateKey)
        }

        fun generate(): UnlockedDocument =
                generator.document()

    }

    val publicKeyResolver = object : PublicKeyResolver {
        override fun resolve(documentId: String): Key? =
                locked.cache[documentId]?.publicKey
    }

    private val jsonBuilder = JsonBuilder()

    private val generator = Generator()

    private val locker = Locker(publicKeyResolver)

}

package com.anyaku.epd

import com.anyaku.epd.structure.Factory
import com.anyaku.epd.structure.UnlockedDocument
import com.anyaku.epd.structure.modules.Basic
import com.anyaku.util.generateId
import com.anyaku.crypt.Password
import com.anyaku.crypt.HashParameters
import com.anyaku.crypt.generatePassword

/**
 * The Generator provides functions to generate unlocked documents and hashed passwords. The constructor can be called
 * with a factory instance that is used to build all the version-specific components of the generated document or
 * password.
 *
 * Example
 * <pre>val generator = Generator()
 * val document = generator.document()
 * val password = generator.password("test123")</pre>
 */
public class Generator(private val factory: Factory = Factory.latest) {

    /**
     * Generates a new unlocked document and returns it. The document will contain all the default components like the
     * public and private key, the own contact-entry, public and private sections and an basic module in the public
     * section.
     */
    fun document(): UnlockedDocument {
        val keyPair = factory.buildKeyPair()
        val document = UnlockedDocument(generateId(), keyPair.publicKey, keyPair.privateKey, factory)

        document.contacts.add(document)

        document.sections.addPublicSection()
        document.sections.publicSection.modules.add(Basic.id)
        document.sections.addPrivateSection()

        return document
    }

    /**
     * Uses the given plain password and a default set of hash parameters to generate a hashed password.
     */
    fun password(plain: String): Password =
            generatePassword(plain)

    /**
     * Uses the given plain password and the hash parameters to generate a hashed password. While the plain password
     * should be simple string that comes most likely from the user's input, the hash parameters specified additions
     * information that is used to generate the hashed version of the password.
     */
    fun password(plain: String, hashParameters: HashParameters): Password =
            generatePassword(plain, hashParameters)

}

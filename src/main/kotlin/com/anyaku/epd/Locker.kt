package com.anyaku.epd

import com.anyaku.crypt.decrypt
import com.anyaku.crypt.encrypt
import com.anyaku.crypt.asymmetric.Key as AsymmetricKey
import com.anyaku.crypt.combined.decryptAny as combinedDecryptAny
import com.anyaku.crypt.combined.encryptAny as combinedEncryptAny
import com.anyaku.crypt.symmetric.Key as SymmetricKey
import com.anyaku.crypt.symmetric.decryptAny as symmetricDecryptAny
import com.anyaku.crypt.symmetric.encryptAny as symmetricEncryptAny
import com.anyaku.epd.structure.UnlockedDocument
import com.anyaku.epd.structure.LockedDocument
import com.anyaku.epd.structure.Factory
import com.anyaku.epd.structure.UnlockedSection
import com.anyaku.epd.structure.LockedSection
import com.anyaku.epd.structure.UnlockedSectionsMap
import com.anyaku.epd.structure.LockedSectionsMap
import com.anyaku.epd.structure.UnlockedKeysMap
import com.anyaku.epd.structure.LockedContactsMap
import com.anyaku.epd.structure.UnlockedContactsMap
import com.anyaku.epd.structure.UnlockedContact
import com.anyaku.epd.structure.UnlockedMapper
import com.anyaku.epd.structure.LockedContact
import com.anyaku.epd.structure.Sections
import com.anyaku.epd.structure.SignedLockedDocument
import com.anyaku.crypt.asymmetric.Key
import com.anyaku.crypt.PasswordEncryptedKey
import com.anyaku.crypt.Password

/**
 * The Locker provides functions to lock a private key or a whole document. At the end of the locking process the
 * [[Signer]] is used to add a signature to the locked document. After this last step, an instance of
 * [[SignedLockedDocument]] is returned by the `lock` function.
 *
 * The revert the locking process `unlock` function for both - private keys and documents - are provided.
 *
 * For initialization, an instance of a [[PublicKeyResolver]] has to be provided. The Locker will use it during the
 * locking, to resolve the public keys for the given contact ids.
 *
 * Locking example
 * <pre>val generator = Generator()
 * val locker = Locker()
 * val password = generator.password("test123")
 * val document = generator.document()
 *
 * val lockedPrivateKey = locker.lock(document.privateKey, password)
 * val lockedDocument = locker.lock(document, lockedPrivateKey)</pre>
 *
 * Unlocking example (given the values from the previous example)
 * <pre>val unlockedPrivateKey = locker.unlock(lockedPrivateKey, password)
 * val unlockedDocument = locker.unlock(lockedDocument, unlockedPrivateKey)</pre>
 */
public class Locker(private val publicKeyResolver: PublicKeyResolver) {

    class InvalidPasswordException: Exception("an invalid password was given")

    /**
     * Locks the given asymmetric key with the given password. The result is of type [[PasswordEncryptedKey]] and
     * contains the encrypted key as well as a set of hash parameters, that is needed to generate the hash password
     * again.
     */
    fun lock(key: Key, password: Password): PasswordEncryptedKey =
            encrypt(key, password)

    /**
     * Unlocks the given encrypted asymmetric key with the given password. The result is the decrypted asymmetric key.
     * If an invalid password is provided, the function should throw an [[InvalidPasswordException]]. The successfuly
     * decrypted key can be used to unlock an LockedDocument using the unlock function.
     */
    fun unlock(encryptedKey: PasswordEncryptedKey, password: Password): Key {
        try {
            return decrypt(encryptedKey, password)
        } catch (exception: javax.crypto.BadPaddingException) {
            throw InvalidPasswordException()
        }
    }

    /**
     * Locks the given document using the key pair inside the given [[UnlockedDocument]]. Optionally, an encrypted
     * private key can be provided. If so, it's stored inside the returned [[SignedLockedDocument]]. In this case,
     * strong hash parameters (aka a high security level) should be used to hash the password that has been used to
     * encrypt the private key. The generated [[SignedLockedDocument]] is usually exposed to the public and thereby
     * accessible to possible attackers.
     * If no encrypted private key is provided, the returned document won't include it and the private key has to be
     * stored somewhere else. This could be - for example - a save area in the memory of the local device. In that case,
     * the key-encryption might need a less stronger password, but the profile access might also be less protable.
     */
    fun lock(
            unlockedDocument: UnlockedDocument,
            encryptedPrivateKey: PasswordEncryptedKey? = null
    ): SignedLockedDocument {
        val unlockedMapper = unlockedDocument.factory.buildUnlockedMapper()

        val lockedDocument = LockedDocument(
                unlockedDocument.id,
                unlockedDocument.publicKey,
                encryptedPrivateKey,
                unlockedDocument.factory)

        lock(lockedDocument.contacts,
                unlockedDocument.contacts,
                unlockedMapper)

        lock(lockedDocument.sections,
                unlockedDocument.sections,
                unlockedDocument.contacts.ownContact.keys,
                unlockedMapper)

        return signer.sign(lockedDocument, unlockedDocument.privateKey)
    }

    /**
     * Unlocks the given document with the given private key. The function returns an [[UnlockedDocument]], which is
     * equal to the [[UnlockedDocument]] that has been used to generate the given [[SignedLockedDocument]].
     */
    fun unlock(lockedDocument: LockedDocument, privateKey: Key): UnlockedDocument {
        val unlockedDocument =
                UnlockedDocument(lockedDocument.id, lockedDocument.publicKey, privateKey, lockedDocument.factory)

        unlock(unlockedDocument.contacts,
                lockedDocument.contacts,
                unlockedDocument.privateKey,
                lockedDocument.factory)

        unlock(unlockedDocument.sections,
                lockedDocument.sections,
                unlockedDocument.contacts,
                lockedDocument.factory.buildUnlockedMapper())

        return unlockedDocument
    }

    /*
     * Locks the given sections with the given keys.
     */
    private fun lock(
            lockedSections: LockedSectionsMap,
            unlockedSections: UnlockedSectionsMap,
            keys: UnlockedKeysMap,
            unlockedMapper: UnlockedMapper
    ) {
        lockedSections.publicSection = unlockedSections.publicSection

        for (entry in unlockedSections.entrySet())
            if (!entry.value.isPublicSection)
                lockedSections[ entry.key ] = lock(entry.value, keys[ entry.key ], unlockedMapper)
    }

    /*
     * Unlocks the given sections with the given keys.
     */
    private fun unlock(
            unlockedSections: UnlockedSectionsMap,
            lockedSections: LockedSectionsMap,
            contacts: UnlockedContactsMap,
            unlockedMapper: UnlockedMapper
    ) {
        val keysMap = contacts.ownContact.keys

        unlockedSections.publicSection = lockedSections.publicSection as UnlockedSection
        unlockedSections.privateSection = unlock(
                lockedSections.privateSection,
                keysMap.privateSectionKey,
                Sections.PRIVATE_SECTION_ID,
                contacts,
                unlockedMapper)

        for (entry in lockedSections.entrySet())
            unlockedSections[ entry.key ] =
                unlock(entry.value, keysMap[ entry.key ], entry.key, contacts, unlockedMapper)
    }

    /*
     * Locks the given contacts.
     */
    private fun lock(
            lockedContacts: LockedContactsMap,
            unlockedContacts: UnlockedContactsMap,
            unlockedMapper: UnlockedMapper
    ) {
        val ownContactId = unlockedContacts.ownContactId
        val ownContactPublicKey = unlockedContacts.ownContactPublicKey

        for (entry in unlockedContacts.entrySet())
            if (!entry.key.equals(ownContactId)) {
                val publicKey = publicKeyResolver.resolve(entry.key)
                if (publicKey != null)
                    lockedContacts[ entry.key ] =
                        lock(entry.value,
                             publicKey,
                             unlockedContacts.ownContact.keys.privateSectionKey,
                             unlockedMapper)
            }

        lockedContacts[ ownContactId ] = lock(unlockedContacts.ownContact, ownContactPublicKey, unlockedMapper)
    }

    /*
     * Unlocks the given contacts.
     */
    private fun unlock(
            unlockedContacts: UnlockedContactsMap,
            lockedContacts: LockedContactsMap,
            privateKey: AsymmetricKey,
            factory: Factory
    ) {
        unlockedContacts.ownContact = unlock(lockedContacts.ownContact, privateKey, factory)
        for (entry in lockedContacts.entrySet()) {
            if (unlockedContacts.ownContactId.equals(entry.key))
                continue

            unlockedContacts[ entry.key ] = unlock(entry.value, unlockedContacts.ownContact.keys, factory)
        }
    }

    /*
     * Locks the given section with the given key.
     */
    private fun lock(
            unlockedSection: UnlockedSection,
            key: SymmetricKey,
            unlockedMapper: UnlockedMapper
    ): LockedSection =
            LockedSection(symmetricEncryptAny(unlockedMapper.sectionToMap(unlockedSection), key))

    /*
     * Unlocks the given section with the given key.
     */
    [ suppress("UNCHECKED_CAST") ]
    private fun unlock(
            lockedSection: LockedSection,
            key: SymmetricKey,
            id: String,
            contacts: UnlockedContactsMap,
            unlockedMapper: UnlockedMapper
    ): UnlockedSection =
            unlockedMapper.sectionFromMap(
                    id,
                    symmetricDecryptAny(lockedSection.encrypted, key) as Map<String, Any?>,
                    contacts)

    /*
     * Locks the given contact.
     */
    private fun lock(
            unlockedContact: UnlockedContact,
            publicKey: AsymmetricKey,
            privateSectionKey: SymmetricKey,
            unlockedMapper: UnlockedMapper
    ): LockedContact =
            LockedContact(
                    combinedEncryptAny(unlockedMapper.keysToMap(unlockedContact.keys), publicKey),
                    symmetricEncryptAny(unlockedContact.sections, privateSectionKey))

    /*
     * Locks the given own contact.
     */
    private fun lock(
            unlockedContact: UnlockedContact,
            publicKey: AsymmetricKey,
            unlockedMapper: UnlockedMapper
    ): LockedContact =
            LockedContact(
                    combinedEncryptAny(unlockedMapper.keysToMap(unlockedContact.keys), publicKey),
                    null)

    /*
     * Unlocks the given contact.
     */
    [ suppress("UNCHECKED_CAST") ]
    private fun unlock(
            lockedContact: LockedContact,
            keys: UnlockedKeysMap,
            factory: Factory
    ): UnlockedContact {
        val unlockedContact = UnlockedContact(factory)
        val lockedSections = lockedContact.sections

        if (lockedSections != null)
            unlockedContact.sections =
                symmetricDecryptAny(lockedSections, keys.privateSectionKey) as MutableCollection<String>

        for (section in unlockedContact.sections)
            unlockedContact.keys[section] = keys[section]

        return unlockedContact
    }

    /*
     * Unlocks the given contact.
     */
    [ suppress("UNCHECKED_CAST") ]
    private fun unlock(
            lockedContact: LockedContact,
            privateKey: AsymmetricKey,
            factory: Factory
    ): UnlockedContact {
        val unlockedContact = UnlockedContact(factory)
        val unlockedMapper = factory.buildUnlockedMapper()

        unlockedContact.keys.setAll(
                unlockedMapper.keysFromMap(combinedDecryptAny(lockedContact.keys, privateKey) as Map<String, Any?>))

        return unlockedContact
    }

    private val signer = Signer()

}

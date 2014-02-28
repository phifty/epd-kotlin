package com.anyaku.epd.structure.v1

import com.anyaku.crypt.PasswordEncryptedKey
import com.anyaku.crypt.PasswordSalt
import com.anyaku.crypt.asymmetric.Key
import com.anyaku.crypt.asymmetric.Signature
import com.anyaku.crypt.coder.decode
import com.anyaku.crypt.coder.encode
import com.anyaku.crypt.combined.EncryptedData as CombinedEncryptedData
import com.anyaku.crypt.symmetric.EncryptedData as SymmetricEncryptedData
import com.anyaku.epd.structure.Contactable
import com.anyaku.epd.structure.LockedContact
import com.anyaku.epd.structure.LockedContactsMap
import com.anyaku.epd.structure.LockedDocument
import com.anyaku.epd.structure.LockedMapper as LockedMapperTrait
import com.anyaku.epd.structure.LockedSectionsMap
import com.anyaku.epd.structure.LockedSection
import com.anyaku.epd.structure.UnlockedSection
import com.anyaku.epd.structure.Sections
import com.anyaku.epd.structure.SignedLockedDocument
import java.util.HashMap
import com.anyaku.crypt.HashParameters

class LockedMapper(private val factory: Factory) : LockedMapperTrait {

    override fun signedDocumentToMap(document: SignedLockedDocument): Map<String, Any?> {
        val result = HashMap(documentToMap(document))

        result["signature"] = encode(document.signature)

        return result
    }

    override fun signedDocumentFromMap(map: Map<String, Any?>): SignedLockedDocument {
        val document = documentFromMap(map)
        val signature = decode(map["signature"] as String) as Signature

        val result = SignedLockedDocument(
                document.id,
                document.publicKey,
                document.privateKey,
                signature,
                document.factory)

        result.contacts.setAll(document.contacts)
        result.sections.setAll(document.sections)

        return result
    }

    override fun documentToMap(document: LockedDocument): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        result["id"] = document.id
        result["publicKey"] = encode(document.publicKey)
        if (document.privateKey != null)
            result["privateKey"] = passwordEncryptedKeyToMap(document.privateKey)
        result["contacts"] = contactsToMap(document.contacts)
        result["sections"] = sectionsToMap(document.sections)
        result["version"] = 1

        return result
    }

    [ suppress("UNCHECKED_CAST") ]
    override fun documentFromMap(map: Map<String, Any?>): LockedDocument {
        val id = map["id"] as String
        val publicKey = decode(map["publicKey"] as String) as Key
        val privateKey = if (map.containsKey("privateKey"))
                             passwordEncryptedKeyFromMap(map["privateKey"] as Map<String, Any?>)
                         else null

        val result = LockedDocument(id, publicKey, privateKey, factory)

        result.contacts.setAll(contactsFromMap(map["contacts"] as Map<String, Any?>, result))
        result.sections.setAll(sectionsFromMap(map["sections"] as Map<String, Any?>))

        return result
    }

    fun passwordEncryptedKeyToMap(key: PasswordEncryptedKey): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        result.put("encrypted", encode(key.encrypted))
        result.put("salt", encode(PasswordSalt(key.hashParameters.salt)))
        result.put("iterations", key.hashParameters.iterations)
        result.put("keySize", key.hashParameters.keySize / 32)

        return result
    }

    fun passwordEncryptedKeyFromMap(map: Map<String, Any?>): PasswordEncryptedKey =
            PasswordEncryptedKey(
                decode(map.get("encrypted") as String) as SymmetricEncryptedData,
                HashParameters(
                        (decode(map.get("salt") as String) as PasswordSalt).data,
                        (map.get("iterations") as Number).toInt(),
                        (map.get("keySize") as Number).toInt() * 32))

    fun contactsToMap(contacts: LockedContactsMap): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        for (entry in contacts.entrySet())
            result[ entry.key ] = contactToMap(entry.value)

        return result
    }

    [ suppress("UNCHECKED_CAST") ]
    fun contactsFromMap(map: Map<String, Any?>, contactable: Contactable): LockedContactsMap {
        val result = LockedContactsMap(contactable)

        for (entry in map.entrySet())
            result[ entry.key ] = contactFromMap(entry.value as Map<String, Any?>)

        return result
    }

    fun contactToMap(contact: LockedContact): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        result["keys"] = encode(contact.keys)
        if (contact.sections != null)
            result["sections"] = encode(contact.sections as SymmetricEncryptedData)

        return result
    }

    fun contactFromMap(map: Map<String, Any?>): LockedContact =
            LockedContact(
                    decode(map["keys"] as String) as CombinedEncryptedData,
                    if (map["sections"] == null) null else decode(map["sections"] as String) as SymmetricEncryptedData)

    fun sectionsToMap(sections: LockedSectionsMap): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        for (entry in sections.entrySet())
            if (!Sections.PUBLIC_SECTION_ID.equals(entry.key))
                result[ entry.key ] = encode(entry.value.encrypted)

        result[ Sections.PUBLIC_SECTION_ID ] = unlockedMapper.sectionToMap(sections.publicSection as UnlockedSection)

        return result
    }

    [ suppress("UNCHECKED_CAST") ]
    fun sectionsFromMap(map: Map<String, Any?>): LockedSectionsMap {
        val result = LockedSectionsMap()

        for (entry in map.entrySet())
            if (!Sections.PUBLIC_SECTION_ID.equals(entry.key))
                result[ entry.key ] = LockedSection(decode(entry.value as String) as SymmetricEncryptedData)

        result.publicSection = unlockedMapper.sectionFromMap(
                Sections.PUBLIC_SECTION_ID,
                map[ Sections.PUBLIC_SECTION_ID ] as Map<String, Any?>,
                null)

        return result
    }

    private val unlockedMapper = UnlockedMapper(factory)

}

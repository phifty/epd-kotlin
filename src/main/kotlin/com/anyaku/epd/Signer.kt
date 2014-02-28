package com.anyaku.epd

import com.anyaku.crypt.coder
import com.anyaku.crypt.asymmetric.Key
import com.anyaku.crypt.asymmetric.RSAKey
import com.anyaku.crypt.asymmetric.Signature
import com.anyaku.crypt.asymmetric.Signer as AsymmetricSigner
import com.anyaku.epd.structure.LockedDocument
import com.anyaku.epd.structure.Factory
import com.anyaku.map.stringify
import java.util.HashMap
import com.anyaku.epd.structure.SignedLockedDocument

/**
 * The Signer provides functions to sign and verify a document. This should ensure, that a document can only be
 * modified by its owner (the person who owns the decrypted private key). If modified by someone else, the signature
 * would brake and it's invalid state would be exposed to anyone.
 */
public class Signer {

    /**
     * Checks if the given [[SignedLockedDocument]] has a valid signature. If so, true is returned, false otherwise.
     */
    fun verify(document: SignedLockedDocument): Boolean {
        val signer = AsymmetricSigner(stringify(strippedDocumentMap(document)))
        return signer.verify(document.publicKey, document.signature)
    }

    /**
     * Signs the given [[LockedDocument]] using the given private key. The signature will be injected in to document
     * and a [[SignedLockedDocument]] will be returned.
     */
    fun sign(document: LockedDocument, key: Key): SignedLockedDocument {
        val signer = AsymmetricSigner(stringify(strippedDocumentMap(document)))
        val signedDocument = SignedLockedDocument(
                document.id,
                document.publicKey,
                document.privateKey,
                signer.sign(key as RSAKey),
                document.factory)

        signedDocument.contacts.setAll(document.contacts)
        signedDocument.sections.setAll(document.sections)

        return signedDocument
    }

    private fun strippedDocumentMap(document: LockedDocument): Map<String, Any?> {
        val result = HashMap<String, Any?>()

        val mapper = document.factory.buildLockedMapper()
        for (entry in mapper.documentToMap(document).entrySet())
            if (!"signature".equals(entry.key))
                result[ entry.key ] = entry.value

        return result
    }

}

package com.anyaku.epd.structure

trait LockedMapper {

    fun signedDocumentToMap(document: SignedLockedDocument): Map<String, Any?>

    fun signedDocumentFromMap(map: Map<String, Any?>): SignedLockedDocument

    fun documentToMap(document: LockedDocument): Map<String, Any?>

    fun documentFromMap(map: Map<String, Any?>): LockedDocument

}

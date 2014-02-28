package com.anyaku.test.integration

import com.anyaku.epd.structure.Factory
import com.anyaku.epd.Signer
import com.anyaku.json.parseJson
import com.anyaku.map.stringify
import com.anyaku.test.integration.javascript.runInWorker
import java.util.HashMap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SignerTest() {

    val signer = Signer()
    val mapper = Factory.latest.buildLockedMapper()

    [ Test suppress("UNCHECKED_CAST") ]
    fun testFreshlyGeneratedProfile() {
        runInWorker("var profile = epdRoot.Generator.generate(512);" +
            "profile.sections.open.modules['build_in:com.anyaku.Basic'].content = { name: 'Mr Test', gravata: undefined, country: 'NON' };" +
            "var lockedProfile = epdRoot.Locker.lock(profile, password, function (id) { return id === profile.id ? profile.publicKey : null; });")
        val profileJson = runInWorker("JSON.stringify(lockedProfile);") as String
        val profileMap = parseJson(profileJson) as MutableMap<String, Any?>
        val profile = mapper.signedDocumentFromMap(HashMap(profileMap))

        val serializedProfile = runInWorker("delete(lockedProfile.signature); epdRoot.Object.stringify(lockedProfile);") as String
        profileMap.remove("signature")
        assertEquals(serializedProfile, stringify(profileMap))

        assertTrue(signer.verify(profile))
    }

}

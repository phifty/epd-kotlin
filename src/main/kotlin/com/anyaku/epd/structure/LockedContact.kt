package com.anyaku.epd.structure

import com.anyaku.crypt.combined.EncryptedData as CombinedEncryptedData
import com.anyaku.crypt.symmetric.EncryptedData as SymmetricEncryptedData

class LockedContact(
        val keys: CombinedEncryptedData,
        val sections: SymmetricEncryptedData?)

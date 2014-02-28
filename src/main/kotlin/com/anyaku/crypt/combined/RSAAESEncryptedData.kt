package com.anyaku.crypt.combined

import com.anyaku.crypt.asymmetric.EncryptedData as AsymmetricEncryptedData
import com.anyaku.crypt.symmetric.EncryptedData as SymmetricEncryptedData

class RSAAESEncryptedData(key: AsymmetricEncryptedData, data: SymmetricEncryptedData) : EncryptedData {

    override val key = key

    override val data = data

}

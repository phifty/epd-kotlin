package com.anyaku.crypt.combined

import com.anyaku.crypt.asymmetric.EncryptedData as AsymmetricEncryptedData
import com.anyaku.crypt.symmetric.EncryptedData as SymmetricEncryptedData

trait EncryptedData {

    val key: AsymmetricEncryptedData

    val data: SymmetricEncryptedData

}

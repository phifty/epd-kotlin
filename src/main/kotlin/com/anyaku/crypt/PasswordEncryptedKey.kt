package com.anyaku.crypt

import com.anyaku.crypt.PasswordSalt
import com.anyaku.crypt.symmetric.EncryptedData

/**
 * Contains an encrypted asymmetric key. The key is encrypted using a hashed password and symmetric encryption. A set
 * of hash parameters is also stored in this class in order to enable the user to re-produce the hashed password.
 */
public class PasswordEncryptedKey(
        val encrypted: EncryptedData,
        val hashParameters: HashParameters)

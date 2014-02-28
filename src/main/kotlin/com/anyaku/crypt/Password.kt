package com.anyaku.crypt

import com.anyaku.crypt.symmetric.AESKey

/**
 * Contains a hashed password. This includes a [[ByteArray]] with the hashed password itself, but also a
 * [[HashParameters]] instance.
 */
public class Password(val hash: ByteArray, val hashParameters: HashParameters)

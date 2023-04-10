package mine.utils

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

object Hasher {

    fun hashString(input: String, algorithm: String, encoding: Charset): String {

        val bytes = MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray(encoding))

        return BigInteger(1, bytes).toString(16).padStart(32, '0')
    }

}
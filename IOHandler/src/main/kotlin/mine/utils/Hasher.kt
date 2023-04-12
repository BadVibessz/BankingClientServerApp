package mine.utils

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

object Hasher {


    // todo: use bcrypt/scrypt or PBDKF2
    //  https://security.stackexchange.com/questions/16354/whats-the-advantage-of-using-pbkdf2-vs-sha256-to-generate-an-aes-encryption-key

    fun hashString(input: String, algorithm: String, encoding: Charset): String {

        val bytes = MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray(encoding))

        return BigInteger(1, bytes).toString(16).padStart(32, '0')
    }

}
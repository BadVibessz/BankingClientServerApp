package mine.cryptography.symmetric

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object AES {

    fun generateSecretKey() = KeyGenerator.getInstance("AES")
        .apply { init(256) }.generateKey()

    fun encrypt(data: ByteArray, key: SecretKey): Pair<ByteArray, ByteArray> {

        val encryptor = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            .apply { init(Cipher.ENCRYPT_MODE, key) }

        return Pair<ByteArray, ByteArray>(encryptor.doFinal(data), encryptor.iv)
    }

    fun decrypt(data: ByteArray, key: SecretKey, iv: ByteArray): ByteArray {

        val decryptor = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            .apply { init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv)) }

        return decryptor.doFinal(data)
    }

}
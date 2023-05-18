package mine.cryptography.asymmetric

import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSA {

    fun generateKeyPair() = KeyPairGenerator.getInstance("RSA")
        .apply { initialize(2048, SecureRandom()) }
        .genKeyPair()

    fun getPublicKeyFromByteArray(bytes: ByteArray) = KeyFactory.getInstance("RSA")
        .generatePublic(X509EncodedKeySpec(bytes))

    fun encrypt(data: ByteArray, key: PublicKey): ByteArray {

        val encryptor = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            .apply { init(Cipher.ENCRYPT_MODE, key) }

        return encryptor.doFinal(data)
    }

    fun decrypt(data: ByteArray, key: PrivateKey): ByteArray
    {
        val decryptor = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            .apply { init(Cipher.DECRYPT_MODE, key) }

        return decryptor.doFinal(data)
    }

}
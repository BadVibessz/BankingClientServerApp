package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

object RegisterAPI {


    // todo: is it safe to hash on client?
    private fun hashString(input: String, algorithm: String, encoding: Charset): String {

        val bytes = MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray(encoding))

        return BigInteger(1, bytes).toString(16).padStart(32,'0')
    }

    fun register(email: String, password: String, communicator: Communicator) {

        val hashedPassword = hashString(password, "SHA-256", UTF_8)

        val service = "register-service"
        val requestCommand = "register-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["email"] = email
        requestContent["password"] = hashedPassword


        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)

    }
}
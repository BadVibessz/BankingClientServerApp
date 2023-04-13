package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request
import mine.utils.Hasher
import kotlin.text.Charsets.UTF_8

object RegisterAPI {

    var salt: String? = null
    var serverChallenge: String? = null

    // todo: is it safe to hash on client?

    fun register(email: String, password: String, communicator: Communicator) {

        // todo: https://habr.com/ru/articles/594071/

        // todo: use bcrypt/scrypt or PBDKF2
        //  https://security.stackexchange.com/questions/16354/whats-the-advantage-of-using-pbkdf2-vs-sha256-to-generate-an-aes-encryption-key

        //TODO: in new coroutine?

        var service = "register-service"
        var requestCommand = "generate-salt-and-challenge-command"

        var request = Request(service, requestCommand, null)

        var json = Gson().toJson(request)
        communicator.send(json)

        // todo: wait for response with salt to be received

        val hashedPassword = Hasher.hashString(password, "SHA-256", UTF_8)

        service = "register-service"
        requestCommand = "register-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["email"] = email
        requestContent["password"] = hashedPassword


        request = Request(service, requestCommand, requestContent)

        json = Gson().toJson(request)
        communicator.send(json)

    }
}
package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request
import mine.utils.Hasher
import kotlin.text.Charsets.UTF_8

object RegisterAPI {


    // todo: is it safe to hash on client?


    fun register(email: String, password: String, communicator: Communicator) {

        val hashedPassword = Hasher.hashString(password, "SHA-256", UTF_8)

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
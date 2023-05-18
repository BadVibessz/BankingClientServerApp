package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request
import mine.utils.Hasher


object LoginAPI {

    fun login(email: String, password: String, callback: (String) -> Unit) {

        val service = "login-service"
        val requestCommand = "login-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["email"] = email
        requestContent["password"] = Hasher.hashString(password, "SHA-256", Charsets.UTF_8)

        // todo: use rsa + aes

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }


}
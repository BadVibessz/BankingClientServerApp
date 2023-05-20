package mine.api

import com.google.gson.Gson
import mine.requests.Request
import mine.utils.Hasher
import kotlin.reflect.KSuspendFunction1


object LoginAPI {

    suspend fun login(email: String, password: String, callback: KSuspendFunction1<String, Unit>) {

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
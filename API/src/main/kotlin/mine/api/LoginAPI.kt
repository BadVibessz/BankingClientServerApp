package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request


object LoginAPI {

    fun login(email: String, password: String, communicator: Communicator) {

        val service = "login-service"
        val requestCommand = "login-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["email"] = email
        requestContent["password"] = password


        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }


}
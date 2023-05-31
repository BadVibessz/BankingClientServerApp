package mine.api

import com.google.gson.Gson
import mine.requests.Request
import mine.utils.JsonUtils

object BankClientAPI {

    fun get(callback: (String) -> Unit){

        val service = "client-service"
        val requestCommand = "get-command"

        val request = Request(service, requestCommand, null)
        val json = Gson().toJson(request)
        callback(json)
    }

}
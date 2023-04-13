package mine.client

import com.google.gson.Gson
import mine.api.RegisterAPI
import mine.responses.Response

object ServerResponseHandler {

    private val _gson = Gson()

    fun handleRespone(json: String) {
        val response = _gson.fromJson(json, Response::class.java)

        val status = response.status
        val message = response.message
        val content = response.content
        val request = response.request

        when(request.command)
        {
            "generate-salt-and-challenge-command" ->
            {
                if(content == null) { TODO()}

                RegisterAPI.salt = content["salt"] as String
                RegisterAPI.serverChallenge = content["challenge"] as String
            }
        }

        println(message)
    }

}
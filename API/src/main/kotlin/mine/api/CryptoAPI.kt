package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request

object CryptoAPI {


    fun getRSAPublicKey(communicator: Communicator) {

        val service = "crypto-service"
        val requestCommand = "get-rsa-public-key"

        val request = Request(service, requestCommand, null)
        communicator.send(Gson().toJson(request))
    }

}
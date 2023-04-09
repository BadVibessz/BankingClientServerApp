package mine.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.gson.Gson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mine.Communicator
import mine.requests.Request
import mine.statuses.StatusCode
import mine.types.AccountType
import java.util.concurrent.CancellationException

object AccountAPI {

    //private val _gson =

    fun create(name: String, accountType: AccountType, communicator: Communicator) {

        // todo: form request and send it on server using communicator

        val service = "account-service"
        val requestCommand = "create-command"

//        val requestContent = object {
//            val bankAccountName = name
//            val bankAccountType = accountType
//        }

        val requestContent = mutableMapOf<String, Any>()
        requestContent["bankAccountName"] = name
        requestContent["bankAccountType"] = accountType



        val request = Request(service, requestCommand, requestContent)
        //val json = Json.encodeToString(request)
        // json = jacksonObjectMapper().writeValueAsString(request)

        val json = Gson().toJson(request)
        communicator.send(json)

    }

    // todo: delete, update

}
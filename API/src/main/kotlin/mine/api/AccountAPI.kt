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

        val requestContent = mutableMapOf<String, Any>()
        requestContent["name"] = name
        requestContent["type"] = accountType

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }

    fun update(id: Int, newName: String, communicator: Communicator) {
        val service = "account-service"
        val requestCommand = "update-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id
        requestContent["newName"] = newName

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)

    }

    fun delete(id: Int, communicator: Communicator) {
        val service = "account-service"
        val requestCommand = "delete-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }

    fun get(id: Int, communicator: Communicator) {
        val service = "account-service"
        val requestCommand = "get-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }

    fun getAll(communicator: Communicator) {
        val service = "account-service"
        val requestCommand = "get-all-command"

        val requestContent = mutableMapOf<String, Any>()

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }


}
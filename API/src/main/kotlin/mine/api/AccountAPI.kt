package mine.api

import com.google.gson.Gson
import mine.requests.Request
import mine.types.AccountType
import kotlin.reflect.KSuspendFunction1

object AccountAPI {

    //private val _gson =

    // TODO: DO NOT CREATE ACCOUNTS WITH WHITESPACES IN NAME
    fun create(name: String, accountType: AccountType, callback: (String) -> Unit) {

        // todo: form request and send it on server using communicator

        val service = "account-service"
        val requestCommand = "create-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["name"] = name
        requestContent["type"] = accountType

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

    fun update(id: Int, newName: String, callback: (String) -> Unit) {
        val service = "account-service"
        val requestCommand = "update-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id
        requestContent["newName"] = newName

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)

    }

    fun delete(id: Int, callback: (String) -> Unit) {
        val service = "account-service"
        val requestCommand = "delete-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

    fun get(id: Int, callback: (String) -> Unit) {
        val service = "account-service"
        val requestCommand = "get-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

    suspend fun getAll(callback: KSuspendFunction1<String, Unit>) {
        val service = "account-service"
        val requestCommand = "get-all-command"

        val requestContent = mutableMapOf<String, Any>()

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }


}
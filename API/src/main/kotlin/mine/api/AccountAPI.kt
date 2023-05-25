package mine.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.gson.Gson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mine.Communicator
import mine.models.BankAccountModel
import mine.requests.Request
import mine.statuses.StatusCode
import mine.types.AccountType
import mine.utils.JsonUtils
import org.joda.time.DateTime
import java.util.concurrent.CancellationException

object AccountAPI {

    //private val _gson =

    // TODO: DO NOT CREATE ACCOUNTS WITH WHITESPACES IN NAME
    fun create(model: BankAccountModel, callback: (String) -> Unit) {

        // TODO: SERIALIZE MODEL AND DESERIALIZE IT ON SERVER

        val service = "account-service"
        val requestCommand = "create-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["firstOrder"] = model.firstOrder
        requestContent["secondOrder"] = model.secondOrder
        requestContent["checkDigit"] = model.checkDigit
        requestContent["currency"] = model.currency
        requestContent["type"] = model.type
        requestContent["department"] = model.department
        requestContent["expiresAt"] = JsonUtils.gsonDateTime()!!.toJson(model.expiresAt)

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

    fun update(id: Int, newDate: DateTime, callback: (String) -> Unit) {
        val service = "account-service"
        val requestCommand = "update-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id
        requestContent["newDate"] = JsonUtils.gsonDateTime()!!.toJson(newDate)

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

    fun get(number: String, callback: (String) -> Unit) {
        val service = "account-service"
        val requestCommand = "get-by-number-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["number"] = number

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

    fun getAll(callback: (String) -> Unit) {
        val service = "account-service"
        val requestCommand = "get-all-command"

        val requestContent = mutableMapOf<String, Any>()

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }


}
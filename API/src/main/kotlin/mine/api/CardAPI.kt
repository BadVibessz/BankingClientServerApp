package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.models.CardModel
import mine.requests.Request
import mine.types.CardType

object CardAPI {

    fun create(model: CardModel, accountId: Int, callback: (String) -> Unit)
    {
        val service = "card-service"
        val requestCommand = "create-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["name"] = model.name
        requestContent["type"] = model.type
        requestContent["accountId"] = accountId

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

    fun update(id: Int, newName: String, callback: (String) -> Unit) {

        val service = "card-service"
        val requestCommand = "update-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id
        requestContent["newName"] = newName

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

    fun delete(id: Int, callback: (String) -> Unit) {
        val service = "card-service"
        val requestCommand = "delete-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

    fun get(id: Int, callback: (String) -> Unit) {
        val service = "card-service"
        val requestCommand = "get-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id as Int

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

    fun getAll(callback: (String) -> Unit) {
        val service = "card-service"
        val requestCommand = "get-all-command"

        val requestContent = mutableMapOf<String, Any>()

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }

}
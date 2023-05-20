package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request
import mine.types.CardType

object CardAPI {

    // todo: create, delete, update, transfer(money), deposit, withdraw

    suspend fun create(name: String, type: CardType, accountId: Int, communicator: Communicator)
    {
        val service = "card-service"
        val requestCommand = "create-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["name"] = name
        requestContent["type"] = type
        requestContent["accountId"] = accountId


        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }

    suspend fun update(id: Int, newName: String, communicator: Communicator) {

        val service = "card-service"
        val requestCommand = "update-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id
        requestContent["newName"] = newName

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)

    }

    suspend fun delete(id: Int, communicator: Communicator) {
        val service = "card-service"
        val requestCommand = "delete-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }

    suspend fun get(id: Int, communicator: Communicator) {
        val service = "card-service"
        val requestCommand = "get-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["id"] = id as Int

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }

    suspend fun getAll(communicator: Communicator) {
        val service = "card-service"
        val requestCommand = "get-all-command"

        val requestContent = mutableMapOf<String, Any>()

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }

}
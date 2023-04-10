package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request
import mine.types.CardType

object CardAPI {

    // todo: create, delete, update, transfer(money), deposit, withdraw

    fun create(name: String, type: CardType, accountName: String, communicator: Communicator)
    {
        val service = "card-service"
        val requestCommand = "create-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["name"] = name
        requestContent["type"] = type
        requestContent["accountName"] = accountName


        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        communicator.send(json)
    }

}
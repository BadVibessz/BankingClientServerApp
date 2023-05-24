package mine.api

import com.google.gson.Gson
import mine.models.TransactionModel
import mine.requests.Request

object TransactionAPI {

    fun create(model: TransactionModel, callback: (String) -> Unit) {

        val service = "transaction-service"
        val requestCommand = "create-command"

        val requestContent = mutableMapOf<String, Any>()
        requestContent["cardId"] = model.cardId
        requestContent["receiverPhone"] = model.receiverPhone
        requestContent["amount"] = model.amount

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)

    }

    fun getAll(callback: (String) -> Unit) {

        val service = "transaction-service"
        val requestCommand = "get-all-command"

        val request = Request(service, requestCommand, null)
        val json = Gson().toJson(request)
        callback(json)
    }

}
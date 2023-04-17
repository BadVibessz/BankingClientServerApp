package mine.client

import com.google.gson.Gson
import mine.responses.Response
import mine.serializable.BankAccountSerializable
import mine.serializable.CardSerializable
import mine.types.ResponseType

object ServerResponseHandler {

    private val _gson = Gson()

    fun handleRespone(json: String) {
        val response = _gson.fromJson(json, Response::class.java)

        val status = response.status
        val message = response.message
        val content = response.content
        val request = response.request

        when (response.type) {
            ResponseType.SingleAccount -> {
                val account = _gson.fromJson(
                    content!!["account"].toString(),
                    BankAccountSerializable::class.java
                )

                // todo: ??
            }

            ResponseType.AccountList -> {
                val accounts = _gson.fromJson(
                    content!!["accounts"].toString(),
                    Array<BankAccountSerializable>::class.java
                ).toList()

                // todo: update client's account list
            }

            ResponseType.SingleCard -> {
                val card = _gson.fromJson(
                    content!!["card"].toString(),
                    CardSerializable::class.java
                )

                // todo: ??
            }

            ResponseType.CardList -> {
                val cards = _gson.fromJson(
                    content!!["cards"].toString(),
                    Array<CardSerializable>::class.java
                ).toList()

                // todo: update client's card list
            }

            else -> {

            }
        }

        println(message)
    }

}
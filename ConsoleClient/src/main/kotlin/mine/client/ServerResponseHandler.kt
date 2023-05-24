package mine.client

import com.google.gson.Gson
import mine.cryptography.asymmetric.RSA
import mine.responses.Response
import mine.serializable.BankAccountSerializable
import mine.serializable.CardSerializable
import mine.serializable.TransactionSerializable
import mine.types.ResponseType

object ServerResponseHandler {

    private val _gson = Gson()

    fun handleRespone(json: String, client: Client) {

        if (client.ui == null) return

        val response = _gson.fromJson(json, Response::class.java)

        val status = response.status
        val message = response.message
        val content = response.content
        val request = response.request


        client.ui.showMessage(message)

        when (response.type) {

            ResponseType.RSAPublicKey -> {


                val bytes = (content!!["bytes"] as ArrayList<Int>)
                    .map { it.toByte() }.toByteArray()

                client.rsaPublicKey = RSA.getPublicKeyFromByteArray(bytes)
            }

            ResponseType.SingleAccount -> {
                val account = _gson.fromJson(
                    content!!["account"].toString(),
                    BankAccountSerializable::class.java
                )

                client.ui.showAccount(account)

                // todo: ??
            }

            ResponseType.AccountList -> {
                val accounts = _gson.fromJson(
                    content!!["accounts"].toString(),
                    Array<BankAccountSerializable>::class.java
                ).toList()

                client.ui.updateAccountsList(accounts)

                // todo: update client's account list
            }

            ResponseType.SingleCard -> {
                val card = _gson.fromJson(
                    content!!["card"].toString(),
                    CardSerializable::class.java
                )

                client.ui.showCard(card)

                // todo: ??
            }

            ResponseType.CardList -> {
                val cards = _gson.fromJson(
                    content!!["cards"].toString(),
                    Array<CardSerializable>::class.java
                ).toList()

                client.ui.updateCardsList(cards)
            }

            ResponseType.TransactionList -> {
                val transactions = _gson.fromJson(
                    content!!["transactions"].toString(),
                    Array<TransactionSerializable>::class.java
                ).toList()

                client.ui.updateTransactionsList(transactions)
            }


            else -> {

                when (request.command) {
                    "register-command" -> client.ui.showAlert(message)
                    "login-command" -> client.ui.onSuccessfulLogin()
                }

            }
        }

    }

}
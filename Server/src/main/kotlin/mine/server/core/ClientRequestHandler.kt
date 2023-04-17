package mine.server.core

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request
import mine.responses.Response
import mine.serializable.BankAccountSerializable
import mine.serializable.CardSerializable
import mine.server.core.services.BankAccountService
import mine.server.core.services.CardService
import mine.server.core.services.LoginService
import mine.server.core.services.RegisterService
import mine.server.entities.BankAccount
import mine.server.entities.Card
import mine.statuses.StatusCode
import mine.types.AccountType
import mine.types.CardType
import mine.types.ResponseType
import org.jetbrains.exposed.sql.transactions.transaction

object ClientRequestHandler {

    private val _gson = Gson()


    private fun cardToCardSerializable(card: Card) =
        CardSerializable(
            card.name,
            card.account.value,
            card.type,
            card.balance
        )


    private fun bankAccountToSerializable(account: BankAccount): BankAccountSerializable {
        val cardsSerializable = transaction {
            account.cards.toList()
        }.map { cardToCardSerializable(it) }

        return BankAccountSerializable(
            account.name,
            account.client.value,
            account.type,
            account.balance,
            cardsSerializable
        )
    }


    fun handleRequest(json: String, client: ConnectedClient, communicator: Communicator) {

        /*todo:
        *  1) prevent code duplication
        *
        */


        val request = _gson.fromJson(json, Request::class.java)

        when (request.service) {

            "register-service" -> {
                val service = RegisterService()

                when (request.command) {
                    "register-command" -> {

                        val content = request.content!!
                        val email = content["email"] as String
                        val password = content["password"] as String

                        request.content = null // hide pass and email

                        val succeeded = service.register(email, password)

                        val status = if (succeeded) StatusCode.OK else StatusCode.BadRequest
                        val message = if (succeeded) "You have successfully registered!" else "Something went wrong"

                        val response = Response(status, message, null, request, ResponseType.NoContent)

                        communicator.send(_gson.toJson(response))
                    }
                }


            }

            "login-service" -> {
                val service = LoginService()

                when (request.command) {
                    "login-command" -> {

                        val content = request.content!!
                        val email = content["email"] as String
                        val password = content["password"] as String

                        request.content = null // hide pass and email

                        val bankClient = service.login(email, password)

                        val status: StatusCode
                        val message: String

                        if (bankClient != null) {
                            client.bankClient = bankClient

                            status = StatusCode.OK
                            message = "You have successfully logged in!"

                        } else {
                            status = StatusCode.NotFound
                            message = "Wrong email or password"
                        }

                        val response = Response(status, message, null, request, ResponseType.NoContent)
                        communicator.send(_gson.toJson(response))
                    }

                    "logout-command" -> {
                        TODO()
                    }
                }
            }

            "account-service" -> {
                val service = BankAccountService()

                // unauthorized
                if (client.bankClient == null) {

                    val response = Response(
                        StatusCode.Unauthorized,
                        "You are not logged in!",
                        null,
                        request,
                        ResponseType.NoContent
                    )

                    communicator.send(_gson.toJson(response))
                    return
                }


                when (request.command) {

                    "create-command" -> {
                        val content = request.content!!
                        val name = content["name"] as String


                        // todo: do it better (serialize from json to actual AccountType enum)
                        val type = when ((content["type"] as String).lowercase()) {
                            "checking" -> AccountType.Checking
                            "saving" -> AccountType.Saving
                            "credit" -> AccountType.Credit

                            else -> null
                        }

                        if (type == null) {

                            val response = Response(
                                StatusCode.Unknown,
                                "Unknown account type",
                                null,
                                request,
                                ResponseType.NoContent
                            )

                            communicator.send(_gson.toJson(response))
                            return
                        }

                        val succeeded = service.create(name, type, client.bankClient!!)

                        val status = if (succeeded) StatusCode.OK else StatusCode.BadRequest
                        val message = if (succeeded) "You have successfully created account" else "Something went wrong"

                        val response = Response(status, message, null, request, ResponseType.NoContent)
                        communicator.send(_gson.toJson(response))
                        return
                    }

                    "update-command" -> {

                        val content = request.content!!

                        // todo: acc does not exist
                        val account = service.get((content["id"] as Double).toInt())

                        // client can't change account he doesn't own
                        if (account == null || account.client.value != client.bankClient!!.id.value) {
                            val response = Response(
                                StatusCode.BadRequest,
                                "No such account", null, request,
                                ResponseType.NoContent
                            )
                            communicator.send(_gson.toJson(response))

                            return
                        }

                        val newName = content["newName"] as String

                        var status: StatusCode
                        var message: String
                        try {
                            service.update(account, newName)

                            val response = Response(
                                StatusCode.OK,
                                "You have successfully updated an account!",
                                null, request, ResponseType.NoContent
                            )
                            communicator.send(_gson.toJson(response))

                            return

                        } catch (_: Exception) {

                            val response = Response(
                                StatusCode.BadRequest,
                                "Something went wrong", null, request,
                                ResponseType.NoContent
                            )
                            communicator.send(_gson.toJson(response))

                            return
                        }
                    }

                    "delete-command" -> {

                        val content = request.content!!
                        val account = service.get((content["id"] as Double).toInt())

                        // client cannot delete account he doesn't own
                        if (account == null || account.client.value != client.bankClient!!.id.value) {

                            val response = Response(
                                StatusCode.NotFound,
                                "There's no such account", null, request,
                                ResponseType.NoContent
                            )
                            communicator.send(_gson.toJson(response))
                            return
                        }

                        service.delete(account)

                        val response = Response(
                            StatusCode.OK,
                            "You have successfully deleted an account",
                            null, request, ResponseType.NoContent
                        )
                        communicator.send(_gson.toJson(response))
                        return
                    }

                    "get-command" -> {
                        val content = request.content!!

                        val account = service.get((content["id"] as Double).toInt())

                        // client cannot get account he doesn't own
                        if (account == null || account.client.value != client.bankClient!!.id.value) {

                            val response = Response(
                                StatusCode.NotFound,
                                "There's no such account", null, request, ResponseType.NoContent
                            )
                            communicator.send(_gson.toJson(response))
                            return
                        }


                        val responseContent = mutableMapOf<String, Any>()
                        responseContent["account"] = bankAccountToSerializable(account)

                        val response = Response(
                            StatusCode.OK,
                            "There you go =)", responseContent, request,
                            ResponseType.SingleAccount
                        )
                        communicator.send(_gson.toJson(response))
                        return
                    }

                    // return not all accounts but all accounts associated with this client
                    "get-all-command" -> {

                        val accounts = transaction {
                            client.bankClient!!.accounts.toList()
                        }.map { bankAccountToSerializable(it) }

                        val responseContent = mutableMapOf<String, Any>()
                        responseContent["accounts"] = accounts

                        val response = Response(
                            StatusCode.OK,
                            "There you go =)", responseContent, request,
                            ResponseType.AccountList
                        )
                        communicator.send(_gson.toJson(response))
                        return
                    }

                }
            }

            "card-service" -> {
                val service = CardService()

                if (client.bankClient == null) {

                    val response = Response(
                        StatusCode.Unauthorized,
                        "You are not logged in!",
                        null,
                        request,
                        ResponseType.NoContent
                    )

                    communicator.send(_gson.toJson(response))
                    return
                }


                when (request.command) {
                    "create-command" -> {

                        val content = request.content!!

                        val name = content["name"] as String
                        val account = transaction {
                            BankAccountService().get((content["accountId"] as Double).toInt())
                        }

                        // client cannot create card for account he doesn't own
                        if (account == null || account.client.value != client.bankClient!!.id.value) {

                            val response = Response(
                                StatusCode.NotFound,
                                "There's no such account",
                                null, request,
                                ResponseType.NoContent
                            )
                            communicator.send(_gson.toJson(response))
                            return
                        }


                        val type = when ((content["type"] as String).lowercase()) {
                            "debit" -> CardType.Debit
                            "credit" -> CardType.Credit

                            else -> null
                        }

                        if (type == null) {

                            val response = Response(
                                StatusCode.Unknown,
                                "Unknown card type",
                                null,
                                request,
                                ResponseType.NoContent
                            )

                            communicator.send(_gson.toJson(response))
                            return
                        }

                        val succeeded = service.create(name, type, account)

                        val status = if (succeeded) StatusCode.OK else StatusCode.BadRequest
                        val message =
                            if (succeeded) "You have successfully created new card" else "Something went wrong"

                        val response = Response(status, message, null, request, ResponseType.NoContent)
                        communicator.send(_gson.toJson(response))
                    }

                    "update-command" -> {

                        val content = request.content!!


                        // todo: just store ref on BankClient in account/card table
                        val cards = mutableListOf<Card>()
                        transaction {
                            val accounts = client.bankClient!!.accounts.toList()
                            accounts.forEach { cards.addAll(it.cards) }
                        }

                        val card = cards.find { it.id.value == (content["id"] as Double).toInt() }

                        // client can't change card he doesn't own
                        if (card == null) {
                            val response = Response(
                                StatusCode.NotFound,
                                "There's no such card",
                                null, request,
                                ResponseType.NoContent
                            )
                            communicator.send(_gson.toJson(response))
                            return
                        }

                        val newName = content["newName"] as String
                        val succeeded = service.update(newName, card)

                        val status = if (succeeded) StatusCode.OK else StatusCode.BadRequest
                        val message =
                            if (succeeded) "You have successfully updated the card" else "Something went wrong"

                        val response = Response(status, message, null, request, ResponseType.NoContent)
                        communicator.send(_gson.toJson(response))
                    }

                    "delete-command" -> {

                        val content = request.content!!

                        // todo: just store ref on BankClient in card table
                        val cards = mutableListOf<Card>()
                        transaction {
                            val accounts = client.bankClient!!.accounts.toList()
                            accounts.forEach { cards.addAll(it.cards) }
                        }

                        val card = cards.find { it.id.value == (content["id"] as Double).toInt() }

                        // client can't change card he doesn't own
                        if (card == null) {
                            val response = Response(
                                StatusCode.NotFound,
                                "There's no such card",
                                null,
                                request,
                                ResponseType.NoContent
                            )
                            communicator.send(_gson.toJson(response))
                            return
                        }

                        val succeeded = service.delete(card)

                        val status = if (succeeded) StatusCode.OK else StatusCode.BadRequest
                        val message =
                            if (succeeded) "You have successfully deleted the card" else "Something went wrong"

                        val response = Response(status, message, null, request, ResponseType.NoContent)
                        communicator.send(_gson.toJson(response))
                    }

                    "get-command" -> {
                        val content = request.content!!

                        val card = transaction {
                            service.get((content["id"] as Double).toInt())
                        }

                        if (card == null ||
                            transaction { client.bankClient!!.accounts.find { it.id.value == card.account.value } } == null
                        ) {
                            val response = Response(
                                StatusCode.NotFound,
                                "There's no such card",
                                null,
                                request,
                                ResponseType.NoContent
                            )
                            communicator.send(_gson.toJson(response))
                            return
                        }

                        val responseContent = mutableMapOf<String, Any>()
                        responseContent["card"] = cardToCardSerializable(card)

                        val response = Response(
                            StatusCode.OK,
                            "There you go =)",
                            responseContent,
                            request,
                            ResponseType.SingleCard
                        )

                        val g = _gson.toJson(response)
                        communicator.send(_gson.toJson(response))
                    }

                    // return not all cards but all cards associated with this client
                    "get-all-command" -> {

                        val cards = mutableListOf<Card>()

                        transaction {
                            val accounts = client.bankClient!!.accounts.toList()
                            accounts.forEach { cards.addAll(it.cards) }
                        }


                        val responseContent = mutableMapOf<String, Any>()
                        responseContent["cards"] = cards.map { cardToCardSerializable(it) }

                        val response = Response(
                            StatusCode.OK,
                            "There you go =)",
                            responseContent,
                            request,
                            ResponseType.CardList
                        )
                        communicator.send(_gson.toJson(response))
                    }
                }
            }

        }

    }

}
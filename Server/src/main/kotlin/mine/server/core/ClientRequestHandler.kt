package mine.server.core

import com.google.gson.Gson
import mine.Communicator
import mine.models.*
import mine.requests.Request
import mine.responses.Response
import mine.serializable.BankAccountSerializable
import mine.serializable.CardSerializable
import mine.serializable.TransactionSerializable
import mine.server.core.services.*
import mine.server.entities.*
import mine.statuses.StatusCode
import mine.types.AccountType
import mine.types.CardType
import mine.types.ResponseType
import mine.utils.JsonUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object ClientRequestHandler {

    private val _gson = Gson()


    private fun cardToCardSerializable(card: Card) =
        CardSerializable(
            card.name,
            card.account.value,
            card.type.value,
            card.balance
        )


    private fun bankAccountToSerializable(account: BankAccount): BankAccountSerializable {
        val cardsSerializable = transaction {
            account.cards.toList()
        }.map { cardToCardSerializable(it) }

        return BankAccountSerializable(
            account.id.value,
            account.firstOrder,
            account.secondOrder,
            account.currency,
            account.checkDigit,
            account.department,
            account.type.value,
            JsonUtils.gsonDateTime()!!.toJson(account.expiresAt),
            account.client.value,
            account.balance,
            cardsSerializable
        )
    }

    fun transactionToSerializable(transaction: Transaction): TransactionSerializable? {

        val senderPhone = transaction {
            BankClient.all()
                .find { it.accounts.find { acc -> acc.id.value == transaction.account.value } != null }
        }?.phoneNumber

        val receiverPhone = transaction {
            BankClient.all()
                .find { it.accounts.find { acc -> acc.id.value == transaction.counterAgent.value } != null }
        }?.phoneNumber


        if (senderPhone == null || receiverPhone == null) return null

        return TransactionSerializable(
            senderPhone,
            receiverPhone,
            transaction.amount,
            JsonUtils.gsonDateTime()!!.toJson(transaction.dateTime)
        )

    }


    private fun handleUnauthorized(communicator: Communicator, request: Request) {

        // unauthorized
        val response = Response(
            StatusCode.Unauthorized,
            "You are not logged in!",
            null,
            request,
            ResponseType.NoContent
        )

        communicator.send(_gson.toJson(response))
    }

    fun handleRequest(json: String, client: ConnectedClient, communicator: Communicator) {

        /*todo:
        *  1) prevent code duplication
        *
        */


        val request = _gson.fromJson(json, Request::class.java)

        when (request.service) {

            "crypto-service" -> {

                // todo: say to client what encoding server uses

                val stringPublicKey = client.getRSAPublicKey()
                    .encoded.toString(Charsets.UTF_8)

                val content = mutableMapOf<String, Any>()
                content["bytes"] = client.getRSAPublicKey().encoded

                val response = Response(
                    StatusCode.OK,
                    "Here's your RSA public key",
                    content,
                    request,
                    ResponseType.RSAPublicKey
                )

                communicator.send(_gson.toJson(response))
            }

            "register-service" -> {
                val service = RegisterService()

                when (request.command) {
                    "register-command" -> {

                        val content = request.content!!

                        val login = content["login"] as String
                        val password = content["password"] as String
                        val firstName = content["firstName"] as String
                        val secondName = content["secondName"] as String
                        val lastName = content["lastName"] as String
                        val phoneNumber = content["phoneNumber"] as String

                        request.content = null // hide pass and email

                        val model = RegisterModel(login, password, firstName, secondName, lastName, phoneNumber)
                        val succeeded = service.register(model)

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
                        val login = content["login"] as String
                        val password = content["password"] as String

                        request.content = null // hide pass and email

                        val model = LoginModel(login, password)
                        val bankClient = service.login(model)

                        val status: StatusCode
                        val message: String

                        if (bankClient != null) {
                            client.bankClient = bankClient

                            status = StatusCode.OK
                            message = "You have successfully logged in!"

                        } else {
                            status = StatusCode.BadRequest
                            message = "Wrong login or password"
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
                if (client.bankClient == null)
                    handleUnauthorized(communicator, request)



                when (request.command) {

                    "create-command" -> {
                        val content = request.content!!

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

                        val firstOrder = content["firstOrder"] as String
                        val secondOrder = content["secondOrder"] as String
                        val currency = (content["currency"] as Double).toInt()
                        val checkDigit = (content["checkDigit"] as Double).toInt()
                        val department = content["department"] as String
                        val expiresAt = JsonUtils.gsonDateTime()!!.fromJson(
                            content["expiresAt"].toString(),
                            DateTime::class.java
                        )

                        val model = BankAccountModel(
                            firstOrder,
                            secondOrder,
                            currency,
                            checkDigit,
                            department,
                            type,
                            expiresAt
                        );

                        val succeeded = service.create(model, client.bankClient!!)

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

                        val newDate = JsonUtils.gsonDateTime()!!.fromJson(
                            content["newDate"].toString(),
                            DateTime::class.java
                        )

                        var status: StatusCode
                        var message: String
                        try {
                            service.update(account, newDate)

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

                if (client.bankClient == null)
                    handleUnauthorized(communicator, request)


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

                        val model = CardModel(name, type)
                        val succeeded = service.create(model, account)

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

            "transaction-service" -> {

                if (client.bankClient == null)
                    handleUnauthorized(communicator, request)


                val service = TransactionsService()
                when (request.command) {

                    "create-command" -> {

                        val content = request.content

                        val model = TransactionModel(
                            (content!!["cardId"] as Double).toInt(),
                            content["receiverPhone"] as String,
                            (content["amount"] as Double).toFloat()
                        )

                        val succeeded = service.create(client.bankClient!!, model)

                        val response: Response

                        if (!succeeded)
                            response = Response(
                                StatusCode.BadRequest,
                                "Something went wrong",
                                null,
                                request,
                                ResponseType.NoContent
                            )
                        else response = Response(
                            StatusCode.OK,
                            "Transaction successful",
                            null,
                            request,
                            ResponseType.NoContent
                        )

                        communicator.send(_gson.toJson(response))
                    }

                    "get-all-command" -> {

                        // return all transaction associated with this client
                        transaction {


                            val transactions = mutableListOf<Transaction>()
                            client.bankClient!!.accounts.forEach { acc ->
                                transactions.addAll(Transaction.all()
                                    .filter { it.account == acc.id || it.counterAgent == acc.id })
                            }

                            val transactionSerializable = mutableListOf<TransactionSerializable>()

                            transactions.forEach {
                                val serializable = transactionToSerializable(it)
                                if (serializable != null) transactionSerializable.add(serializable)
                            }

                            val responseContent = mutableMapOf<String, Any>()
                            responseContent["transactions"] = transactionSerializable

                            val response = Response(
                                StatusCode.OK,
                                "There you go =)",
                                responseContent,
                                request,
                                ResponseType.TransactionList
                            )
                            communicator.send(_gson.toJson(response))
                        }

                    }

                }

            }

        }
    }
}
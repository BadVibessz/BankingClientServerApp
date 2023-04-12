package mine.server.core

import com.google.gson.Gson
import mine.Communicator
import mine.requests.Request
import mine.responses.Response
import mine.server.core.services.BankAccountService
import mine.server.core.services.CardService
import mine.server.core.services.LoginService
import mine.server.core.services.RegisterService
import mine.server.entities.Card
import mine.statuses.StatusCode
import mine.types.AccountType
import mine.types.CardType
import org.jetbrains.exposed.sql.transactions.transaction

object ClientHandler {

    private val _gson = Gson()


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

                        val succeeded = service.register(email, password)

                        val status = if (succeeded) StatusCode.OK else StatusCode.BadRequest
                        val message = if (succeeded) "You have successfully registered!" else "Something went wrong"

                        request.content = null // hide pass and email
                        val response = Response(status, message, null, request)

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


                        request.content = null // hide pass and email

                        val response = Response(status, message, null, request)
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
                        request
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
                                request
                            )

                            communicator.send(_gson.toJson(response))
                            return
                        }

                        val succeeded = service.create(name, type, client.bankClient!!)

                        val status = if (succeeded) StatusCode.OK else StatusCode.BadRequest
                        val message = if (succeeded) "You have successfully created account" else "Something went wrong"

                        val response = Response(status, message, null, request)
                        communicator.send(_gson.toJson(response))
                    }

                    "update-command" -> {

                        val content = request.content!!

                        // todo: acc does not exist
                        val account = service.get((content["id"] as Double).toInt())

                        if (account == null) {
                            val response = Response(
                                StatusCode.BadRequest,
                                "No such account", null, request
                            )
                            communicator.send(_gson.toJson(response))

                            return
                        }


                        // todo: test // todo: client can't change account he don't own
                        if (client.bankClient!!.accounts.find { it.id == account.id } == null) {

                            val response = Response(
                                StatusCode.BadRequest,
                                "No such account", null, request
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
                                "You have successfully updated account!", null, request
                            )
                            communicator.send(_gson.toJson(response))

                            return;

                        } catch (e: Exception) {

                            val response = Response(
                                StatusCode.BadRequest,
                                "Something went wrong", null, request
                            )
                            communicator.send(_gson.toJson(response))

                            return;
                        }
                    }

                    "delete-command" -> {

                        val content = request.content!!
                        val account =
                            service.get((content["id"] as Double).toInt()) ?: return // todo: acc does not exist

                        service.delete(account)

                    }

                    "get-command" -> {
                        val content = request.content!!

                        val account = service.get((content["id"] as Double).toInt())

                        // todo: if account is null return badRequest else return this account

                    }

                    // return not all accounts but all accounts associated with this client
                    "get-all-command" -> {

                        val accounts = transaction {
                            client.bankClient!!.accounts.toList()
                        }

                        // todo: return accounts to client
                    }

                }
            }

            "card-service" -> {
                val service = CardService()

                if (client.bankClient == null) return // todo: unauthorized


                when (request.command) {
                    "create-command" -> {

                        val content = request.content!!

                        val name = content["name"] as String
                        val account = transaction {
                            BankAccountService().get((content["accountId"] as Double).toInt())
                        } ?: return

                        // todo: account whit this name does not exist

                        val type = when ((content["type"] as String).lowercase()) {
                            "debit" -> CardType.Debit
                            "credit" -> CardType.Credit

                            else -> null
                        } ?: return // todo: badRequest (unknown type)

                        service.create(name, type, account)
                    }

                    "update-command" -> {

                        val content = request.content!!

                        val card = transaction {
                            service.get((content["id"] as Double).toInt())
                        } ?: return // todo: card does not exist


                        val cards = mutableListOf<Card>()
                        transaction {
                            val accounts = client.bankClient!!.accounts.toList()
                            accounts.forEach { cards.addAll(it.cards) }
                        }

                        if (cards.find { it.id == card.id } == null) return // todo: client can't change card he don't own

                        val newName = content["newName"] as String
                        service.update(newName, card)
                    }

                    "delete-command" -> {

                        val content = request.content!!
                        val card = transaction {
                            service.get((content["id"] as Double).toInt())
                        } ?: return // todo: card does not exist

                        service.delete(card)
                    }

                    "get-command" -> {
                        val content = request.content!!

                        val card = transaction {
                            service.get((content["id"] as Double).toInt())
                        }

                        // todo: if account is null return badRequest else return this account

                    }

                    // return not all cards but all cards associated with this client
                    "get-all-command" -> {
                        transaction {
                            val accounts = client.bankClient!!.accounts.toList()

                            val cards = mutableListOf<Card>()
                            accounts.forEach { cards.addAll(it.cards) }
                        }


                        // todo: return cards to client
                    }
                }
            }

        }

    }

}
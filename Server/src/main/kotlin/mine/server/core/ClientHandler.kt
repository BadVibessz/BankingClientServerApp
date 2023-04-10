package mine.server.core

import com.google.gson.Gson
import mine.requests.Request
import mine.server.core.services.BankAccountService
import mine.server.core.services.CardService
import mine.server.core.services.LoginService
import mine.server.core.services.RegisterService
import mine.server.entities.BankAccount
import mine.types.AccountType
import mine.types.CardType
import org.jetbrains.exposed.sql.transactions.transaction

object ClientHandler {

    val gson = Gson()


    fun parseRequest(json: String, client: ConnectedClient) {

        /*todo:
        *  1) parse request in services
        *  2) service should throw exception if something went wrong
        *  3) handle exception over here and then send errorResponse to client
        * */


        //val request = Json.parseToJsonElement(json)

        val request = gson.fromJson(json, Request::class.java)

        when (request.service) {

            "register-service" -> {
                val service = RegisterService()

                when (request.command) {
                    "register-command" -> {

                        val content = request.content
                        val email = content["email"] as String
                        val password = content["password"] as String

                        service.register(email, password)
                    }
                }


            }

            "login-service" -> {
                val service = LoginService()

                when (request.command) {
                    "login-command" -> {

                        val content = request.content
                        val email = content["email"] as String
                        val password = content["password"] as String

                        val bankClient = service.login(email, password)

                        if (bankClient != null)
                            client.bankClient = bankClient
                        else TODO("badResponse")
                    }

                    "logout-command" -> {
                        TODO()
                    }
                }
            }

            "account-service" -> {
                val service = BankAccountService()

                if (client.bankClient == null) return // todo: unauthorized


                when (request.command) {

                    "create-command" -> {
                        val content = request.content
                        val name = content["name"] as String


                        // todo: do it better (serialize from json to actual AccountType enum)
                        val type = when ((content["type"] as String).lowercase()) {
                            "checking" -> AccountType.Checking
                            "saving" -> AccountType.Saving
                            "credit" -> AccountType.Credit

                            else -> null
                        }

                        if (type == null) return // todo: badRequest (unknown type)

                        service.create(name, type, client.bankClient!!)
                    }

                    "update-command" -> {

                        val content = request.content

                        val account = service.get(content["name"] as String) ?: return // todo: acc does not exist
                        val newName = content["newName"] as String

                        service.update(account, newName)
                    }

                    "delete-command" -> {

                        val content = request.content
                        val account = service.get(content["name"] as String) ?: return // todo: acc does not exist

                        service.delete(account)

                    }

                    "get-command" -> {
                        val content = request.content

                        val account = service.get(content["name"] as String)

                        // todo: if account is null return badRequest else return this account

                    }

                    // return not all accounts but all accounts associated with this client
                    "get-all-command" -> {
                        val accounts = client.bankClient!!.accounts.toList()

                        // todo: return accounts to client
                    }

                }
            }

            "card-service" -> {
                val service = CardService()

                if (client.bankClient == null) return // todo: unauthorized


                when (request.command) {
                    "create-command" -> {

                        val content = request.content

                        val name = content["name"] as String
                        val account = BankAccountService().get(content["accountName"] as String) ?: return

                        // todo: account whit this name does not exist

                        val type = when ((content["type"] as String).lowercase()) {
                            "debit" -> CardType.Debit
                            "credit" -> CardType.Credit

                            else -> null
                        } ?: return // todo: badRequest (unknown type)


                        service.create(name, type, account)
                    }
                }
            }

        }

    }

}
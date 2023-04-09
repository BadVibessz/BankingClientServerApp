package mine.server.core

import com.google.gson.Gson
import mine.requests.Request
import mine.server.core.services.BankAccountService
import mine.server.core.services.LoginService
import mine.server.core.services.RegisterService

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

            "account-service" -> {
                val service = BankAccountService()

                // todo: get client's BankClient

                //service.
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

                    "logout-command" ->
                    {
                        TODO()
                    }
                }
            }

            "register-service" ->
            {
                val service = RegisterService()

                when (request.command)
                {
                    "register-command" ->
                    {

                        val content = request.content
                        val email = content["email"] as String
                        val password = content["password"] as String

                        // todo: unique constraint
                        service.register(email, password)
                    }
                }



            }

        }

    }

}
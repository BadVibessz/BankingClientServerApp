package mine.client.gui.core

import com.google.gson.Gson
import mine.api.AccountAPI
import mine.client.gui.windows.MainWindow
import mine.responses.Response
import mine.serializable.BankAccountSerializable
import mine.serializable.CardSerializable
import mine.statuses.StatusCode
import mine.types.AccountType
import mine.types.ResponseType

object ServerResponseHandler {

    private val _gson = Gson()

    fun handleRespone(json: String, client: Client) {
        val response = _gson.fromJson(json, Response::class.java)

        val status = response.status
        val message = response.message
        val content = response.content
        val request = response.request


        when (status) {
            StatusCode.OK -> {

                when (request.command) {
                    "login-command" -> {
                        client.loginWindow!!.dispose()
                        client.loginWindow = null
                        client.mainWindow = MainWindow(client).apply { isVisible = true }

                        //AccountAPI.create("MyAccount", AccountType.Checking, client.communicator)

                        //AccountAPI.getAll(client.communicator)

                    }

                    "register-command" -> {
                        client.registerWindow!!.showMessageBox(message)
                    }

                    else -> {
                        // todo

                    }
                }

            }

            StatusCode.BadRequest -> {

                when (request.command) {
                    "register-command" -> {
                        client.registerWindow!!.showMessageBox(message)
                    }

                    "login-command" -> {
                        client.loginWindow!!.showMessageBox(message)
                    }
                }

            }

            else -> {
                // todo

            }
        }

        when (response.type) {
            ResponseType.SingleAccount -> {
                val account = _gson.fromJson(
                    content!!["account"].toString(),
                    BankAccountSerializable::class.java
                )

                // todo: ??
            }

            ResponseType.AccountList -> {

                val z = content!!["accounts"].toString().trim()

                val accounts = _gson.fromJson(
                    content!!["accounts"].toString().trim(),
                    Array<BankAccountSerializable>::class.java
                ).toList()

                // todo: update client's account list

                client.mainWindow!!.updateThumbnails(accounts)

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
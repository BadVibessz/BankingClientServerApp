package mine.client

import kotlinx.coroutines.*
import mine.Communicator
import mine.api.LoginAPI
import mine.api.RegisterAPI
import java.net.Socket

class Client(
    host: String,
    port: Int,
) {
    private val _socket: Socket
    private val _communicator: Communicator
    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    init {
        _socket = Socket(host, port)
        _communicator = Communicator(_socket)
    }

    fun start() = _mainCoroutineScope.launch {
        launch {
            _communicator.startReceiving {
                parse(it)
            }
        }

        launch {
            while (isActive) {
                //val input = readlnOrNull() ?: ""

                //if (input == "createacc")
                // TODO: TROUBLES WITH COROUTINES
                //AccountAPI.create("newAcc", AccountType.Checking, _communicator)

                //LoginAPI.login("email@mail.com", "pass", _communicator)
                RegisterAPI.register("email@mail.com", "pass", _communicator)

            }
        }
    }

    private fun parse(data: String) {
        data.split(":", limit = 2).let {
            when (it[0]) {
                "INTR" -> {
                    print("Представьте себя: ")
                }

                "REINTR" -> {
                    print("Имя занято, выберите другое: ")
                }

                "NAMEOK" -> {
                    println("Вы успешно вошли в чат")
                }

                "MSG" -> {
                    println(it[1])
                }

                "NEW" -> {
                    println("Пользователь ${it[1]} вошёл в чат")
                }

                "EXIT" -> {
                    println("Пользователь ${it[1]} покинул чат")
                }
            }
        }
    }
}
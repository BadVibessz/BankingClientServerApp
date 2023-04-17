package mine.client

import kotlinx.coroutines.*
import mine.Communicator
import mine.api.AccountAPI
import mine.api.CardAPI
import mine.api.LoginAPI
import mine.api.RegisterAPI
import mine.types.AccountType
import mine.types.CardType
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
                ServerResponseHandler.handleRespone(it)
            }
        }

        launch {
            while (isActive) {
                //val input = readlnOrNull() ?: ""


//                if (input.contains("register")) {
//                    val spltd = input.split(' ')
//                    RegisterAPI.register(spltd[1], spltd[2], _communicator)
//
//                }
//
//                if (input.contains("login")) {
//                    val spltd = input.split(' ')
//                    LoginAPI.login(spltd[1], spltd[2], _communicator)
//                }

                // todo: implement type enum as constants and move type enum to server app

                LoginAPI.login("test@gmail.com", "test", _communicator)
                //AccountAPI.create("myAccount", AccountType.Checking, _communicator)
                //CardAPI.create("myDebitCard",CardType.Debit,3,_communicator)
                //CardAPI.get(6,_communicator)
                AccountAPI.getAll(_communicator)


                //if (input == "createacc")
                //AccountAPI.create("newAcc", AccountType.Checking, _communicator)

                //RegisterAPI.register("test@gmail.com", "test", _communicator)

                //LoginAPI.login("email@mail.com", "pass", _communicator)
                //AccountAPI.create("newAcc", AccountType.Checking, _communicator)
                //CardAPI.create("myNewDebitCard", CardType.Debit, "newAcc", _communicator)

                //AccountAPI.delete("newAcc", _communicator)

                //CardAPI.update(4,"xdd",_communicator)

                //AccountAPI.delete(1,_communicator)


                //AccountAPI.update("myCheckingAccount", "myAccount", _communicator)


                //RegisterAPI.register("email@mail.com", "pass", _communicator)

            }
        }
    }

}
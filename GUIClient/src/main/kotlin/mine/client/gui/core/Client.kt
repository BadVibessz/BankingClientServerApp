package mine.client.gui.core

import com.google.gson.Gson
import kotlinx.coroutines.*
import mine.Communicator
import mine.client.gui.windows.LoginWindow
import mine.client.gui.windows.MainWindow
import mine.client.gui.windows.RegisterWindow
import mine.cryptography.symmetric.AES
import mine.serializable.BankAccountSerializable
import java.net.Socket

class Client(
    host: String,
    port: Int,
) {
    private val _socket: Socket
    val communicator: Communicator // todo: is it safe?
    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    var registerWindow: RegisterWindow? = null
    var loginWindow: LoginWindow? = null
    var mainWindow: MainWindow? = null

    var bankAccounts = listOf<BankAccountSerializable>()
        get() = field.toList()

    init {
        _socket = Socket(host, port)
        communicator = Communicator(_socket)
        loginWindow = LoginWindow(this).apply { isVisible = true }
    }

    fun start() = _mainCoroutineScope.launch {
        launch {
            communicator.startReceiving {
                ServerResponseHandler.handleRespone(it, this@Client)
            }
        }

        launch {
            if (isActive) {
                mainWindow?.isVisible = true
            }
        }
    }

    fun send(msg: String) {

        // todo: use cipher

        communicator.send(msg)
    }


}
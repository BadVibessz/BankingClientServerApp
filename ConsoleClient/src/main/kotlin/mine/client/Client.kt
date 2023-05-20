package mine.client

import kotlinx.coroutines.*
import mine.AsyncHandler
import mine.Communicator
import mine.api.*
import mine.client.gui.ui.UIType
import mine.ui.ConsoleUI
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousSocketChannel
import kotlin.coroutines.suspendCoroutine

//class Client(
//    host: String,
//    port: Int,
//) {
//    private val _socket: Socket
//    private val _communicator: Communicator
//    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())
//
//    var rsaPublicKey: PublicKey? = null
//    private val _aesSecretKey = AES.generateSecretKey()
//
//    init {
//        _socket = Socket(host, port)
//        _communicator = Communicator(_socket)
//    }
//
//    fun start() = _mainCoroutineScope.launch {
//        launch {
//            _communicator.startReceiving {
//                ServerResponseHandler.handleRespone(it, this@Client)
//            }
//        }
//
//        launch {
//            if (isActive) {
//
//
//                // todo: implement type enum as constants and move type enum to server app
//
//                CryptoAPI.getRSAPublicKey(_communicator)
//
//                LoginAPI.login("test@gmail.com", "test", this@Client::send)
//
//                //AccountAPI.getAll(this@Client::send)
//
//
//            }
//        }
//    }
//
//    private fun send(msg: String) {
//
//        val (encrypted: ByteArray, iv: ByteArray) =
//            AES.encrypt(msg.toByteArray(Charsets.US_ASCII), _aesSecretKey)
//
//        val (a: Int, b: Int, c: Int) = Triple(1, 3, 2)
//
//        val message = Triple(encrypted, iv, _aesSecretKey.encoded)
//
//        val str = Gson().toJson(message)
//
//        val bytes = Gson().fromJson(str, Triple::class.java)
//
//        val decrypted = AES.decrypt(encrypted, _aesSecretKey, iv).toString(Charsets.US_ASCII)
//
//        // todo: send via socket data as byteArray and encrypted aes secret keys
//        _communicator.send(encrypted.toString(Charsets.US_ASCII))
//    }
//}


class Client(
    host: String,
    port: Int,
    uiType: UIType
) {

    private val _socket = AsynchronousSocketChannel.open()
    private val _adress = InetSocketAddress(host, port)
    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private var _communicator: Communicator? = null

    val ui = when (uiType) {

        UIType.Console -> ConsoleUI(this::send)

        UIType.GUI -> null

        else -> null
    }


    fun start() = _mainCoroutineScope.launch {

        if (_socket.isOpen) {

            suspendCoroutine {
                _socket.connect(_adress, it, AsyncHandler())
                _communicator = Communicator(_socket)
            }


            launch {
                _communicator?.startReceiving {
                    ServerResponseHandler.handleRespone(it, this@Client)
                }
            }

            launch {
                if (isActive) {

                    // todo: implement type enum as constants and move type enum to server app

                    //CryptoAPI.getRSAPublicKey(_communicator!!) // ??

                    LoginAPI.login("test@gmail.com", "test", this@Client::send)
                    AccountAPI.getAll(this@Client::send)

                    //AccountAPI.getAll(this@Client::send)

                }
            }
        }
    }

    fun stop() {
        _socket.shutdownInput()
        _socket.shutdownOutput()
        _socket.close()
    }

    suspend fun send(msg: String) {
        // todo: crypto
        _communicator?.send(msg)
    }

}
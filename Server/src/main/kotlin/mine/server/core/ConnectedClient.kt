package mine.server.core

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mine.Communicator
import mine.cryptography.asymmetric.RSA
import mine.server.entities.BankClient
import java.net.Socket
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import kotlin.coroutines.suspendCoroutine

class ConnectedClient(private val socket: AsynchronousSocketChannel) { // todo: private socket???

    private val _communicator = Communicator(socket) // todo: protected/private?
    private val _rsaKeyPair = RSA.generateKeyPair()

    fun getRSAPublicKey() = _rsaKeyPair.public

    var name: String? = null
    var password: String? = null // todo: hash

    var bankClient: BankClient? = null

    init {
        ConnectedClients.list.add(this)

        // todo: check whether or not bankClient already exists
        //bankClient = name?.let { name -> password?.let { pass -> BankClient(name, pass) } }
    }

//    suspend fun start() = coroutineScope {
//        launch {
//            try { // todo: is correct?
//                _communicator.startReceiving { json ->
//                    launch { ClientRequestHandler.handleRequest(json, this@ConnectedClient, _communicator) }
//                }
//            } catch (_: Throwable) {
//                // todo: handle
//            }
//        }
//    }

    suspend fun start() = coroutineScope {
        launch {
            try {
                _communicator.startReceiving { json ->
                    launch { ClientRequestHandler.handleRequest(json, this@ConnectedClient, _communicator) }
                }
            } catch (_: Throwable) {
                // todo: handle
            }
        }
    }
}
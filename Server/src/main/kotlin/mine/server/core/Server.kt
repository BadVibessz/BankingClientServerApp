package mine.server.core

import kotlinx.coroutines.*
import java.net.ServerSocket

class Server(val port: Int = 5004) {
    private val _serverSocket: ServerSocket
    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())


    init {
        _serverSocket = ServerSocket(port);
    }

    fun start() = _mainCoroutineScope.launch {
        while (isActive) {
            try {
                _serverSocket.accept().apply {
                    ConnectedClient(this).apply {
                        launch {
                            try {
                                start()
                            } catch (_: Throwable) {
                                println("Something went wrong")
                            }
                        }
                    }
                    // todo: handle new clients
                }
            } catch (_: Throwable) {
                // todo: handle error
                println("Something went wrong on server")
            }
        }
    }

    fun stop() {
        _mainCoroutineScope.cancel("Server shutted down")
        _serverSocket.close()
    }

}
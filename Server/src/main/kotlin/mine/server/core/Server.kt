package mine.server.core

import kotlinx.coroutines.*
import mine.server.DbContext
import java.net.ServerSocket

class Server(val port: Int = 5004) {
    private val _serverSocket: ServerSocket = ServerSocket(port)
    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val _db = DbContext()


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
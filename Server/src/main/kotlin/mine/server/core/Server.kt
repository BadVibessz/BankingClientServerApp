package mine.server.core

import kotlinx.coroutines.*
import mine.AsyncHandler
import mine.server.DbContext
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.SocketAddress
import java.nio.channels.AsynchronousServerSocketChannel
import kotlin.coroutines.suspendCoroutine

//class Server(val port: Int = 5004) {
//    private val _serverSocket: ServerSocket = ServerSocket(port)
//    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())
//    private val _db = DbContext()
//
//
//    fun start() = _mainCoroutineScope.launch {
//        while (isActive) {
//            try {
//                _serverSocket.accept().apply {
//                    ConnectedClient(this).apply {
//                        launch {
//                            try {
//                                start()
//                            } catch (_: Throwable) {
//                                println("Something went wrong")
//                            }
//                        }
//                    }
//                    // todo: handle new clients
//                }
//            } catch (_: Throwable) {
//                // todo: handle error
//                println("Something went wrong on server")
//            }
//        }
//    }
//
//    fun stop() {
//        _mainCoroutineScope.cancel("Server shutted down")
//        _serverSocket.close()
//    }
//
//}

class Server(port: Int = 5004) {

    private val _ssc: AsynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()
    private val _socketAddress: SocketAddress = InetSocketAddress(port)
    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private val _db = DbContext()


    fun start() = _mainCoroutineScope.launch {
        if (_ssc.isOpen) {
            _ssc.bind(_socketAddress)
            val sc = suspendCoroutine { _ssc.accept(it, AsyncHandler()) }


            ConnectedClient(sc).apply {
                launch {
                    try {
                        start()
                    } catch (e: Throwable) {
                        println(e.message)
                    }
                }
            }


        }
    }

    fun stop() {
        _mainCoroutineScope.cancel("Server shutted down")
        _ssc.close()
    }
}
package mine

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import kotlin.coroutines.suspendCoroutine

class Communicator(val socket: AsynchronousSocketChannel) { // todo: object?

    suspend fun startReceiving(parseCallback: (String) -> Unit) {

        val buf = ByteBuffer.allocate(1024)

        suspendCoroutine {
            socket.read(buf, it, AsyncHandler())
        }

        buf.flip()
        val ba = ByteArray(buf.limit())
        buf.get(ba)

        parseCallback(ba.toString(Charsets.UTF_8))
    }

    suspend fun send(data: String) {

        val buf = ByteBuffer.allocate(1024)
        buf.put(data.toByteArray(Charsets.UTF_8))
        buf.flip()

        suspendCoroutine {
            socket.write(buf, it, AsyncHandler())
        }
    }

}
package mine

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Communicator(val socket: Socket) { // todo: object?

    suspend fun startReceiving(parseRule: (String) -> Unit) {
        val br = BufferedReader(InputStreamReader(socket.getInputStream()))

        coroutineScope {
            while (isActive) {
                val data = br.readLine()
                parseRule(data)
            }
        }
    }

    fun send(data: String) {
        val pw = PrintWriter(socket.getOutputStream())

        pw.println(data)
        pw.flush()
    }

}
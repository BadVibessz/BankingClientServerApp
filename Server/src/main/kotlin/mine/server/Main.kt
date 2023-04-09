package mine.server

import kotlinx.coroutines.runBlocking
import mine.server.core.Server

fun main() = runBlocking {
    Server().start().join()
}
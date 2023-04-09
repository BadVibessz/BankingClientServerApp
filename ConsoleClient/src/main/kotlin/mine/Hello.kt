package mine

import kotlinx.coroutines.runBlocking
import mine.client.Client

fun main() = runBlocking {
    Client("127.0.0.1", 5004).start().join()



}


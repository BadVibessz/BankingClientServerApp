package mine

import kotlinx.coroutines.runBlocking
import mine.client.Client
import mine.client.gui.ui.UIType

fun main() = runBlocking {
    Client("127.0.0.1", 5004, UIType.Console).start().join()



}


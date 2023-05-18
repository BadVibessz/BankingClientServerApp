package mine.client.gui

import kotlinx.coroutines.runBlocking
import mine.client.gui.core.Client
import mine.client.gui.windows.MainWindow

fun main() = runBlocking {
    Client("localhost", 5004).start().join()
    //MainWindow().isVisible = true
}


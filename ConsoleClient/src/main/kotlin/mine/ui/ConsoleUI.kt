package mine.ui

import mine.client.gui.ui.UI
import kotlin.reflect.KSuspendFunction1

class ConsoleUI(sendCallback: KSuspendFunction1<String, Unit>) : UI(sendCallback) {


    override fun requestRegistration() {
        println("Please, register: [email] [password]")
    }

    override fun requestLogin() {
        println("Please, login: [email] [password]")

    }

    override fun showAlert(msg: String) {
        println("ALERT: $msg")
    }

    override fun showMessage(msg: String) {
        println(msg)
    }

    override fun updateUserList(users: List<String>) {
        TODO("Not yet implemented")
    }

    override fun init() {
        TODO("Not yet implemented")
    }


}
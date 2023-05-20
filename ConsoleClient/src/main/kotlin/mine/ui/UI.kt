package mine.client.gui.ui

import kotlin.reflect.KSuspendFunction1


abstract class UI(private val sendCallback: KSuspendFunction1<String, Unit>) {

    abstract fun requestRegistration()
    abstract fun requestLogin()
    abstract fun showAlert(msg: String)
    abstract fun showMessage(msg: String)
    abstract fun updateUserList(users: List<String>)
    abstract fun init()

    suspend fun handleClientMessage(message: String) = sendCallback(message)

}
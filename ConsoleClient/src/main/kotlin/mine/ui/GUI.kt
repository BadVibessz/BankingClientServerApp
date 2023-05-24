package mine.ui

import mine.client.windows.MainWindow
import mine.client.windows.LoginWindow
import mine.client.windows.RegisterWindow
import mine.serializable.BankAccountSerializable
import mine.serializable.CardSerializable
import mine.serializable.TransactionSerializable

class GUI(sendCallback: (String) -> Unit) : UI(sendCallback) {


    var mainWindow: MainWindow? = null
    var loginWindow: LoginWindow? = null
    var registerWindow: RegisterWindow? = null

    override fun onSuccessfulLogin() {
        loginWindow?.dispose()
        loginWindow = null

        mainWindow = MainWindow(this)
    }

    override fun requestRegistration() {
        loginWindow?.dispose()
        loginWindow = null

        registerWindow = RegisterWindow(this)
    }

    override fun requestLogin() {
        registerWindow?.dispose()
        registerWindow = null

        loginWindow = LoginWindow(this)
    }

    override fun showAlert(msg: String) {
        registerWindow?.showMessageBox(msg)
        loginWindow?.showMessageBox(msg)
    }

    override fun showMessage(msg: String) {
        // todo:
        println(msg)
    }

    override fun showAccount(account: BankAccountSerializable) {
        TODO("Not yet implemented")
    }

    override fun showCard(card: CardSerializable) {
        TODO("Not yet implemented")
    }

    override fun updateAccountsList(accounts: List<BankAccountSerializable>) {
        mainWindow?.updateAccountThumbnails(accounts)
    }

    override fun updateCardsList(cards: List<CardSerializable>) {
        mainWindow?.updateCardThumbnails(cards)
    }

    override fun updateTransactionsList(transactions: List<TransactionSerializable>) {
        mainWindow?.updateTransactionThumbnails(transactions)
    }
}
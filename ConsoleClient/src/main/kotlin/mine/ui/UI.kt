package mine.ui

import mine.api.*
import mine.models.*
import mine.serializable.BankAccountSerializable
import mine.serializable.BankClientSerializable
import mine.serializable.CardSerializable
import mine.serializable.TransactionSerializable
import org.joda.time.DateTime


abstract class UI(private val sendCallback: (String) -> Unit) {

    fun handleClientMessage(message: String) = sendCallback(message)

    // send
    fun register(model: RegisterModel) = RegisterAPI.register(model, sendCallback)
    fun login(model: LoginModel) = LoginAPI.login(model.login, model.password, sendCallback)

    fun createAccount(model: BankAccountModel) = AccountAPI.create(model, sendCallback)
    fun updateAccount(id: Int, newDate: DateTime) = AccountAPI.update(id, newDate, sendCallback)
    fun getAccount(id: Int) = AccountAPI.get(id, sendCallback)
    fun getAccountId(number: String) = AccountAPI.get(number, sendCallback)
    fun getAllAccounts() = AccountAPI.getAll(sendCallback)
    fun deleteAccount(id: Int) = AccountAPI.delete(id, sendCallback)

    fun createCard(model: CardModel, accountId: Int) = CardAPI.create(model, accountId, sendCallback)
    fun updateCard(id: Int, newName: String) = CardAPI.update(id, newName, sendCallback)
    fun getCard(id: Int) = CardAPI.get(id, sendCallback)
    fun getAllCards() = CardAPI.getAll(sendCallback)
    fun deleteCard(id: Int) = CardAPI.delete(id, sendCallback)

    fun createTransaction(model: TransactionModel) = TransactionAPI.create(model, sendCallback)
    fun getAllTransactions() = TransactionAPI.getAll(sendCallback)

    fun getClientInfo() = BankClientAPI.get(sendCallback)


    abstract fun onSuccessfulLogin()

    // show
    abstract fun requestRegistration()
    abstract fun requestLogin()
    abstract fun showAlert(msg: String)
    abstract fun showMessage(msg: String)

    abstract fun updateAccountCallback(account: BankAccountSerializable, callback: (BankAccountSerializable) -> Unit)

    abstract fun showAccount(account: BankAccountSerializable)
    abstract fun showCard(card: CardSerializable)

    abstract fun updateAccountsList(accounts: List<BankAccountSerializable>)
    abstract fun updateCardsList(cards: List<CardSerializable>)
    abstract fun updateTransactionsList(transactions: List<TransactionSerializable>)

    abstract fun updateProfileInfo(bankClient: BankClientSerializable)


}
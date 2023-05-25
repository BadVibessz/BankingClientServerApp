package mine.ui

import com.google.gson.Gson
import mine.serializable.BankAccountSerializable
import mine.serializable.CardSerializable
import mine.serializable.TransactionSerializable
import java.lang.StringBuilder
import kotlin.reflect.KSuspendFunction1

class ConsoleUI(sendCallback: (String) -> Unit) : UI(sendCallback) {


    private fun getIdWithLeadingZeros(id: Int): String {

        val leadingZeros = 7 - id.toString().length

        if (leadingZeros <= 0) return id.toString()

        val builder = StringBuilder()

        repeat(leadingZeros, { builder.append('0') })
        builder.append(id)

        return builder.toString()
    }

    private fun formCardString(card: CardSerializable): String {
        val builder = StringBuilder()
        builder.append("Name: ${card.name}\n")
        builder.append("Type: ${card.typeId}\n")
        builder.append("Balance: ${card.balance}\n")

        return builder.toString()
    }

    private fun formAccountString(account: BankAccountSerializable): String {
        val builder = StringBuilder()

        builder.append(
            "Account number: ${
                account.firstOrder + account.secondOrder +
                        account.currency + account.checkDigit +
                        account.department + getIdWithLeadingZeros(account.id)
            } \n"
        )

        builder.append("Cards:\n[\n")
        account.cards.forEachIndexed { ind, it ->

            val deilm = when (ind) {

                account.cards.size - 1 -> ""
                else -> "\n"
            }

            builder.append(formCardString(it) + deilm)
        }
        builder.append("]\n")

        builder.append("Balance: ${account.balance}\n")
        builder.append("Expires at: ${account.expiresAt}\n")

        return builder.toString()
    }

    override fun requestRegistration() {
        println("Please, register: [login] [password] [firstName] [secondName] [lastName?] [phoneNumber: +7...]")
    }

    override fun requestLogin() {
        println("Please, login: [login] [password]")

    }

    override fun showAlert(msg: String) {
        println("ALERT: $msg")
    }

    override fun showMessage(msg: String) {
        println(msg)
    }

    override fun updateAccountCallback(account: BankAccountSerializable, callback: (BankAccountSerializable) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun showAccount(account: BankAccountSerializable) {

        val accountString = formAccountString(account)

        println("--------------------")
        println(accountString)
        println("--------------------")
    }

    override fun showCard(card: CardSerializable) {
        println(formCardString(card))
    }

    override fun updateAccountsList(accounts: List<BankAccountSerializable>) {
        println("Your accounts:\n")
        accounts.forEach { showAccount(it) }
    }

    override fun updateCardsList(cards: List<CardSerializable>) {
        println("Your cards:\n")
        cards.forEach { showCard(it) }
    }

    override fun updateTransactionsList(transactions: List<TransactionSerializable>) {
        TODO("Not yet implemented")
    }


    override fun onSuccessfulLogin() {
        // todo:
    }


}
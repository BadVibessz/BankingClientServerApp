package mine.server.models.accounts

import kotlinx.coroutines.DisposableHandle
import mine.server.models.BankClient
import mine.server.models.cards.Card
import mine.types.AccountType


// счет может иметь несколько карт
abstract class BankAccount(val name: String, val bankClient: BankClient, val type: AccountType) : DisposableHandle {

    // todo: id

    // name instead of id while id not implemented

    var cards: MutableList<Card> = mutableListOf() // todo: protected?

    protected var balance: Double = 0.0

    init {

    }

    @JvmName("getAccountBalance")
    fun getBalance() = balance

    abstract fun deposit(amount: Double)
    abstract fun withdraw(amount: Double)

    abstract fun createCard(name: String): Card
    fun deleteCard(card: Card) = cards.remove(card)

    override fun dispose() {

        cards.forEach { it.dispose() }
        cards.clear()
    }


}
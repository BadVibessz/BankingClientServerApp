package mine.server.models.cards

import kotlinx.coroutines.DisposableHandle
import mine.server.models.accounts.BankAccount
import mine.types.CardType


abstract class Card(var name: String, protected val account: BankAccount, val type: CardType) : DisposableHandle {

    var balance: Double = 0.0
        protected set;

    abstract fun deposit(amount: Double);
    abstract fun withdraw(amount: Double);

    override fun dispose() {
        balance = 0.0
    }

}
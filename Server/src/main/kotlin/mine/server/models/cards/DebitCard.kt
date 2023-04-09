package mine.server.models.cards

import mine.server.models.accounts.BankAccount
import java.lang.Exception
import mine.types.CardType

class DebitCard(name: String, account: BankAccount) : Card(name, account, CardType.Debit) {


    override fun deposit(amount: Double) {
        balance += amount
        account.deposit(amount)
    }

    override fun withdraw(amount: Double) {

        if (amount <= balance) {
            balance -= amount
            account.withdraw(amount)
        };
        else throw Exception("No such amount of money");
    }


}
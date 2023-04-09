package mine.server.models.accounts

import mine.server.models.BankClient
import mine.server.models.cards.Card
import mine.server.models.cards.DebitCard
import mine.types.AccountType


class CheckingAccount(name: String, bankClient: BankClient) : BankAccount(name, bankClient, AccountType.Checking) {


    override fun deposit(amount: Double) {
        balance += amount
    }


    override fun withdraw(amount: Double) {
        if (amount <= balance)
            balance -= amount
    }

    override fun createCard(name: String): Card {
        val card = DebitCard(name, this)
        cards.add(card)

        return card
    }


}
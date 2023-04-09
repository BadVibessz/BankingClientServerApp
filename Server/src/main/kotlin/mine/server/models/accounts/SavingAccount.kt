package mine.server

import mine.server.models.BankClient
import mine.server.models.accounts.BankAccount
import mine.server.models.cards.Card
import mine.types.AccountType


class SavingAccount(name: String, bankClient: BankClient) : BankAccount(name, bankClient, AccountType.Saving) {

    // todo: придумать процентную ставку и т.п
    override fun deposit(amount: Double) {
        TODO("Not yet implemented")
    }

    override fun withdraw(amount: Double) {
        TODO("Not yet implemented")
    }

    override fun createCard(name: String): Card {
        TODO("Not yet implemented")
    }


}
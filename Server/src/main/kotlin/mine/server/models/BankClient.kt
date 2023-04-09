package mine.server.models

import mine.server.SavingAccount
import mine.server.models.accounts.BankAccount
import mine.server.models.accounts.CheckingAccount
import mine.server.models.cards.Card
import mine.server.models.cards.DebitCard
import mine.types.AccountType
import mine.types.CardType
import java.lang.Exception

class BankClient(var name: String, private var password: String) {

    // todo: id (db?)

    val accounts = mutableListOf<BankAccount>()
    val cards = mutableListOf<Card>()


    fun getTotalBalance() = cards.sumOf { it.balance }

    fun createAccount(name: String, type: AccountType): BankAccount // todo: optimize
    {

        val account: BankAccount = when (type) {
            AccountType.Checking -> CheckingAccount(name, this)
            AccountType.Saving -> SavingAccount(name, this)

            // todo: handle others

            else -> throw Exception("Unknown type of an account")
        }
        accounts.add(account)
        return account
    }

    fun createCard(name: String, type: CardType, accountName: String): Card {

        val account = accounts.find { it.name == accountName }
            ?: throw Exception("No such account")

        val card: Card = when (type) {
            CardType.Debit -> DebitCard(name, account)

            else -> throw Exception("Unknown type of a card")

        }

        val createdCard = account.createCard(name)

        if (createdCard.type != type) {
            account.deleteCard(createdCard) // todo: slow, optimize
            throw Exception("Not valid type of card for given account")
        }

        cards.add(card)
        return card
    }

    fun deleteAccount(account: BankAccount) {
        account.dispose()
        accounts.remove(account)
    }

    fun deleteCard(card: Card) =
        accounts.find { it.cards.contains(card) }?.deleteCard(card) ?: throw Exception("Card does not exist")

}
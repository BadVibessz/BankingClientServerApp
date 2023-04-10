package mine.server.core.services

import mine.server.entities.BankAccount
import mine.server.entities.BankAccounts
import mine.server.entities.Card
import mine.types.CardType
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class CardService {

    // todo: get, getall, create, update, delete

    fun create(name: String, type: CardType, account: BankAccount) {

        var success = true
        val defaultBalance = 1000f

        transaction {

            if (Card.all().find { it.name == name } != null
                || BankAccount.all().find { it.id == account.id } == null) {
                success = false
                close()
            }

            Card.new {
                this.name = name
                this.type = type
                this.account = account.id
                this.balance = defaultBalance // todo: ??
            }

            // todo: is it correct?
            val newBalance = account.balance + defaultBalance
            BankAccounts.update({ BankAccounts.name eq account.name }) {
                it[balance] = newBalance
            }
        }

        // todo: badRequest if success == false, Ok else

    }

}
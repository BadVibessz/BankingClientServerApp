package mine.server.core.services

import mine.models.CardModel
import mine.server.entities.*
import mine.types.AccountType
import mine.types.CardType
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.lang.Exception

class CardService {

    private fun getType(type: CardType) = when (type) {

        CardType.Debit ->
            transaction { mine.server.entities.CardType.all().find { it.name.lowercase() == "debit" } }

        CardType.Credit ->
            transaction { mine.server.entities.CardType.all().find { it.name.lowercase() == "credit" } }

        else -> null
    }

    fun create(model: CardModel, account: BankAccount): Boolean {

        var success = true
        val defaultBalance = 1000f

        try {
            transaction {

                if (BankAccount.all().find { it.id == account.id } == null) {
                    success = false
                    close()
                }

                val typeColumn = getType(model.type)
                if(typeColumn == null){
                    success = false
                    close()
                }

                Card.new {
                    this.name = model.name
                    this.type = typeColumn!!.id
                    this.account = account.id
                    this.balance = defaultBalance
                }

                // todo: is it correct?
                val newBalance = account.balance + defaultBalance
                BankAccounts.update({ BankAccounts.id eq account.id }) {
                    it[balance] = newBalance
                }
            }
        } catch (_: Exception) {
            success = false
        }
        return success
    }

    fun update(newName: String, card: Card): Boolean {

        var success = true

        try {
            transaction {
                Cards.update {
                    card.name = newName
                }
            }
        } catch (_: Exception) {
            success = false
        }
        return success

    }

    fun get(id: Int) = transaction { Card.all().find { it.id.value == id } }
    fun getAll() = transaction { Card.all().toList() }

    fun delete(card: Card): Boolean {

        var success = true
        val cardBalance = card.balance

        try {
            transaction {
                // todo: handle better
                val account = BankAccountService().get(card.account.value) ?: throw Exception("wtf?")

                card.delete()

                val newBalance = account.balance - cardBalance
                BankAccounts.update({ BankAccounts.id eq account.id }) {
                    it[balance] = newBalance
                }
            }
        } catch (_: Exception) {
            success = false
        }

        return success
    }


}
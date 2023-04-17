package mine.server.core.services

import mine.server.entities.BankAccount
import mine.server.entities.BankAccounts
import mine.server.entities.Card
import mine.types.CardType
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.lang.Exception

class CardService {

    // todo: get, getall, create, update, delete

    fun create(name: String, type: CardType, account: BankAccount): Boolean {

        var success = true
        val defaultBalance = 1000f

        try {
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
        } catch (_: Exception) {
            success = false
        }
        return success
    }

    fun update(newName: String, card: Card): Boolean {

        var success = true

        try {
            transaction {
                BankAccounts.update {
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
                BankAccounts.update({ BankAccounts.name eq account.name }) {
                    it[balance] = newBalance
                }
            }
        } catch (_: Exception) {
            success = false
        }

        return success
    }


}
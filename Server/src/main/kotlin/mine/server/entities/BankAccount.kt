package mine.server.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption


object BankAccounts : IntIdTable() {

    // number
    val firstOrder = varchar("firstOrder", 3)
    val secondOrder = varchar("secondOrder", 2)
    val currency = integer("currency")
    val checkDigit = integer("checkDigit")
    val department = varchar("department", 4)

    val type = reference("type", BankAccountTypes, onDelete = ReferenceOption.SET_NULL)
    val expiresAt = datetime("expiresAt")
    val balance = float("balance")

    val client = reference("client", BankClients, onDelete = ReferenceOption.CASCADE)
}

// счет может иметь несколько карт
class BankAccount(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BankAccount>(BankAccounts)

    var firstOrder by BankAccounts.firstOrder
    var secondOrder by BankAccounts.secondOrder
    var currency by BankAccounts.currency
    var checkDigit by BankAccounts.checkDigit
    var department by BankAccounts.department
    var type by BankAccounts.type
    var client by BankAccounts.client
    var expiresAt by BankAccounts.expiresAt
    var balance by BankAccounts.balance

    val cards by Card referrersOn Cards.account
}


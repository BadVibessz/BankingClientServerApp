package mine.server.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption


object Transactions : IntIdTable() {

    val dateTime = datetime("dateTime")
    val account = reference("account", BankAccounts, onDelete = ReferenceOption.CASCADE)
    val counterAgent = reference("counterAgent", BankAccounts, onDelete = ReferenceOption.CASCADE)
    val amount = float("amount")
}

class Transaction(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Transaction>(Transactions)

    var dateTime by Transactions.dateTime
    var account by Transactions.account
    var counterAgent by Transactions.counterAgent
    var amount by Transactions.amount
}
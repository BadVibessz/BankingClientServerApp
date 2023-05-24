package mine.server.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Cards : IntIdTable() {

    val name = varchar("name", 128)
    val account = reference("account", BankAccounts, onDelete = ReferenceOption.CASCADE)
    val type = reference("type",CardTypes, onDelete = ReferenceOption.SET_NULL)
    val balance = float("balance")
}

class Card(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Card>(Cards)

    var name by Cards.name
    var account by Cards.account
    var type by Cards.type
    var balance by Cards.balance
}


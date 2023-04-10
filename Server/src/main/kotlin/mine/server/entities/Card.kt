package mine.server.entities

import mine.types.CardType
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption


object Cards : IntIdTable() {

    val name = varchar("name", 256)
    val account = reference("account", BankAccounts, onDelete = ReferenceOption.CASCADE)
    val type = enumeration("type", CardType::class)
    val balance = float("balance")

}

class Card(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Card>(Cards)

    var name by Cards.name
    var account by Cards.account
    var type by Cards.type
    var balance by Cards.balance
}
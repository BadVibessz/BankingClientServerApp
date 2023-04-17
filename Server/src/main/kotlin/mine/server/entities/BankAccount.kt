package mine.server.entities

import mine.server.entities.BankClients
import mine.types.AccountType
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.io.Serializable


object BankAccounts : IntIdTable() {

    val name = varchar("name", 256)
    val client = reference("client", BankClients, onDelete = ReferenceOption.CASCADE)
    val type = enumeration("type", AccountType::class)
    val balance = float("balance")
}

// счет может иметь несколько карт
class BankAccount(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BankAccount>(BankAccounts)

    var name by BankAccounts.name
    var client by BankAccounts.client
    var type by BankAccounts.type
    var balance by BankAccounts.balance
    val cards by Card referrersOn Cards.account

}


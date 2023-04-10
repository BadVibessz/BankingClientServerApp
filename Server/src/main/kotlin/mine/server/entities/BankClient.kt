package mine.server.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable


object BankClients : IntIdTable() {

    val email = varchar("email", 256).index() // todo: wdm index??
    val password = varchar("password", 256) // todo: maybe longer?
}

class BankClient(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<BankClient>(BankClients)

    var email by BankClients.email
    var password by BankClients.password
    val accounts by BankAccount referrersOn BankAccounts.client

}
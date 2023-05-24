package mine.server.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable


object BankClients : IntIdTable() {

    val login = varchar("login", 128)
    val password = varchar("password", 256)
    val firstName = varchar("firstName", 256) // имя
    val secondName = varchar("secondName", 256).nullable() // отчество
    val lastName = varchar("lastName", 256) // фамилия
    val phoneNumber = varchar("phoneNumber", 12) // +7....

}

class BankClient(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<BankClient>(BankClients)

    var login by BankClients.login
    var password by BankClients.password
    var firstName by BankClients.firstName
    var secondName by BankClients.secondName
    var lastName by BankClients.lastName
    var phoneNumber by BankClients.phoneNumber
    val accounts by BankAccount referrersOn BankAccounts.client

}
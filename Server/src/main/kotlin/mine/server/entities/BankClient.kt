package mine.server.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable


object BankClients : IntIdTable() {

    val email = varchar("email", 256).index() // todo: wdm index??
    val password = varchar("password", 256) // todo: maybe longer?

    /* нужна для предотвращения компроментации пользователей-"двойников",
    т.е если взломана бд и обнаружен h(password, salt),
    злоумышленник не может обнаружить аккаунты с таким же хешем пароля,
    т.к h(pass, salt1) != h(pass, salt2)
    https://auth0.com/blog/adding-salt-to-hashing-a-better-way-to-store-passwords/
    */
    val salt = varchar("salt", 256)

}

class BankClient(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<BankClient>(BankClients)

    var email by BankClients.email
    var password by BankClients.password
    val accounts by BankAccount referrersOn BankAccounts.client
    var salt by BankClients.salt
}
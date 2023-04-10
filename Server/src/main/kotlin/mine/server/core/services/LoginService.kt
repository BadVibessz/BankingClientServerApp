package mine.server.core.services

import mine.server.entities.BankClient
import org.jetbrains.exposed.sql.transactions.transaction

class LoginService {

    fun login(email: String, password: String): BankClient? =
        transaction { BankClient.all().find { it.email == email && it.password == password } }
}
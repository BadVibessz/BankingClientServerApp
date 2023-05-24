package mine.server.core.services

import mine.models.LoginModel
import mine.server.entities.BankClient
import org.jetbrains.exposed.sql.transactions.transaction

class LoginService {

    fun login(model: LoginModel): BankClient? =
        transaction { BankClient.all().find { it.login == model.login && it.password == model.password } }
}
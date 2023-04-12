package mine.server.core.services

import mine.server.entities.BankClient
import org.jetbrains.exposed.sql.transactions.transaction

class RegisterService {

    fun register(email: String, password: String): Boolean {

        // todo: return response
        var success = true

        transaction {

            if (BankClient.all().find { client -> client.email == email } != null) {
                success = false
                close()
            }

            BankClient.new {
                this.email = email
                this.password = password
            }

        }

        return success
    }
}
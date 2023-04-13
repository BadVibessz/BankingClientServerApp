package mine.server.core.services

import mine.server.entities.BankClient
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigInteger
import java.util.*

class RegisterService {

    fun generateSalt(size: Int): String
    {
        // todo: stronger?
        val bytes = ByteArray(size)
        Random().nextBytes(bytes)
        return BigInteger(1, bytes).toString(16).padStart(32, '0')
    }
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
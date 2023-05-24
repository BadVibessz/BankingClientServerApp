package mine.server

import mine.server.entities.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception

class DbContext {

    private var _connection: Database? = null

    init {
        _connection = getConnection()
        _connection?.let { establishConnection(it) }

    }

    private fun establishConnection(connection: Database) =
        transaction(connection) {

            // creates table if it doesn't exist yet
            SchemaUtils.create(BankClients, BankAccountTypes, BankAccounts, CardTypes, Cards, Transactions)
        }


    private fun getConnection(): Database? {

        var connection: Database? = null
        try {
            connection = Database.connect(
                url = "jdbc:postgresql://localhost:5432/BankingApp",
                driver = "org.postgresql.Driver",
                user = "postgres",
                password = "gunna"
            )
        } catch (e: Exception) {
            println(e.message)
        }

        return connection
    }

}
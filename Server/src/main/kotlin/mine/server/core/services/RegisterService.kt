package mine.server.core.services

import mine.models.RegisterModel
import mine.server.entities.BankClient
import mine.server.entities.BankClients
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class RegisterService {

    fun register(model: RegisterModel): Boolean {

        // todo: return response
        var success = true

        transaction {


            if (BankClient.all().find { client ->
                    client.login == model.login
                            || client.phoneNumber == model.phoneNumber
                } != null) {
                success = false
                return@transaction
            }

//            BankClients.insert {
//                it[login] = model.login
//                it[password] = model.password
//                it[firstName] = model.firstName
//                it[secondName] = model.secondName
//                it[lastName] = model.lastName
//                it[phoneNumber] = model.phoneNumber
//            }

            BankClient.new {
                this.login = model.login
                this.password = model.password
                this.firstName = model.firstName
                this.secondName = model.secondName
                this.lastName = model.lastName
                this.phoneNumber = model.phoneNumber
            }
        }
        return success
    }
}
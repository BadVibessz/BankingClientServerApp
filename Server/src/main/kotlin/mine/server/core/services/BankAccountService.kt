package mine.server.core.services

import mine.server.entities.BankAccount
import mine.server.entities.BankAccounts
import mine.server.entities.BankClient
import mine.types.AccountType
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class BankAccountService { // todo: implement as Scoped https://metanit.com/sharp/aspnet5/6.2.php

    // todo: get, getall, create, update, delete

    fun create(name: String, type: AccountType, client: BankClient): Boolean {

        var success = true
        transaction {

            if (BankAccount.all().find { it.name == name } != null) {
                success = false
                close()
            } // todo: badRequest


            BankAccount.new {
                this.name = name
                this.type = type
                this.client = client.id
                this.balance = 0F // todo: ??
            }
        }

        return success

        // todo: badRequest if success == false, Ok else

    }

    fun get(id: Int) = transaction { BankAccount.all().find { it.id.value == id} }
    fun getAll() = transaction { BankAccount.all().toList() }

    fun update(account: BankAccount, newName: String) = transaction {
        BankAccounts.update {
            account.name = newName
        }
    }

    fun delete(account: BankAccount) = transaction {
        account.delete()
    }


}
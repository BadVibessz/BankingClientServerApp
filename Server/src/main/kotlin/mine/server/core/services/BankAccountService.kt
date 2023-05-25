package mine.server.core.services

import mine.models.BankAccountModel
import mine.server.entities.BankAccount
import mine.server.entities.BankAccountType
import mine.server.entities.BankAccounts
import mine.server.entities.BankClient
import mine.types.AccountType
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.lang.StringBuilder

class BankAccountService { // todo: implement as Scoped https://metanit.com/sharp/aspnet5/6.2.php

    // todo: get, getall, create, update, delete

    private fun getIdWithLeadingZeros(id: Int): String {

        val leadingZeros = 7 - id.toString().length

        if (leadingZeros <= 0) return id.toString()

        val builder = StringBuilder()

        repeat(leadingZeros, { builder.append('0') })
        builder.append(id)

        return builder.toString()
    }

    private fun formAccountNumber(account: BankAccount) =
        account.firstOrder + account.secondOrder +
                account.currency + account.checkDigit +
                account.department + getIdWithLeadingZeros(account.id.value)


    private fun getType(type: AccountType) = when (type) {

        AccountType.Saving ->
            transaction { BankAccountType.all().find { it.name.lowercase() == "saving" } }

        AccountType.Checking ->
            transaction { BankAccountType.all().find { it.name.lowercase() == "checking" } }

        AccountType.Credit ->
            transaction { BankAccountType.all().find { it.name.lowercase() == "credit" } }

        else -> null
    }

    fun create(model: BankAccountModel, client: BankClient): Boolean {

        var success = true
        try {
            transaction {

//                if (BankAccount.all().find { it.name == name } != null) {
//                    success = false
//                    close()
//                } // todo: badRequest


                val typeColumn = getType(model.type)
                if (typeColumn == null) {
                    success = false
                    close()
                }

                BankAccount.new {
                    this.type = typeColumn!!.id
                    this.firstOrder = model.firstOrder
                    this.secondOrder = model.secondOrder
                    this.currency = model.currency
                    this.checkDigit = model.checkDigit
                    this.department = model.department
                    this.expiresAt = model.expiresAt // todo: ??
                    this.client = client.id
                    this.balance = 0F // todo: ??
                }
            }
        } catch (e: Exception) {
            success = false
        }

        return success

        // todo: badRequest if success == false, Ok else

    }

    fun get(id: Int) = transaction { BankAccount.all().find { it.id.value == id } }
    fun get(number: String) = transaction { BankAccount.all().find { formAccountNumber(it) == number } }


    fun getAll() = transaction { BankAccount.all().toList() }

    fun update(account: BankAccount, newDate: DateTime) = transaction {
        BankAccounts.update {
            account.expiresAt = newDate
        }
    }

    fun delete(account: BankAccount) = transaction {
        account.delete()
    }


}
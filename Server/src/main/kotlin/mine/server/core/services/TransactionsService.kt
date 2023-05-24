package mine.server.core.services

import mine.models.TransactionModel
import mine.server.entities.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class TransactionsService {


    fun create(bankClient: BankClient, model: TransactionModel): Boolean {

        var success = true

        try {
            transaction {
                val sender = BankClient.all().find { client -> client.phoneNumber == bankClient.phoneNumber }
                val receiver = BankClient.all().find { client -> client.phoneNumber == model.receiverPhone }

                if (sender == null || receiver == null) {
                    success = false
                    return@transaction
                }

                val firstReceiverCard = receiver
                    .accounts.find { acc -> acc.cards.count() != 0 }
                    ?.cards?.first()

                if (firstReceiverCard == null) {
                    success = false
                    return@transaction
                }

                val senderCard = Card.all().find { it.id.value == model.cardId }
                if (senderCard == null) {
                    success = false
                    return@transaction
                }

                if (senderCard.balance < model.amount) {
                    success = false
                    return@transaction
                }


                // make money transfer

                // card
                var newSenderBalance = senderCard.balance - model.amount
                Cards.update({ Cards.id eq senderCard.id }) {
                    it[balance] = newSenderBalance
                }

                val senderAccount = BankAccountService().get(senderCard.account.value)!!

                // account
                newSenderBalance = senderAccount.balance - model.amount
                BankAccounts.update({ BankAccounts.id eq senderAccount.id }) {
                    it[balance] = newSenderBalance
                }

                // card
                val newReceiverBalance = firstReceiverCard.balance + model.amount
                Cards.update({ Cards.id eq firstReceiverCard.id }) {
                    it[balance] = newReceiverBalance
                }

                val receiverAccount = BankAccountService().get(firstReceiverCard.account.value)!!

                // account
                newSenderBalance = receiverAccount.balance + model.amount
                BankAccounts.update({ BankAccounts.id eq receiverAccount.id }) {
                    it[balance] = newSenderBalance
                }

                // save transaction in history
                Transaction.new {
                    this.account = senderCard.account
                    this.counterAgent = firstReceiverCard.account
                    this.amount = model.amount
                    this.dateTime = DateTime.now()
                }
            }


        } catch (e: Throwable) {
            success = false
        }
        return success
    }

}
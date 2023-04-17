package mine.serializable

import mine.types.AccountType
import java.io.Serializable

data class BankAccountSerializable(
    val name: String,
    val clientId: Int,
    val type: AccountType,
    val balance: Float,
    val cards: List<CardSerializable>
) : Serializable


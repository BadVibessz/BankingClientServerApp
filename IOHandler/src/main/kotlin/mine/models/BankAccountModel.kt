package mine.models

import mine.types.AccountType
import org.joda.time.DateTime

data class BankAccountModel(
    val firstOrder: String,
    val secondOrder: String,
    val currency: Int,
    val checkDigit: Int,
    val department: String,

    val type: AccountType,
    val expiresAt: DateTime,
)
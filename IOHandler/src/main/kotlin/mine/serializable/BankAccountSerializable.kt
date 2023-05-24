package mine.serializable

import org.joda.time.DateTime
import java.io.Serializable

data class BankAccountSerializable(
    val id: Int,
    val firstOrder: String,
    val secondOrder: String,
    val currency: Int,
    val checkDigit: Int,
    val department: String,
    val typeId: Int,
    val expiresAt: String, // todo: storing as json
    val clientId: Int,
    val balance: Float,
    val cards: List<CardSerializable>
) : Serializable


package mine.serializable

import mine.types.CardType
import java.io.Serializable

data class CardSerializable(
    val id: Int,
    val name: String,
    val accountId: Int,
    val typeId: Int,
    val balance: Float
) : Serializable

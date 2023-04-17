package mine.serializable

import mine.types.CardType
import java.io.Serializable

data class CardSerializable(
    val name: String,
    val accountId: Int,
    val type: CardType,
    val balance: Float
) : Serializable

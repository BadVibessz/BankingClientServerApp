package mine.serializable

data class TransactionSerializable(
    val senderPhone: String,
    val receiverPhone: String,
    val amount: Float,
    val dateTime: String
)

package mine.serializable

data class BankClientSerializable(
    val id: Int,
    val login: String,
    val firstName: String,
    val secondName: String?,
    val lastName: String,
    val phoneNumber: String,
)

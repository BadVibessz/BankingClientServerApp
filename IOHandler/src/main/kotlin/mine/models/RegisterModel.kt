package mine.models

data class RegisterModel(
    val login: String,
    var password: String,
    val firstName: String,
    val secondName: String,
    val lastName: String,
    val phoneNumber: String,
)

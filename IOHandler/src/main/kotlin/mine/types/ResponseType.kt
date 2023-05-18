package mine.types

enum class ResponseType {
    RSAPublicKey,
    SingleCard,
    CardList,
    SingleAccount,
    AccountList,
    NoContent, // ничего не возвращает
}
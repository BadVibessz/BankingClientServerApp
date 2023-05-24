package mine.types

enum class ResponseType {
    RSAPublicKey,
    SingleCard,
    CardList,
    SingleAccount,
    AccountList,
    TransactionList,
    NoContent, // ничего не возвращает
}
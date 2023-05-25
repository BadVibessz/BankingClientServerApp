package mine.types

enum class ResponseType {
    RSAPublicKey,
    SingleCard,
    CardList,
    SingleAccount,
    AccountId,
    AccountList,
    TransactionList,
    NoContent, // ничего не возвращает
}
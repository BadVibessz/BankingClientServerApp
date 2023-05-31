package mine.types

enum class ResponseType {
    RSAPublicKey,
    SingleCard,
    CardList,
    SingleAccount,
    AccountId,
    AccountList,
    TransactionList,
    BankClientInfo,
    NoContent, // ничего не возвращает
}
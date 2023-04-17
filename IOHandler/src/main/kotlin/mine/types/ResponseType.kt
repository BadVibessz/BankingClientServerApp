package mine.types

enum class ResponseType {
    SingleCard, // одна карта
    CardList, // список карт
    SingleAccount, // один счет
    AccountList, // список счетов
    NoContent, // ничего не возвращает
}
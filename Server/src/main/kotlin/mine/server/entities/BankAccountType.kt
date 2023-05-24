package mine.server.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable


object BankAccountTypes : IntIdTable() {

    val name = varchar("name", 256)
    val description = varchar("description", 1000)

}

class BankAccountType(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<BankAccountType>(BankAccountTypes)

    val name by BankAccountTypes.name
    val description by BankAccountTypes.description

}
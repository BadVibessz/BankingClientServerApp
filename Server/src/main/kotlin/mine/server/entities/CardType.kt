package mine.server.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable


object CardTypes : IntIdTable() {

    val name = varchar("name", 256)
    val description = varchar("description", 1000)
}


class CardType(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<CardType>(CardTypes)

    val name by CardTypes.name
    val description by CardTypes.description
}
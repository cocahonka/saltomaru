package com.cocahonka.saltomaru.database.tables

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import com.cocahonka.saltomaru.database.entities.Locate

/**
 * Представление таблицы базы данных для [Locate]
 *
 * Не допускает хранение одинаковых значений [Locate]
 * @property id уникальное значение ссылающееся на [CauldronsTable] (One-to-One)
 */
object LocatesTable : IdTable<Int>("locate") {
    override val id = reference("id", CauldronsTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()

    val worldId = integer("world_id")
    val x = integer("x")
    val y = integer("y")
    val z = integer("z")

    init {
        index(false, worldId, x, y, z)
        uniqueIndex(worldId, x, y, z)
    }
}
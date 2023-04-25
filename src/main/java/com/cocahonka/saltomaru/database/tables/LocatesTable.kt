package com.cocahonka.saltomaru.database.tables

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import com.cocahonka.saltomaru.database.entities.Locate
import com.cocahonka.saltomaru.database.base.Mappable
import org.jetbrains.exposed.sql.ResultRow

/**
 * Представление таблицы базы данных для [Locate]
 *
 * Не допускает хранение одинаковых значений [Locate]
 *
 * При удалении связанные записи удаляются согласно [ReferenceOption.CASCADE]
 * @property id уникальное значение ссылающееся на [CauldronsTable] (One-to-One)
 */
object LocatesTable : IdTable<Int>("locate"), Mappable<Locate> {
    override val id = reference("id", CauldronsTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()

    val worldId = integer("world_id")
    val x = integer("x")
    val y = integer("y")
    val z = integer("z")

    init {
        index(false, worldId, x, y, z)
        uniqueIndex(worldId, x, y, z)
    }

    override fun fromRow(resultRow: ResultRow): Locate {
        check(resultRow.hasValue(worldId)) { "row must have worldId value" }
        check(resultRow.hasValue(x)) { "row must have x value" }
        check(resultRow.hasValue(y)) { "row must have y value" }
        check(resultRow.hasValue(z)) { "row must have z value" }
        return Locate(
            worldId = resultRow[worldId],
            x = resultRow[x],
            y = resultRow[y],
            z = resultRow[z],
        )
    }

}
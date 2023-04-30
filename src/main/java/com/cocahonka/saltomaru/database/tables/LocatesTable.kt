package com.cocahonka.saltomaru.database.tables

import com.cocahonka.saltomaru.database.base.TableConditions
import org.jetbrains.exposed.dao.id.IdTable
import com.cocahonka.saltomaru.database.entities.Locate
import com.cocahonka.saltomaru.database.base.TableMappable
import com.cocahonka.saltomaru.database.base.UUIDBinaryColumnType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

/**
 * Представление таблицы базы данных для [Locate]
 *
 * Не допускает хранение одинаковых значений [Locate]
 *
 * При удалении связанные записи удаляются согласно [ReferenceOption.CASCADE]
 * @property id уникальное значение ссылающееся на [CauldronsTable] (One-to-One)
 */
object LocatesTable :
    IdTable<Int>("locate"),
    TableMappable<Locate>,
    TableConditions<Locate> {
    override val id = reference("id", CauldronsTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()

    val worldUUID = registerColumn<UUID>("world_uuid", UUIDBinaryColumnType)
    val x = integer("x")
    val y = integer("y")
    val z = integer("z")

    init {
        uniqueIndex(worldUUID, x, y, z)
    }

    override fun fromRow(resultRow: ResultRow): Locate {
        check(resultRow.hasValue(worldUUID)) { "row must have worldUUID value" }
        check(resultRow.hasValue(x)) { "row must have x value" }
        check(resultRow.hasValue(y)) { "row must have y value" }
        check(resultRow.hasValue(z)) { "row must have z value" }
        return Locate(
            worldUUID = resultRow[worldUUID],
            x = resultRow[x],
            y = resultRow[y],
            z = resultRow[z],
        )
    }

    override fun selectUniqueCondition(entity: Locate): Op<Boolean> =
        (worldUUID eq entity.worldUUID) and
                (x eq entity.x) and
                (y eq entity.y) and
                (z eq entity.z)

}

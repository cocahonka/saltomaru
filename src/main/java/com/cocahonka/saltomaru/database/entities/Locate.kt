package com.cocahonka.saltomaru.database.entities

import com.cocahonka.saltomaru.database.base.EntityMappable
import com.cocahonka.saltomaru.database.tables.LocatesTable
import org.bukkit.Location
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.*

/**
 * Дата класс содержащий информацию о местоположении блока
 * @param worldUUID uuid мира
 * @param x координата по оси x
 * @param y координата по оси y
 * @param z координата по оси z
 * @throws IllegalArgumentException если значения не входят в диапазон допустимых (см. [Config])
 */
data class Locate(
    val worldUUID: UUID,
    val x: Int,
    val y: Int,
    val z: Int,
) : EntityMappable<Int> {

    /**
     * Создает объект [Locate] из указанной [Location].
     * @param location объект [Location] для преобразования
     * @return объект [Locate], созданный на основе указанной [location]
     */
    constructor(location: Location) : this(
        worldUUID = location.world.uid,
        x = location.blockX,
        y = location.blockY,
        z = location.blockZ
    )

    override fun toInsertStatement(statement: InsertStatement<Number>, id: EntityID<Int>): InsertStatement<Number> =
        statement.also {
            it[LocatesTable.worldUUID] = worldUUID
            it[LocatesTable.x] = x
            it[LocatesTable.y] = y
            it[LocatesTable.z] = z
            it[LocatesTable.id] = id
        }

    override fun toBatchInsertStatement(statement: BatchInsertStatement, id: EntityID<Int>): BatchInsertStatement =
        statement.also {
            it[LocatesTable.worldUUID] = worldUUID
            it[LocatesTable.x] = x
            it[LocatesTable.y] = y
            it[LocatesTable.z] = z
            it[LocatesTable.id] = id
        }

    override fun toUpdateStatement(statement: UpdateStatement, id: EntityID<Int>): UpdateStatement =
        statement.also {
            it[LocatesTable.worldUUID] = worldUUID
            it[LocatesTable.x] = x
            it[LocatesTable.y] = y
            it[LocatesTable.z] = z
            it[LocatesTable.id] = id
        }
}



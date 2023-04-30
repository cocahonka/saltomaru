package com.cocahonka.saltomaru.database.entities

import com.cocahonka.saltomaru.database.base.EntityMappable
import com.cocahonka.saltomaru.database.tables.LocatesTable
import org.bukkit.Location
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import com.cocahonka.saltomaru.config.SaltomaruConfig.Minecraft as Config
import com.cocahonka.saltomaru.config.SaltomaruConfig.Minecraft.WorldsIds
import com.cocahonka.saltomaru.exceptions.UnknownEnvironmentException

/**
 * Дата класс содержащий информацию о местоположении блока
 * @param worldId id мира (см. [Config])
 * @param x координата по оси x
 * @param y координата по оси y
 * @param z координата по оси z
 * @throws IllegalArgumentException если значения не входят в диапазон допустимых (см. [Config])
 */
data class Locate(
    val worldId: Int,
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
        worldId = Config.getWorldIdFromEnvironment(location.world.environment),
        x = location.blockX,
        y = location.blockY,
        z = location.blockZ
    )

    init {
        require(WorldsIds.values().any { it.id == worldId })
        { "worldId must be in ${WorldsIds.values()}" }

        require(x in Config.MIN_X..Config.MAX_X)
        { "x must be between ${Config.MIN_X}..${Config.MAX_X}" }

        require(z in Config.MIN_Z..Config.MAX_X)
        { "z must be between ${Config.MIN_Z}..${Config.MAX_Z}" }

        when (worldId) {
            WorldsIds.OVERWORLD_ID.id -> require(y in Config.MIN_OVERWORLD_Y..Config.MAX_OVERWORLD_Y)
            { "y in overworld must be between ${Config.MIN_OVERWORLD_Y}..${Config.MAX_OVERWORLD_Y}" }

            WorldsIds.NETHER_ID.id -> require(y in Config.MIN_NETHER_Y..Config.MAX_NETHER_Y)
            { "y in nether must be between ${Config.MIN_NETHER_Y}..${Config.MAX_NETHER_Y}" }

            WorldsIds.END_ID.id -> require(y in Config.MIN_END_Y..Config.MAX_END_Y)
            { "y in end must be between ${Config.MIN_END_Y}..${Config.MAX_END_Y}" }
        }
    }

    override fun toInsertStatement(statement: InsertStatement<Number>, id: EntityID<Int>): InsertStatement<Number> =
        statement.also {
            it[LocatesTable.worldId] = worldId
            it[LocatesTable.x] = x
            it[LocatesTable.y] = y
            it[LocatesTable.z] = z
            it[LocatesTable.id] = id
        }

    override fun toBatchInsertStatement(statement: BatchInsertStatement, id: EntityID<Int>): BatchInsertStatement =
        statement.also {
            it[LocatesTable.worldId] = worldId
            it[LocatesTable.x] = x
            it[LocatesTable.y] = y
            it[LocatesTable.z] = z
            it[LocatesTable.id] = id
        }

    override fun toUpdateStatement(statement: UpdateStatement, id: EntityID<Int>): UpdateStatement =
        statement.also {
            it[LocatesTable.worldId] = worldId
            it[LocatesTable.x] = x
            it[LocatesTable.y] = y
            it[LocatesTable.z] = z
            it[LocatesTable.id] = id
        }
}



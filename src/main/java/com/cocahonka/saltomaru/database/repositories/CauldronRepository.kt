package com.cocahonka.saltomaru.database.repositories

import com.cocahonka.saltomaru.config.SaltomaruConfig
import com.cocahonka.saltomaru.database.entities.Locate
import com.cocahonka.saltomaru.database.tables.CauldronsTable
import com.cocahonka.saltomaru.database.tables.LocatesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class CauldronRepository {
    fun synchronizeCachedWithDatabase(
        deletedLocations: HashSet<Locate>,
        createdLocations: HashSet<Locate>,
    ) {
        delete(deletedLocations)
        insert(createdLocations)
    }

    private fun delete(deletedLocations: HashSet<Locate>) {
        val chunked = deletedLocations.chunked(SaltomaruConfig.Database.BATCHED_CHUNK_SIZE)
        for (chunk in chunked) {
            transaction {
                for (deleted in chunk) {
                    val locationId = LocatesTable.select {
                        (LocatesTable.worldId eq deleted.worldId) and
                                (LocatesTable.x eq deleted.x) and
                                (LocatesTable.y eq deleted.y) and
                                (LocatesTable.z eq deleted.z)
                    }.singleOrNull()?.get(LocatesTable.id)
                    if (locationId != null) {
                        CauldronsTable.deleteWhere { id eq locationId }
                    }
                }
            }
        }
    }

    private fun insert(createdLocations: HashSet<Locate>) {
        val chunked = createdLocations.chunked(SaltomaruConfig.Database.BATCHED_CHUNK_SIZE)
        for (chunk in chunked) {
            transaction {
                val existingLocations = LocatesTable.select {
                    val conditions = chunk.map { locate ->
                        (LocatesTable.worldId eq locate.worldId) and
                                (LocatesTable.x eq locate.x) and
                                (LocatesTable.y eq locate.y) and
                                (LocatesTable.z eq locate.z)
                    }

                    conditions.reduce { acc, condition -> acc or condition }
                }.associateBy {
                    Locate(
                        it[LocatesTable.worldId],
                        it[LocatesTable.x],
                        it[LocatesTable.y],
                        it[LocatesTable.z]
                    )
                }


                val newLocations = chunk.filter { it !in existingLocations }

                val newCauldronIds = CauldronsTable.batchInsert(newLocations) { }.map { it[CauldronsTable.id] }

                LocatesTable.batchInsert(newLocations.indices) { index ->
                    this[LocatesTable.id] = newCauldronIds[index]
                    this[LocatesTable.worldId] = newLocations[index].worldId
                    this[LocatesTable.x] = newLocations[index].x
                    this[LocatesTable.y] = newLocations[index].y
                    this[LocatesTable.z] = newLocations[index].z
                }
            }
        }
    }
}
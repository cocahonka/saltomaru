package com.cocahonka.saltomaru.database.repositories

import com.cocahonka.saltomaru.config.SaltomaruConfig.Database as Config
import com.cocahonka.saltomaru.database.entities.Locate
import com.cocahonka.saltomaru.database.tables.CauldronsTable
import com.cocahonka.saltomaru.database.tables.LocatesTable
import com.cocahonka.saltomaru.database.utils.TableUtils.getCombinedEitherCondition
import com.cocahonka.saltomaru.database.utils.TableUtils.getUniqueExistingEntitiesFromPrompt
import com.cocahonka.saltomaru.database.utils.TableUtils.safeGetIds
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
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
        val chunked = deletedLocations.chunked(Config.BATCHED_CHUNK_SIZE)

        for (chunk in chunked) {
            transaction {

                val existingLocationsIds = LocatesTable.safeGetIds {
                    val conditions = chunk.map(::selectUniqueCondition)
                    select(conditions.getCombinedEitherCondition()).toList()
                }

                CauldronsTable.deleteWhere { id inList existingLocationsIds }

            }
        }
    }

    private fun insert(createdLocations: HashSet<Locate>) {
        val chunked = createdLocations.chunked(Config.BATCHED_CHUNK_SIZE)

        for (chunk in chunked) {
            transaction {

                val existingLocations = LocatesTable.getUniqueExistingEntitiesFromPrompt(chunk)

                val newLocations = chunk.filter { it !in existingLocations }

                val newCauldronIds = CauldronsTable.safeGetIds { batchInsert(newLocations) { } }

                LocatesTable.batchInsert(newLocations.indices) { index ->
                    val location = newLocations[index]
                    val id = newCauldronIds[index]
                    location.toBatchInsertStatement(this, id)
                }

            }
        }
    }
}
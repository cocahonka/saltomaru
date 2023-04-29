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

/**
 * Репозиторий для работы с Котлами
 *
 * Отвечает за синхронизацию кэшированных данных с базой данных, включая удаление и добавление записей.
 */
class CauldronRepository {

    /**
     * Синхронизирует кэшированные данные с базой данных.
     *
     * @param deletedLocations множество [Locate] для удаления из базы данных.
     * @param createdLocations множество [Locate] для добавления в базу данных.
     */
    fun synchronizeCachedWithDatabase(
        deletedLocations: HashSet<Locate>,
        createdLocations: HashSet<Locate>,
    ) {
        delete(deletedLocations)
        insert(createdLocations)
    }

    /**
     * Удаляет переданные [deletedLocations] из базы данных, при условии что такие записи есть.
     *
     * @param deletedLocations множество [Locate] для удаления.
     */
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

    /**
     * Вставляет переданные [createdLocations] в базу данных, при условии что таких записей нету
     *
     * @param createdLocations множество [Locate] для добавления.
     */
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
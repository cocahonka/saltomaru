package com.cocahonka.saltomaru.database

import com.cocahonka.saltomaru.config.SaltomaruConfig
import com.cocahonka.saltomaru.database.entities.Locate
import com.cocahonka.saltomaru.database.repositories.CauldronRepository
import com.cocahonka.saltomaru.database.tables.CauldronsTable
import com.cocahonka.saltomaru.database.tables.LocatesTable
import com.cocahonka.saltomaru.database.utils.TableUtils.safeMap
import com.cocahonka.saltomaru.database.utils.TableUtils.safeMapSingle
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThrows
import kotlin.test.assertEquals


class SaltomaruDatabaseTest {

    @BeforeEach
    fun setUp() {
        Database.connect("jdbc:h2:mem:test${SaltomaruConfig.Database.PARAMETERS}", driver = "org.h2.Driver")
    }

    @Test
    fun `one to one relationship`() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(LocatesTable, CauldronsTable)

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            LocatesTable.insert {
                it[x] = 0
                it[y] = 0
                it[z] = 0
                it[worldId] = 0
                it[id] = cauldronId
            }

            expectThrows<ExposedSQLException> {
                LocatesTable.insert {
                    it[x] = 1
                    it[y] = 1
                    it[z] = 1
                    it[worldId] = 1
                    it[id] = cauldronId
                }
            }
        }
    }

    @Test
    fun `unique set of values`() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(LocatesTable, CauldronsTable)

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            val cauldronId2 = CauldronsTable.insert { } get CauldronsTable.id
            val cauldronId3 = CauldronsTable.insert { } get CauldronsTable.id
            val cauldronId4 = CauldronsTable.insert { } get CauldronsTable.id

            LocatesTable.insert {
                it[x] = 0
                it[y] = 0
                it[z] = 0
                it[worldId] = 0
                it[id] = cauldronId
            }

            LocatesTable.insert {
                it[x] = 0
                it[y] = 0
                it[z] = 0
                it[worldId] = 1
                it[id] = cauldronId2
            }

            LocatesTable.insert {
                it[x] = 1
                it[y] = 0
                it[z] = 0
                it[worldId] = 1
                it[id] = cauldronId3
            }

            assertEquals(LocatesTable.selectAll().count(), 3)
            expectThrows<ExposedSQLException> {
                LocatesTable.insert {
                    it[x] = 0
                    it[y] = 0
                    it[z] = 0
                    it[worldId] = 0
                    it[id] = cauldronId4
                }
            }
        }
    }

    @Test
    fun `cascade delete linked location`() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(LocatesTable, CauldronsTable)

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id

            LocatesTable.insert {
                it[x] = 0
                it[y] = 0
                it[z] = 0
                it[worldId] = 0
                it[id] = cauldronId
            }

            CauldronsTable.deleteWhere { CauldronsTable.id eq cauldronId }

            assertEquals(LocatesTable.selectAll().count(), 0)
        }
    }

    @Test
    fun `locate mapper`() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(LocatesTable, CauldronsTable)

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            val locate = Locate(1, 2, 3, 4)

            LocatesTable.insert {
                it[x] = locate.x
                it[y] = locate.y
                it[z] = locate.z
                it[worldId] = locate.worldId
                it[id] = cauldronId
            }

            val locateFromRow = LocatesTable.safeMapSingle {
                select { id eq cauldronId }.first()
            }

            assertEquals(locateFromRow, locate)

        }
    }

    @Test
    fun `locate toInsertStatement`() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(LocatesTable, CauldronsTable)

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            val locate = Locate(1, 2, 3, 4)

            LocatesTable.insert { locate.toInsertStatement(it, cauldronId) }

            val locateFromRow = LocatesTable.safeMapSingle {
                select {id eq cauldronId }.first()
            }

            assertEquals(locateFromRow, locate)
        }
    }

    @Test
    fun `locate toUpdateStatement`() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(LocatesTable, CauldronsTable)

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            val locate = Locate(1, 2, 3, 4)
            val locateUpdated = Locate(2, 3, 4, 5)

            LocatesTable.insert { locate.toInsertStatement(it, cauldronId) }

            LocatesTable.update { locateUpdated.toUpdateStatement(it, cauldronId) }

            val locatesFromRow = LocatesTable.safeMap {
                selectAll()
            }

            assertEquals(locatesFromRow.size, 1)
            assertEquals(locatesFromRow.first(), locateUpdated)
        }
    }

    @Test
    fun `insert method work properly`(){
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(LocatesTable, CauldronsTable)

            val locations = List(10) {
                Locate(0, it, it + 1, it + 2)
            }

            val newCauldronIds = CauldronsTable.batchInsert(locations) { }.map { it[CauldronsTable.id] }

            LocatesTable.batchInsert(locations.indices) { index ->
                this[LocatesTable.id] = newCauldronIds[index]
                this[LocatesTable.worldId] = locations[index].worldId
                this[LocatesTable.x] = locations[index].x
                this[LocatesTable.y] = locations[index].y
                this[LocatesTable.z] = locations[index].z
            }

            assertEquals(locations, LocatesTable.safeMap { selectAll() })

            val externalLocations = List(20) {
                Locate(0, it, it + 1, it + 2)
            }

            val repo = CauldronRepository()

            repo.synchronizeCachedWithDatabase(hashSetOf(), externalLocations.toHashSet())

            assertEquals(externalLocations, LocatesTable.safeMap { selectAll() })
        }
    }
}
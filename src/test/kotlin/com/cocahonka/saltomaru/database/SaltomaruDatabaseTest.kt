package com.cocahonka.saltomaru.database

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
import java.util.*
import kotlin.collections.LinkedHashSet
import kotlin.test.assertEquals


class SaltomaruDatabaseTest {

    @BeforeEach
    fun setUp() {
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
    }

    @Test
    fun `one to one relationship`() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(LocatesTable, CauldronsTable)

            val uuid1 = UUID.randomUUID()
            val uuid2 = UUID.randomUUID()

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            LocatesTable.insert {
                it[x] = 0
                it[y] = 0
                it[z] = 0
                it[worldUUID] = uuid1
                it[id] = cauldronId
            }

            expectThrows<ExposedSQLException> {
                LocatesTable.insert {
                    it[x] = 1
                    it[y] = 1
                    it[z] = 1
                    it[worldUUID] = uuid2
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

            val uuid1 = UUID.randomUUID()
            val uuid2 = UUID.randomUUID()

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            val cauldronId2 = CauldronsTable.insert { } get CauldronsTable.id
            val cauldronId3 = CauldronsTable.insert { } get CauldronsTable.id
            val cauldronId4 = CauldronsTable.insert { } get CauldronsTable.id

            LocatesTable.insert {
                it[x] = 0
                it[y] = 0
                it[z] = 0
                it[worldUUID] = uuid1
                it[id] = cauldronId
            }

            LocatesTable.insert {
                it[x] = 0
                it[y] = 0
                it[z] = 0
                it[worldUUID] = uuid2
                it[id] = cauldronId2
            }

            LocatesTable.insert {
                it[x] = 1
                it[y] = 0
                it[z] = 0
                it[worldUUID] = uuid2
                it[id] = cauldronId3
            }

            assertEquals(LocatesTable.selectAll().count(), 3)
            expectThrows<ExposedSQLException> {
                LocatesTable.insert {
                    it[x] = 0
                    it[y] = 0
                    it[z] = 0
                    it[worldUUID] = uuid1
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

            val uuid = UUID.randomUUID()

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id

            LocatesTable.insert {
                it[x] = 0
                it[y] = 0
                it[z] = 0
                it[worldUUID] = uuid
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
            val uuid = UUID.randomUUID()

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            val locate = Locate(uuid, 2, 3, 4)

            LocatesTable.insert {
                it[x] = locate.x
                it[y] = locate.y
                it[z] = locate.z
                it[worldUUID] = locate.worldUUID
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
            val uuid = UUID.randomUUID()

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            val locate = Locate(uuid, 2, 3, 4)

            LocatesTable.insert { locate.toInsertStatement(it, cauldronId) }

            val locateFromRow = LocatesTable.safeMapSingle {
                select { id eq cauldronId }.first()
            }

            assertEquals(locateFromRow, locate)
        }
    }

    @Test
    fun `locate toUpdateStatement`() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(LocatesTable, CauldronsTable)

            val uuid1 = UUID.randomUUID()
            val uuid2 = UUID.randomUUID()

            val cauldronId = CauldronsTable.insert { } get CauldronsTable.id
            val locate = Locate(uuid1, 2, 3, 4)
            val locateUpdated = Locate(uuid2, 3, 4, 5)

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
    fun `insert method work properly`() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(LocatesTable, CauldronsTable)

            val uuid = UUID.randomUUID()

            val locations = List(10) {
                Locate(uuid, it, it + 1, it + 2)
            }

            val newCauldronIds = CauldronsTable.batchInsert(locations) { }.map { it[CauldronsTable.id] }

            LocatesTable.batchInsert(locations.indices) { index ->
                this[LocatesTable.id] = newCauldronIds[index]
                this[LocatesTable.worldUUID] = locations[index].worldUUID
                this[LocatesTable.x] = locations[index].x
                this[LocatesTable.y] = locations[index].y
                this[LocatesTable.z] = locations[index].z
            }

            assertEquals(locations, LocatesTable.safeMap { selectAll() })

            val externalLocations = List(20) {
                Locate(uuid, it, it + 1, it + 2)
            }

            val repo = CauldronRepository()

            repo.synchronizeCachedWithDatabase(hashSetOf(), externalLocations.toHashSet())

            val allDataFromDB = LocatesTable.safeMap { selectAll() }
            assertEquals(externalLocations.size, allDataFromDB.size)
            assertEquals(LinkedHashSet(externalLocations), LinkedHashSet(allDataFromDB))
        }
    }

    @Test
    fun `delete method work properly`() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(LocatesTable, CauldronsTable)

            val uuid = UUID.randomUUID()

            val locations = List(10) {
                Locate(uuid, it, it + 1, it + 2)
            }

            val newCauldrons = CauldronsTable.batchInsert(locations) { }
            val newCauldronIds = newCauldrons.map { it[CauldronsTable.id] }

            LocatesTable.batchInsert(locations.indices) { index ->
                this[LocatesTable.id] = newCauldronIds[index]
                this[LocatesTable.worldUUID] = locations[index].worldUUID
                this[LocatesTable.x] = locations[index].x
                this[LocatesTable.y] = locations[index].y
                this[LocatesTable.z] = locations[index].z
            }

            val deleteLocations = List(5) {
                Locate(uuid, it, it + 1, it + 2)
            }
            val extraDeleteLocations = List(10) {
                Locate(
                    uuid,
                    it + locations.size,
                    it + 1 + locations.size,
                    it + locations.size + 2
                )
            }

            val repo = CauldronRepository()

            repo.synchronizeCachedWithDatabase((deleteLocations + extraDeleteLocations).toHashSet(), hashSetOf())

            println(newCauldrons.subList(5, newCauldrons.size))

            assertEquals(locations.subList(5, locations.size), LocatesTable.safeMap { selectAll() })
            assertEquals(5, CauldronsTable.selectAll().count())
        }
    }
}
package com.cocahonka.saltomaru.database

import com.cocahonka.saltomaru.database.entities.Locate
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
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
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
}
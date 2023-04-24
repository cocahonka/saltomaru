package com.cocahonka.saltomaru.database

import com.cocahonka.saltomaru.config.SaltomaruConfig
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class SaltomaruDatabaseTest {
    private lateinit var connection: Connection

    @BeforeEach
    fun setUp() {
        val dataFolder = File("src/test/out")
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }
        val dbPath = File(dataFolder, "test.db").absolutePath
        val url = SaltomaruConfig.Database.PRE_URL + dbPath
        Database.connect(
            url = url,
            driver = SaltomaruConfig.Database.DRIVER,
            user = SaltomaruConfig.Database.USER,
            password = SaltomaruConfig.Database.PASSWORD
        )
        connection = DriverManager.getConnection(url)

        resetDB()

    }

    private fun resetDB() {
        transaction {
            SchemaUtils.drop(Items)
            SchemaUtils.createMissingTablesAndColumns(Items)
        }
    }

    @AfterEach
    fun tearDown() {
        connection.close()
    }

    @Test
    fun `check transaction correct`() {
        val item = Item(200, "Some name")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Items)
            Items.insert {
                it[id] = 200
                it[name] = "Some name"
            }
            val readItems = Items.selectAll().map {
                val id = it[Items.id]
                val name = it[Items.name]
                Item(id, name)
            }
            expectThat(readItems).isEqualTo(listOf(item))
        }

    }
}

object Items : Table() {
    val id: Column<Int> = integer("id")
    val name: Column<String> = varchar("name", 100)
}

data class Item(
    val id: Int,
    val name: String,
)
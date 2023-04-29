package com.cocahonka.saltomaru.database

import com.cocahonka.saltomaru.Saltomaru
import com.cocahonka.saltomaru.base.patterns.SingletonHolder
import com.cocahonka.saltomaru.config.SaltomaruConfig
import com.cocahonka.saltomaru.database.tables.CauldronsTable
import com.cocahonka.saltomaru.database.tables.LocatesTable
import org.bukkit.Bukkit.getLogger
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.sql.DriverManager

/**
 * Класс для доступа к базе данных SQLite.
 * @param plugin экземпляр класса Saltomaru, к которому привязывается база данных
 * @property connection текущее соединение с базой данных
 */
class SaltomaruDatabase private constructor(plugin: Saltomaru) {
    private val connection: Connection

    init {
        val dbPath = plugin.getStoragePath(SaltomaruConfig.Database.FILE_NAME).absolutePath
        val url = SaltomaruConfig.Database.PRE_URL + dbPath
        Database.connect(
            url = url,
            driver = SaltomaruConfig.Database.DRIVER,
            user = SaltomaruConfig.Database.USER,
            password = SaltomaruConfig.Database.PASSWORD
        )
        connection = DriverManager.getConnection(url)

        transaction {
            SchemaUtils.createMissingTablesAndColumns(LocatesTable, CauldronsTable)
        }
    }

    companion object : SingletonHolder<SaltomaruDatabase, Saltomaru>(::SaltomaruDatabase)

    /**
     * Закрывает соединение с базой данных
     */
    fun close() {
        getLogger().info("Closing database connection ")
        connection.close()
    }

}

package com.cocahonka.saltomaru.database

import com.cocahonka.saltomaru.Saltomaru
import com.cocahonka.saltomaru.base.patterns.SingletonHolder
import com.cocahonka.saltomaru.config.SaltomaruConfig.Database as Config
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
        val dbPath = plugin.getStoragePath(Config.FILE_NAME).absolutePath
        val url = Config.PRE_URL + dbPath + Config.PARAMETERS
        Database.connect(
            url = url,
            driver = Config.DRIVER,
            user = Config.USER,
            password = Config.PASSWORD
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

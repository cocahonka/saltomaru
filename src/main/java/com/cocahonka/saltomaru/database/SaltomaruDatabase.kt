package com.cocahonka.saltomaru.database

import com.cocahonka.saltomaru.Saltomaru
import com.cocahonka.saltomaru.base.patterns.SingletonHolder
import com.cocahonka.saltomaru.config.SaltomaruConfig
import org.jetbrains.exposed.sql.Database
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
        val dbPath = plugin.getStoragePath(SaltomaruConfig.SaltomaruDatabaseConfig.FILE_NAME).absolutePath
        val url = SaltomaruConfig.SaltomaruDatabaseConfig.PRE_URL + dbPath
        Database.connect(
            url = url,
            driver = SaltomaruConfig.SaltomaruDatabaseConfig.DRIVER,
            user = SaltomaruConfig.SaltomaruDatabaseConfig.USER,
            password = SaltomaruConfig.SaltomaruDatabaseConfig.PASSWORD
        )
        connection = DriverManager.getConnection(url)
    }

    companion object : SingletonHolder<SaltomaruDatabase, Saltomaru>(::SaltomaruDatabase)

    /**
     * Закрывает соединение с базой данных
     */
    fun close() {
        connection.close()
    }

}

package com.cocahonka.saltomaru.database

import com.cocahonka.saltomaru.Saltomaru
import com.cocahonka.saltomaru.base.patterns.SingletonHolder
import com.cocahonka.saltomaru.config.Config
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
        val dbPath = plugin.getStoragePath(Config.Database.FILE_NAME).absolutePath
        val url = Config.Database.PRE_URL + dbPath
        Database.connect(
            url = url,
            driver = Config.Database.DRIVER,
            user = Config.Database.USER,
            password = Config.Database.PASSWORD
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

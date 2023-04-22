package com.cocahonka.saltomaru.config

/**
 * Объект для хранения конфигурации плагина.
 */
object SaltomaruConfig {

    /**
     * Объект для хранения конфигурации базы данных.
     */
    object SaltomaruDatabaseConfig {
        const val USER = "saltomaru"
        const val PASSWORD = ""
        const val FILE_NAME = "saltomaru.db"
        const val DRIVER = "org.sqlite.JDBC"
        const val PRE_URL = "jdbc:sqlite:"
    }
}
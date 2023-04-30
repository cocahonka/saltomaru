package com.cocahonka.saltomaru.config

/**
 * Объект для хранения конфигурации плагина.
 */
object SaltomaruConfig {

    /**
     * Объект для хранения конфигурации базы данных.
     */
    object Database {
        const val USER = "saltomaru"
        const val PASSWORD = ""
        const val FILE_NAME = "saltomaru.db"
        const val DRIVER = "org.sqlite.JDBC"
        const val PRE_URL = "jdbc:sqlite:"

        private const val FOREIGN_PARAMETER = "foreign_keys=on"
        const val PARAMETERS = "?$FOREIGN_PARAMETER"

        const val BATCHED_CHUNK_SIZE = 100

        const val SYNC_PERIOD_MS = 10_000L
    }

    /**
     * Объект для хранения константных свойств ObjectPool'а
     */
    object ObjectPool {
        const val CAULDRONS_INIT_SIZE = 30
    }

    object Cauldron {
        const val HEATING_TIME_MS = 5_000L
    }
}
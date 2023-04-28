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

        private const val BATCHED_PARAMETERS = "rewriteBatchedStatements=true"
        const val PARAMETERS = "?${BATCHED_PARAMETERS}"

        const val BATCHED_CHUNK_SIZE = 100
    }

    /**
     * Объект для хранения константных значений майкнарфта
     */

    object Minecraft {
        const val OVERWORLD_ID = 0
        const val NETHER_ID = 1
        const val END_ID = 2
        val WORLD_IDS = listOf(OVERWORLD_ID, NETHER_ID, END_ID)

        const val MIN_X = -29_999_983
        const val MAX_X = 29_999_983

        const val MIN_Z = -29_999_983
        const val MAX_Z = 29_999_983

        const val MIN_OVERWORLD_Y = -64
        const val MAX_OVERWORLD_Y = 319

        const val MIN_NETHER_Y = 0
        const val MAX_NETHER_Y = 255

        const val MIN_END_Y = 0
        const val MAX_END_Y = 255
    }
}
package com.cocahonka.saltomaru.config

import com.cocahonka.saltomaru.exceptions.UnknownEnvironmentException
import org.bukkit.World

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
     * Объект для хранения константных значений майкнарфта
     */
    object Minecraft {
        enum class WorldsIds(val id: Int) {
            OVERWORLD_ID(0),
            NETHER_ID(1),
            END_ID(2);
        }

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

        /**
         * Возвращает идентификатор мира, соответствующий указанной мировой среде [environment].
         *
         * @param environment мировая среда, для которой нужно определить идентификатор мира
         * @return идентификатор мира, соответствующий указанной мировой среде
         * @throws UnknownEnvironmentException если передана неизвестная мировая среда
         */
        fun getWorldIdFromEnvironment(environment: World.Environment) =
            when (environment) {
                World.Environment.NORMAL -> WorldsIds.OVERWORLD_ID.id
                World.Environment.NETHER -> WorldsIds.NETHER_ID.id
                World.Environment.THE_END -> WorldsIds.END_ID.id
                World.Environment.CUSTOM -> throw UnknownEnvironmentException(environment)
            }
    }

    /**
     * Объект для хранения константных свойств ObjectPool'а
     */
    object ObjectPool {
        const val CAULDRONS_INIT_SIZE = 30
    }
}
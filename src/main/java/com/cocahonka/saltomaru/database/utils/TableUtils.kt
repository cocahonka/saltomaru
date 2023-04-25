package com.cocahonka.saltomaru.database.utils

import com.cocahonka.saltomaru.database.base.TableMappable
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

/**
 * Объект хранящий вспомогательные статические функции для [Table]
 */
object TableUtils {

    /**
     * Функция расширение для типобезопасного маппинга данных из записей базы данных
     * @param T [Table] который имлементирует [TableMappable]
     * @param E entity (data class) - в какой тип мапятся данные
     * @param actions closed-scope функция типа [T] для выборки данных из базы данных
     * @return [List] с элементами типа [E] после маппинга
     */
    inline fun <T, E> T.safeMap(actions: T.() -> Query): List<E>
            where T : Table, T : TableMappable<E> = actions(this).map(::fromRow)

    inline fun <T, E> T.safeMapSingle(actions: T.() -> ResultRow): E
            where T : Table, T : TableMappable<E> = fromRow(actions(this))
}
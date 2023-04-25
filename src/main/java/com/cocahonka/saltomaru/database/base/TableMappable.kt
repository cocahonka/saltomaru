package com.cocahonka.saltomaru.database.base

import com.cocahonka.saltomaru.database.utils.TableUtils.safeMap
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

/**
 * Интерфейс для расширения [Table] для возможности маппингов
 * @param E entity (data class) во что будут мапиться записи из [Table]
 */
interface TableMappable<E> {
    /**
     * Функция маппинга [ResultRow] в объект типа [E]
     *
     * ВАЖНО: ВЫЗОВ НАПРЯМУЮ ЧЕРЕЗ ИМПЛЕМЕНТАТОРА МОЖЕТ ВЫЗВАТЬ ОШИБКУ!
     * Используйте [safeMap]
     * @param resultRow запись полученная при чтении из базы данных
     * @return объект типа [E]
     */
     fun fromRow(resultRow: ResultRow): E

}

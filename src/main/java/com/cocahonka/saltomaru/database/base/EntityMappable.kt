package com.cocahonka.saltomaru.database.base

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.Table

/**
 * Интерфейс для маппинга Entity (data class) в выражения баз данных
 * @param I тип для [EntityID] - главный идентификатор таблицы из базы данных
 */
interface EntityMappable<I : Comparable<I>> {

    /**
     * Функция перевода Entity (data class) в выражение вставки в базу данных
     * @param statement выражение вставки из [Table]
     * @param id главный идентификатор
     * @return [InsertStatement] готовое выражение вставки
     */
    fun toInsertStatement(statement: InsertStatement<Number>, id: EntityID<I>): InsertStatement<Number>

    /**
     * Функция перевода Entity (data class) в выражение обновления в базе данных
     * @param statement выражение обновления из [Table]
     * @param id главный идентификатор
     * @return [UpdateStatement] готовое выражение обновления
     */
    fun toUpdateStatement(statement: UpdateStatement, id: EntityID<I>): UpdateStatement
}
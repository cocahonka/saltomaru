package com.cocahonka.saltomaru.database.utils

import com.cocahonka.saltomaru.database.base.TableConditions
import com.cocahonka.saltomaru.database.base.TableMappable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*

/**
 * Объект хранящий вспомогательные статические функции для [Table]
 */
object TableUtils {

    /**
     * Расширяющая функция для типобезопасного преобразования данных из записей базы данных.
     *
     * @param T [Table], реализующий [TableMappable].
     * @param E тип сущности (data class), в который происходит преобразование данных.
     * @param actions функция внутри области действия типа [T] для выборки данных из базы данных.
     * @return [List] элементов типа [E] после преобразования.
     */
    inline fun <T, E> T.safeMap(actions: T.() -> Query): List<E>
            where T : Table, T : TableMappable<E> = actions(this).map(::fromRow)

    /**
     * Расширяющая функция для типобезопасного преобразования одной записи базы данных.
     *
     * @param T [Table], реализующий [TableMappable].
     * @param E тип сущности (data class), в который происходит преобразование данных.
     * @param actions функция внутри области действия типа [T] для выборки данных из базы данных.
     * @return элемент типа [E] после преобразования.
     */
    inline fun <T, E> T.safeMapSingle(actions: T.() -> ResultRow): E
            where T : Table, T : TableMappable<E> = fromRow(actions(this))

    /**
     * Расширяющая функция для извлечения списка идентификаторов из записей базы данных.
     *
     * @param I тип идентификатора, реализующий [Comparable].
     * @param T тип таблицы, наследующий [IdTable] с идентификатором типа [I].
     * @param actions функция внутри области действия типа [T] для выборки данных из базы данных.
     * @return [List] идентификаторов типа [EntityID]<[I]>.
     */
    inline fun <I: Comparable<I>, T: IdTable<I>> T.safeGetIds(actions: T.() -> List<ResultRow>): List<EntityID<I>> =
        actions(this).map { it[id] }

    /**
     * Функция для объединения списка условий [Op]<[Boolean]> с помощью оператора [or].
     *
     * @return [Op]<[Boolean]> объединенное условие.
     */
    fun List<Op<Boolean>>.getCombinedEitherCondition() : Op<Boolean> =
        reduce { acc, condition -> acc or condition }

    /**
     * Расширяющая функция для получения уникальных существующих сущностей в базе данных из списка [prompt].
     *
     * @param T тип таблицы, наследующий [Table], [TableMappable]<[E]> и [TableConditions]<[E]>.
     * @param E тип сущности (data class).
     * @param prompt список сущностей, по которым нужно получить уникальные существующие сущности.
     * @return [List] уникальных существующих сущностей типа [E].
     */
    fun <T, E> T.getUniqueExistingEntitiesFromPrompt(prompt: List<E>) : List<E>
    where T: Table, T: TableMappable<E>, T: TableConditions<E> = safeMap {
        val conditions = prompt.map(::selectUniqueCondition)
        select(conditions.getCombinedEitherCondition())
    }
}
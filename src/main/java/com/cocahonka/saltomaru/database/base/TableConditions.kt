package com.cocahonka.saltomaru.database.base

import org.jetbrains.exposed.sql.Op

/**
 * Интерфейс, определяющий методы для создания условий выборки данных из базы данных
 *
 * @param E тип сущности
 */
interface TableConditions<E> {

    /**
     * Создает условие выборки, которое должно быть уникальным для каждого экземпляра
     * сущности [entity] при выборке из таблицы.
     *
     * @param entity экземпляр сущности, для которого нужно создать уникальное условие
     * @return [Op] типа [Boolean] - уникальное условие выборки для сущности [entity]
     */
    fun selectUniqueCondition(entity: E): Op<Boolean>
}
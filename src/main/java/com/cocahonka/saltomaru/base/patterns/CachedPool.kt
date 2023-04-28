package com.cocahonka.saltomaru.base.patterns

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Класс [CachedPool] представляет собой комбинацию реестра и объектного пула для хранения
 * и переиспользования объектов типа [T] с ключами типа [K].
 *
 * Данный класс предоставляет следующие функциональные возможности:
 * 1. Получение объекта по ключу. Если объект существует в реестре, он возвращается. В противном случае
 *    объект пытается быть извлечен из объектного пула, и если объектный пул пуст, создается новый объект.
 * 2. Удаление объекта по ключу. Объект удаляется из реестра и перемещается в объектный пул для возможного переиспользования.
 *
 * @param T тип объектов, хранящихся в реестре и объектном пуле
 * @param K тип ключей, используемых для идентификации объектов
 * @param factory функция для создания новых объектов типа [T], принимающая аргумент типа [K]
 * @param update функция для обновления объектов типа [T] перед их использованием, принимающая объект типа [T] и аргумент типа [K]
 */
open class CachedPool<T : Any, in K : Any>(
    private val factory: (K) -> T,
    private val update: (T, K) -> Unit,
) {
    private val registry = ConcurrentHashMap<K, T>()
    private val objectPool = ConcurrentLinkedQueue<T>()

    /**
     * Возвращает объект типа [T] для указанного ключа типа [K].
     * Если объект существует в реестре, он возвращается. В противном случае
     * объект пытается быть извлечен из объектного пула, и если объектный пул пуст, создается новый объект.
     *
     * @param key ключ типа [K] для идентификации объекта типа [T]
     * @return объект типа [T], соответствующий указанному ключу
     */
    @Synchronized
    fun getInstance(key: K): T {
        registry[key]?.let { return it }

        val item = objectPool.poll()

        return if (item != null) {
            update(item, key)
            registry[key] = item
            onCreate(item)
            item
        } else {
            val newItem = factory(key)
            registry[key] = newItem
            objectPool.offer(newItem)
            onCreate(newItem)
            newItem
        }
    }

    /**
     * Удаляет объект типа [T] с указанным ключом типа [K] из реестра и перемещает его в объектный пул
     * для возможного переиспользования.
     *
     * @param key ключ типа [K] для идентификации объекта типа [T], который нужно удалить
     * @return удаленный объект типа [T] или `null`, если объект с указанным ключом не найден
     */
    @Synchronized
    fun removeInstance(key: K): T? {
        return registry.remove(key)?.also {
            objectPool.offer(it)
            onDelete(it)
        }
    }

    /**
     * Фукнция, вызываемая после удаления объекта из реестра.
     *
     * @param item объект типа [T], который был удален
     */
    protected open fun onDelete(item: T) {}

    /**
     * Метод, вызываемый после создания или переиспользования объекта.
     *
     * @param item объект типа [T], который был создан или переиспользован
     */
    protected open fun onCreate(item: T) {}

}

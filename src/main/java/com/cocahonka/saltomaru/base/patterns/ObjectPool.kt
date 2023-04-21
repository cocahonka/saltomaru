package com.cocahonka.saltomaru.base.patterns

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Универсальный класс пула объектов.
 *
 * @param T тип объектов, управляемых пулом.
 * @param A тип аргумента, необходимого для создания и обновления объекта.
 * @param factory функция, которая создает новый экземпляр типа [T].
 * @param update функция, которая обновляет существующий экземпляр типа [T] с использованием аргумента типа [A].
 *
 * @property factory функция-фабрика, используемая для создания новых экземпляров типа [T].
 * @property update функция, используемая для обновления существующих экземпляров типа [T].
 * @property pool очередь, которая хранит доступные экземпляры объектов типа [T].
 *
 */
class ObjectPool<T, A>(private val factory: (A) -> T, private val update: (T, A) -> Unit) {
    private val pool = ConcurrentLinkedQueue<T>()

    /**
     *  [acquire] извлекает доступный экземпляр объекта из пула, обновляет его с использованием
     *  аргумента типа [A] и возвращает его.
     */
    fun acquire(arg: A): T {
        val item = pool.poll() ?: factory(arg)
        update(item, arg)
        return item
    }

    /**
     * [release] возвращает экземпляр объекта обратно в пул.
     */
    fun release(item: T) {
        pool.offer(item)
    }
}

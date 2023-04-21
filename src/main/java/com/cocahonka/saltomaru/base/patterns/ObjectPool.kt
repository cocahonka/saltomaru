package com.cocahonka.saltomaru.base.patterns

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Универсальный, потокобезопасный класс пула объектов.
 *
 * @param T тип объектов, управляемых пулом.
 * @param factory функция, которая создает новый экземпляр типа [T].
 *
 * @property factory функция-фабрика, используемая для создания новых экземпляров типа [T].
 * @property pool очередь, которая хранит доступные экземпляры объектов типа [T].
 */
class ObjectPool<T>(private val factory: () -> T) {
    private val pool = ConcurrentLinkedQueue<T>()

    /**
     * [acquire] извлекает доступный экземпляр объекта из пула или создает новый, если пул пуст.
     * @return экземпляр типа [T]
     */
    private fun acquire(): T {
        return pool.poll() ?: factory()
    }

    /**
     * [release] возвращает экземпляр объекта типа [T] обратно в пул.
     */
    private fun release(item: T) {
        pool.offer(item)
    }
}

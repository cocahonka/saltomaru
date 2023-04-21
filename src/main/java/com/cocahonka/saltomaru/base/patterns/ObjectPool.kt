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
 *
 * @function acquire извлекает доступный экземпляр объекта из пула или создает новый, если пул пуст.
 * @function release возвращает экземпляр объекта обратно в пул.
 */
class ObjectPool<T>(private val factory: () -> T) {
    private val pool = ConcurrentLinkedQueue<T>()

    fun acquire(): T {
        return pool.poll() ?: factory()
    }

    fun release(item: T) {
        pool.offer(item)
    }
}

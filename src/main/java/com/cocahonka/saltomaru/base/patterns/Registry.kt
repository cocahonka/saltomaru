package com.cocahonka.saltomaru.base.patterns

import java.util.concurrent.ConcurrentHashMap

/**
 * Потокобезопасный класс реализующий паттерн "реестр", предоставляющий экземпляры типа [T] на основе ключа типа [K].
 * Экземпляры хранятся в кэше и создаются с использованием предоставленной функции [factory], если
 * они еще не существуют в кэше.
 *
 * @param T тип объектов, управляемых реестром.
 * @param K тип ключа, используемого для идентификации экземпляров в реестре.
 * @param factory функция, которая принимает ключ типа [K] и создает новый экземпляр типа [T].
 *
 * @property factory функция-фабрика, используемая для создания новых экземпляров типа [T].
 * @property instances кэш, который хранит экземпляры типа [T] на основе ключа типа [K].
 *
 * @constructor Создает новый реестр с предоставленной функцией [factory].
 *
 * @function getInstance возвращает экземпляр типа [T] для данного ключа типа [K]. Если экземпляр не
 * существует в кэше, он создается с использованием функции [factory] и добавляется в кэш.
 */
open class Registry<out T : Any, in K>(private val factory: (K) -> T) {
    private val instances = ConcurrentHashMap<K, T>()

    @Synchronized
    fun getInstance(key: K): T {
        return instances.getOrPut(key) { factory(key) }
    }
}
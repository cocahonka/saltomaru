package com.cocahonka.saltomaru.base.patterns

/**
 * Универсальный, потокобезопасный класс синглтона с одним аргументом.
 *
 * @param T тип объекта синглтона.
 * @param A тип аргумента, необходимого для создания объекта синглтона.
 * @param factory функция, которая принимает аргумент типа [A] и создает новый экземпляр типа [T].
 *
 * @property factory функция-фабрика, используемая для создания новых экземпляров типа [T].
 * @property instance ссылка на экземпляр объекта синглтона или `null`, если он еще не был создан.
 *
 */
open class SingletonHolder<out T : Any, in A>(private val factory: (A) -> T) {
    @Volatile
    private var instance: T? = null

    /**
     * [getInstance] возвращает существующий экземпляр синглтона или создает новый, если он еще не существует.
     * @param arg аргумент типа [А] для получения синглтона типа [T]
     */
    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = factory(arg)
                instance = created
                created
            }
        }
    }
}

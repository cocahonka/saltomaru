package com.cocahonka.saltomaru.utils

import java.io.Serializable

/**
 * Класс Quadruple представляет кортеж из четырех элементов разных типов.
 *
 * @param A тип первого элемента кортежа
 * @param B тип второго элемента кортежа
 * @param C тип третьего элемента кортежа
 * @param D тип четвертого элемента кортежа
 * @property first первый элемент кортежа
 * @property second второй элемент кортежа
 * @property third третий элемент кортежа
 * @property fourth четвертый элемент кортежа
 */
data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
) : Serializable {

    override fun toString(): String = "($first, $second, $third, $fourth)"
}

/**
 * Преобразует [Quadruple] с одинаковыми типами элементов в список.
 *
 * @return список, состоящий из элементов Quadruple в порядке: first, second, third, fourth
 */
fun <T> Quadruple<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)

/**
 * Распаковывает список объектов [Quadruple] в четыре списка, содержащие соответствующие элементы.
 *
 * @return [Quadruple] из четырех списков, каждый из которых содержит элементы одного типа
 */
fun <A, B, C, D> List<Quadruple<A, B, C, D>>.unzip(): Quadruple<List<A>, List<B>, List<C>, List<D>> {
    val listA = ArrayList<A>(size)
    val listB = ArrayList<B>(size)
    val listC = ArrayList<C>(size)
    val listD = ArrayList<D>(size)

    for ((a, b, c, d) in this) {
        listA.add(a)
        listB.add(b)
        listC.add(c)
        listD.add(d)
    }

    return Quadruple(listA, listB, listC, listD)
}

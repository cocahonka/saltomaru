package com.cocahonka.saltomaru.database.base

import org.jetbrains.exposed.sql.ColumnType
import java.nio.ByteBuffer
import java.util.*

/**
 * [UUIDBinaryColumnType] представляет собой пользовательский тип столбца для хранения значений типа [UUID]
 * в виде двоичных данных (BINARY(16)) в базе данных. Этот тип столбца предоставляет методы для
 * преобразования значений между [UUID] и [ByteArray] при взаимодействии с базой данных.
 *
 * Внимание: этот тип столбца предназначен для использования с Kotlin Exposed DSL.
 */
object UUIDBinaryColumnType : ColumnType() {

    /**
     * Возвращает SQL тип данных для столбца, хранящего значения [UUID] в виде BINARY(16).
     */
    override fun sqlType() = "BINARY(16)"

    /**
     * Преобразует значение, полученное из базы данных, в значение [UUID].
     *
     * @param value значение, полученное из базы данных.
     * @return значение типа [UUID], преобразованное из [value].
     * @throws IllegalStateException если значение [value] имеет неожиданный тип.
     */
    override fun valueFromDB(value: Any): Any = when (value) {
        is UUID -> value
        is ByteArray -> ByteBuffer.wrap(value).let { bb -> UUID(bb.long, bb.long) }
        else -> error("Unexpected value of type UUID: $value of ${value::class.qualifiedName}")
    }

    /**
     * Преобразует значение типа [UUID] в [ByteArray] для записи в базу данных.
     *
     * @param value значение типа [UUID], которое необходимо преобразовать.
     * @return преобразованное значение типа [ByteArray].
     * @throws IllegalStateException если значение [value] имеет неожиданный тип.
     */
    override fun notNullValueToDB(value: Any): Any = when (value) {
        is UUID -> ByteBuffer.allocate(16).putLong(value.mostSignificantBits).putLong(value.leastSignificantBits).array()
        else -> error("Unexpected value of type UUID: $value of ${value::class.qualifiedName}")
    }
}

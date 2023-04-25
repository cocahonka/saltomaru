package com.cocahonka.saltomaru.database.base

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

interface EntityMappable<I : Comparable<I>> {

    fun toInsertStatement(statement: InsertStatement<Number>, id: EntityID<I>): InsertStatement<Number>

    fun toUpdateStatement(statement: UpdateStatement, cauldronId: EntityID<I>): UpdateStatement
}
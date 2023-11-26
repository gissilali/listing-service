package com.trivago.database.tables

import org.jetbrains.exposed.sql.Table


object Locations : Table() {
    val id = uuid("id").autoGenerate()
    val city = varchar("city", 255)
    val state = varchar("state", 255)
    val country = varchar("country", 255)
    val zipCode = integer("zip_code")
    val address = text("address")
    override val primaryKey = PrimaryKey(id)
}
package com.trivago.database.tables

import org.jetbrains.exposed.sql.Table


object Locations : Table() {
    val id = uuid("id").autoGenerate()
    val city = largeText("city")
    val state = largeText("state")
    val country = largeText("country")
    val zipCode = integer("zip_code")
    val address = largeText("address")
    override val primaryKey = PrimaryKey(id)
}
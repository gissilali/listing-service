package com.trivago.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

enum class AccommodationCategory(value: String) {
    Hotel("hotel"),
    Alternative("alternative"),
    Hostel("hostel"),
    Lodge("lodge"),
    Resort("resort"),
    GuestHouse("guest-house"),
}

object Accommodations : Table() {
    val id = uuid("id").autoGenerate()

    val name = varchar("name", 255)
    val hotelierId = uuid("hotelier_id").nullable()
    val rating = integer("rating")
    val category = enumerationByName("categories", 50, AccommodationCategory::class)
    val locationId = uuid("location_id")
    val image = text("image")
    val reputation = integer("reputation").default(0)
    val availability = integer("availability").default(0)
    val publishedOn = datetime("published_on").defaultExpression(CurrentDateTime).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
    val isUpdating = bool("is_updating").default(false)
    val deletedOn = datetime("deleted_on").nullable()


    override val primaryKey = PrimaryKey(id)
}
package com.trivago.services

import com.trivago.database.tables.Accommodations
import com.trivago.dtos.AccommodationDTO
import com.trivago.dtos.CreateAccommodationDTO
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import toAccommodationDTO

class AccommodationService {
    fun create(data: CreateAccommodationDTO): AccommodationDTO {
        return transaction {
            val accommodationId = Accommodations.insert {
                it[name] = data.name
                it[rating] = data.rating
                it[category] = data.category
                it[locationId] = data.locationId
                it[image] = data.image
                it[reputation] = data.reputation
                it[availability] = data.availability
            } get Accommodations.id

            val accommodation = Accommodations.select {
                Accommodations.id eq accommodationId
            }.limit(1).map { it.toAccommodationDTO() }.singleOrNull()

            accommodation!!
        }


    }
}
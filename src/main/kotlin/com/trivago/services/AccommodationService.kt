package com.trivago.services

import com.trivago.database.tables.Accommodations
import com.trivago.dtos.AccommodationDTO
import com.trivago.dtos.CreateAccommodationDTO
import com.trivago.dtos.UpdateAccommodationDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.transaction
import toAccommodationDTO
import java.util.UUID
import kotlin.reflect.full.memberProperties

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

    fun getAccommodationById(id: UUID): AccommodationDTO? {
        return transaction {
            Accommodations.select {
                Accommodations.id eq id
            }.andWhere {
                Accommodations.deletedOn eq null
            }.limit(1).map { it.toAccommodationDTO() }.singleOrNull()
        }
    }

    fun deleteAccommodationById(id: UUID): Boolean {
        return transaction {
            val updatedCount = Accommodations.update({ Accommodations.id eq id }) {
                it[deletedOn] = CurrentDateTime
            }

            updatedCount > 0
        }
    }

    fun updateAccommodationById(id: UUID, data: UpdateAccommodationDTO) : AccommodationDTO {
        return transaction {
            Accommodations.update({ Accommodations.id eq id }) { updateStatement ->
                data.name?.let {
                    updateStatement[name] = it
                }
                data.rating?.let {
                    updateStatement[rating] = it
                }
                data.category?.let {
                    updateStatement[category] = it
                }
                data.locationId?.let {
                    updateStatement[locationId] = it
                }
                data.image?.let {
                    updateStatement[image] = it
                }
                data.reputation?.let {
                    updateStatement[reputation] = it
                }
            }

            val accommodation = Accommodations.select {
                Accommodations.id eq id
            }.limit(1).map { it.toAccommodationDTO() }.singleOrNull()

            accommodation!!
        }
    }

    fun updateAvailability(id: UUID, numberOfRooms: Int) : AccommodationDTO {
        return transaction {
            Accommodations.update({ Accommodations.id eq id }) {
                it[availability] = 1
            }

            val accommodation = Accommodations.select {
                Accommodations.id eq id
            }.limit(1).map { it.toAccommodationDTO() }.singleOrNull()

            accommodation!!
        }
    }
}
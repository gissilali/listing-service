package com.trivago.services

import com.trivago.database.tables.Accommodations
import com.trivago.database.tables.Locations
import com.trivago.dtos.*
import com.trivago.exceptions.ConcurrentUpdateException
import com.trivago.exceptions.NoAvailableRoomsException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.transaction
import com.trivago.mappers.toAccommodationDTO
import java.util.UUID

class AccommodationService {
    fun create(userId: UUID, data: CreateAccommodationDTO): AccommodationDTO {
        return transaction {
            val newLocationId = addOrUpdateLocation(data.location)

            val accommodationId = Accommodations.insert {
                it[hotelierId] = userId
                it[name] = data.name
                it[rating] = data.rating
                it[category] = data.category
                it[locationId] = newLocationId
                it[image] = data.image
                it[reputation] = data.reputation
                it[availability] = data.availability
            } get Accommodations.id

            getAccommodationById(accommodationId)!!
        }
    }

    fun getAccommodationById(id: UUID): AccommodationDTO? {

        return transaction {
            Accommodations.join(
                Locations,
                JoinType.INNER,
                onColumn = Accommodations.locationId,
                otherColumn = Locations.id
            ).select {
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

    fun updateAccommodationById(id: UUID, data: UpdateAccommodationDTO): AccommodationDTO {
        return transaction {
            val updatedLocationId = data.location?.let { location ->
                addOrUpdateLocation(location)
            }
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
                updatedLocationId?.let {
                    updateStatement[locationId] = it
                }

                data.image?.let {
                    updateStatement[image] = it
                }
                data.reputation?.let {
                    updateStatement[reputation] = it
                }
            }



            getAccommodationById(id)!!
        }
    }

    private fun addOrUpdateLocation(data: LocationDTO): UUID {
        if (data.id == null) {
            return Locations.insert {
                it[city] = data.city
                it[address] = data.address
                it[state] = data.state
                it[country] = data.country
                it[zipCode] = data.zipCode
            } get Locations.id
        } else {
            return Locations.upsert {
                it[id] = data.id
                it[city] = data.city
                it[address] = data.address
                it[state] = data.state
                it[country] = data.country
                it[zipCode] = data.zipCode
            } get Locations.id
        }
    }

    fun updateAvailability(id: UUID, bookingInfo: BookingInfoDTO): AccommodationDTO {
        return transaction {
            //get accommodation that is not being currently updated
            val accommodation = Accommodations.select {
                Accommodations.id eq id
            }.andWhere {
                Accommodations.isUpdating eq false
            }.limit(1).map { it.toAccommodationDTO() }.singleOrNull()

            //throw an error if it is being currently updated
            accommodation ?: throw ConcurrentUpdateException("The requested cannot be updated")

            //lock the current accommodation
            Accommodations.update({ Accommodations.id eq id }) {
                it[isUpdating] = true
            }


            if (bookingInfo.numberOfRooms > accommodation.availability) {
                throw NoAvailableRoomsException("No rooms available")
            }

            //update and unlock the current accommodation
            Accommodations.update({ Accommodations.id eq id }) {
                it[isUpdating] = false
                it[availability] = accommodation.availability - bookingInfo.numberOfRooms
            }

            getAccommodationById(id)!!
        }
    }

    fun getAccommodations(hotelierId: UUID, accommodationFilterDTO: AccommodationFilterDTO): List<AccommodationDTO> {
        return transaction {
            val query = Accommodations.join(
                Locations,
                JoinType.INNER,
                onColumn = Accommodations.locationId,
                otherColumn = Locations.id
            )
                .selectAll()
                .andWhere {
                    Accommodations.deletedOn eq null
                }
                .andWhere {
                    Accommodations.hotelierId eq hotelierId
                }


            accommodationFilterDTO.rating?.let {
                query.andWhere {
                    Accommodations.rating eq it
                }
            }

            accommodationFilterDTO.reputationBadge?.let {
                when (it) {
                    "red" -> query.andWhere {
                        Accommodations.reputation lessEq 500
                    }

                    "yellow" -> query.andWhere {
                        Accommodations.reputation greater 500
                    }.andWhere {
                        Accommodations.reputation lessEq 799
                    }

                    else -> query.andWhere {
                        Accommodations.reputation greater 799
                    }
                }

            }

            accommodationFilterDTO.city?.let {
                query.andWhere {
                    Locations.city like "%$it%"
                }
            }

            query.toList().map {
                it.toAccommodationDTO()!!
            }
        }
    }
}
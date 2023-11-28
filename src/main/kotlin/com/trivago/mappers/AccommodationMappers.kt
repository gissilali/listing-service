package com.trivago.mappers

import com.trivago.database.tables.AccommodationCategory
import com.trivago.database.tables.Accommodations
import com.trivago.dtos.AccommodationDTO
import com.trivago.dtos.CreateAccommodationDTO
import com.trivago.dtos.CreateAccommodationRequestDTO
import com.trivago.utils.calculateReputationBadgeColor
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

fun ResultRow?.toAccommodationDTO(): AccommodationDTO? {
    return if (this == null) {
        null
    } else {
        AccommodationDTO(
            id = this[Accommodations.id].toString(),
            name = this[Accommodations.name],
            rating = this[Accommodations.rating],
            category = this[Accommodations.category],
            locationId = this[Accommodations.locationId].toString(),
            image = this[Accommodations.image],
            reputation = this[Accommodations.reputation],
            reputationBadge = calculateReputationBadgeColor(this[Accommodations.reputation]),
            availability = this[Accommodations.availability],
            createdAt = this[Accommodations.createdAt].toString(),
            updatedAt = this[Accommodations.createdAt].toString(),
            publishedOn = this[Accommodations.createdAt].toString(),
        )
    }
}


fun CreateAccommodationRequestDTO.toCreateAccommodationDTO(): CreateAccommodationDTO {
    return CreateAccommodationDTO(
        name = this.name!!,
        rating = this.rating!!,
        category = AccommodationCategory.valueOf(this.category!!),
        locationId = UUID.fromString(this.locationId),
        image = this.image!!,
        reputation = this.reputation!!,
        availability = this.availability!!,
    )
}

package com.trivago.mappers

import com.trivago.database.tables.AccommodationCategory
import com.trivago.database.tables.Accommodations
import com.trivago.database.tables.Locations
import com.trivago.dtos.AccommodationDTO
import com.trivago.dtos.CreateAccommodationDTO
import com.trivago.dtos.CreateAccommodationRequestDTO
import com.trivago.dtos.LocationRequestDTO
import com.trivago.utils.calculateReputationBadgeColor
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

fun ResultRow?.toAccommodationDTO(): AccommodationDTO? {
    return if (this == null) {
        null
    } else {
        var location: LocationRequestDTO? = null
        if (Locations.id in this.fieldIndex) {
            location = LocationRequestDTO(
                id = this[Locations.id].toString(),
                city = this[Locations.city],
                state = this[Locations.state],
                country = this[Locations.country],
                zipCode = this[Locations.zipCode],
                address = this[Locations.address],
            )
        }
        AccommodationDTO(
            id = this[Accommodations.id].toString(),
            name = this[Accommodations.name],
            rating = this[Accommodations.rating],
            category = this[Accommodations.category],
            image = this[Accommodations.image],
            reputation = this[Accommodations.reputation],
            reputationBadge = calculateReputationBadgeColor(this[Accommodations.reputation]),
            availability = this[Accommodations.availability],
            location = location
        )
    }
}


fun CreateAccommodationRequestDTO.toCreateAccommodationDTO(): CreateAccommodationDTO {
    return CreateAccommodationDTO(
        name = this.name!!,
        rating = this.rating!!,
        category = AccommodationCategory.valueOf(this.category!!),
        location = this.location!!.toLocationDTO(),
        image = this.image!!,
        reputation = this.reputation!!,
        availability = this.availability!!,
    )
}

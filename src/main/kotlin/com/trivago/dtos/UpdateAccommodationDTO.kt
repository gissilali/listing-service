package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import java.util.UUID

data class UpdateAccommodationDTO(
    val name: String? = null,
    val rating: Int? = null,
    val category: AccommodationCategory? = null,
    val location: LocationDTO? = null,
    val image: String? = null,
    val reputation: Int? = null,
)

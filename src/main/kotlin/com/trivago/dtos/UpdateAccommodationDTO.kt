package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import java.util.UUID

data class UpdateAccommodationDTO(
    val name: String? = null,
    val rating: Int? = null,
    val category: AccommodationCategory? = null,
    val locationId: UUID? = null,
    val image: String? = null,
    val reputation: Int? = null,
)

package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import java.util.UUID

data class CreateAccommodationDTO(
    val name: String,
    val rating: Int,
    val category: AccommodationCategory,
    val locationId: UUID,
    val image: String,
    val reputation: Int,
    val availability: Int
)

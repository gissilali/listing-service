package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import kotlinx.serialization.Serializable

@Serializable
data class AccommodationDTO(
    val id: String,
    val name: String,
    val rating: Int,
    val category: AccommodationCategory,
    val location: LocationRequestDTO?,
    val image: String,
    val reputation: Int,
    val reputationBadge: String,
    val availability: Int,
)

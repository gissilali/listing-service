package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class AccommodationDTO(
    val name: String,
    val rating: Int,
    val category: AccommodationCategory,
    val locationId: String,
    val image: String,
    val reputation: Int,
    val availability: Int,
    val publishedOn: String,
    val createdAt: String,
    val updatedAt: String,
)

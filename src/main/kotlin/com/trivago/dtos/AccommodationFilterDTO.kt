package com.trivago.dtos

data class
AccommodationFilterDTO(
    val rating: Int? = null,
    val city: String? = null,
    val reputationBadge: String? = null,
)

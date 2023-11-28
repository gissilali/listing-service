package com.trivago.dtos

import kotlinx.serialization.Serializable

@Serializable
data class LocationRequestDTO(
    val id: String?,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: Int,
    val address: String
)
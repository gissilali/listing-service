package com.trivago.dtos

import java.util.*


data class LocationDTO(
    val id: UUID?,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: Int,
    val address: String
)
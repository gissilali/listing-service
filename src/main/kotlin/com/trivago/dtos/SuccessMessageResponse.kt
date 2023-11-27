package com.trivago.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SuccessMessageResponse(
    val message: String
)

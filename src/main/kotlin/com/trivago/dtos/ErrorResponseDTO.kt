package com.trivago.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDTO(
   val error: String
)

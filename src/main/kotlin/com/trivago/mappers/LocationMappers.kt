package com.trivago.mappers

import com.trivago.dtos.LocationDTO
import com.trivago.dtos.LocationRequestDTO
import java.util.UUID


fun LocationRequestDTO.toLocationDTO(): LocationDTO {
    return LocationDTO(
        id = if (this.id == null) {
            null
        } else {
            UUID.fromString(this.id)
        },
        city = this.city,
        state = this.state,
        country = this.country,
        zipCode = this.zipCode,
        address = this.address
    )
}
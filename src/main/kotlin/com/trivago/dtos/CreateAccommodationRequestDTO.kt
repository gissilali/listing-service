package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import com.trivago.utils.doesNotHaveKeyword
import com.trivago.utils.isValidUUID
import kotlinx.serialization.*
import org.valiktor.functions.*
import org.valiktor.validate
import java.util.*

@Serializable
data class CreateAccommodationRequestDTO(
    val name: String?,
    val rating: Int?,
    val category: String?,
    val locationId: String?,
    val image: String?,
    val reputation: Int?,
    val availability: Int?
) {
    fun validateRequest(): CreateAccommodationDTO {
        validate(this) {
            validate(CreateAccommodationRequestDTO::name).isNotBlank().hasSize(max = 10)
                .doesNotHaveKeyword(listOf("Free", "Offer", "Book", "Website"))
            validate(CreateAccommodationRequestDTO::rating).isPositive().isBetween(0, 5)
            validate(CreateAccommodationRequestDTO::locationId).isValidUUID()
            validate(CreateAccommodationRequestDTO::category).isIn(AccommodationCategory.entries.toList().map {
                it.name
            })
            validate(CreateAccommodationRequestDTO::image).isNotBlank()
            validate(CreateAccommodationRequestDTO::reputation).isBetween(0, 1000)
            validate(CreateAccommodationRequestDTO::availability).isPositive()
        }

        return CreateAccommodationDTO(
            name = this.name!!,
            rating = this.rating!!,
            category = AccommodationCategory.valueOf(this.category!!),
            locationId = UUID.fromString(this.locationId!!),
            image = this.image!!,
            reputation = this.reputation!!,
            availability = this.availability!!,
        )
    }
}



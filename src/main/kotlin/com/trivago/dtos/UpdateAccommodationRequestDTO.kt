package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import com.trivago.utils.doesNotHaveKeyword
import com.trivago.utils.isValidUUID
import kotlinx.serialization.Serializable
import org.valiktor.functions.*
import org.valiktor.validate
import java.util.UUID

@Serializable
data class UpdateAccommodationRequestDTO(
    val name: String? = null,
    val rating: Int? = null,
    val category: String? = null,
    val locationId: String? = null,
    val image: String? = null,
    val reputation: Int? = null,
) {
    fun validateRequest(): UpdateAccommodationDTO {
        validate(this) {
            validate(UpdateAccommodationRequestDTO::name).hasSize(max = 10)
                .doesNotHaveKeyword(listOf("Free", "Offer", "Book", "Website"))
            validate(UpdateAccommodationRequestDTO::rating).isPositive().isBetween(0, 5)
            validate(UpdateAccommodationRequestDTO::locationId).isValidUUID()
            validate(UpdateAccommodationRequestDTO::category).isIn(AccommodationCategory.entries.toList().map {
                it.name
            })
            validate(UpdateAccommodationRequestDTO::image).isNotBlank()
            validate(UpdateAccommodationRequestDTO::reputation).isBetween(0, 1000)
        }

        return UpdateAccommodationDTO(
            name = this.name,
            rating = this.rating,
            category = this.category?.let {
                AccommodationCategory.valueOf(it)
            },
            locationId = this.locationId?.let {
                UUID.fromString(it)
            },
            image = this.image,
            reputation = this.reputation,
        )
    }
}

package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import com.trivago.mappers.toLocationDTO
import com.trivago.utils.doesNotHaveKeyword
import com.trivago.utils.hasLength
import com.trivago.utils.isValidUUID
import com.trivago.utils.isValidUrl
import kotlinx.serialization.Serializable
import org.valiktor.functions.*
import org.valiktor.validate
import java.util.UUID

@Serializable
data class UpdateAccommodationRequestDTO(
    val name: String? = null,
    val rating: Int? = null,
    val category: String? = null,
    val location: LocationRequestDTO? = null,
    val image: String? = null,
    val reputation: Int? = null,
) {
    fun validateRequest(): UpdateAccommodationDTO {
        validate(this) {
            validate(UpdateAccommodationRequestDTO::name).hasSize(min = 10)
                .doesNotHaveKeyword(listOf("Free", "Offer", "Book", "Website"))
            validate(UpdateAccommodationRequestDTO::rating).isPositive().isBetween(0, 5)
            validate(UpdateAccommodationRequestDTO::location).validate {
                validate(LocationRequestDTO::city).isNotBlank()
                validate(LocationRequestDTO::state).isNotBlank()
                validate(LocationRequestDTO::country).isNotBlank()
                validate(LocationRequestDTO::zipCode).hasLength(5)
                validate(LocationRequestDTO::id).isValidUUID()
            }
            validate(UpdateAccommodationRequestDTO::category).isIn(AccommodationCategory.entries.toList().map {
                it.name
            })
            validate(UpdateAccommodationRequestDTO::image).isValidUrl()
            validate(UpdateAccommodationRequestDTO::reputation).isBetween(0, 1000)
        }

        return UpdateAccommodationDTO(
            name = this.name,
            rating = this.rating,
            category = this.category?.let {
                AccommodationCategory.valueOf(it)
            },
            location = this.location?.toLocationDTO(),
            image = this.image,
            reputation = this.reputation,
        )
    }
}

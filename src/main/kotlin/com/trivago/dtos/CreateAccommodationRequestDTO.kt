package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import com.trivago.mappers.toLocationDTO
import com.trivago.utils.doesNotHaveKeyword
import com.trivago.utils.hasLength
import com.trivago.utils.isValidUUID
import com.trivago.utils.isValidUrl
import kotlinx.serialization.*
import org.valiktor.functions.*
import org.valiktor.validate
import java.util.*

@Serializable
data class CreateAccommodationRequestDTO(
    val name: String?,
    val rating: Int?,
    val category: String?,
    val location: LocationRequestDTO?,
    val image: String?,
    val reputation: Int?,
    val availability: Int?,

    ) {
    fun validateRequest(): CreateAccommodationDTO {
        validate(this) {
            validate(CreateAccommodationRequestDTO::name).isNotBlank().hasSize(min = 10)
                .doesNotHaveKeyword(listOf("Free", "Offer", "Book", "Website"))
            validate(CreateAccommodationRequestDTO::rating).isPositive().isBetween(0, 5)
            validate(CreateAccommodationRequestDTO::location).isNotNull().validate {
                validate(LocationRequestDTO::city).isNotBlank()
                validate(LocationRequestDTO::state).isNotBlank()
                validate(LocationRequestDTO::country).isNotBlank()
                validate(LocationRequestDTO::zipCode).hasLength(5)
                validate(LocationRequestDTO::id).isValidUUID()
            }
            validate(CreateAccommodationRequestDTO::category).isIn(AccommodationCategory.entries.toList().map {
                it.name
            })
            validate(CreateAccommodationRequestDTO::image).isNotBlank().isValidUrl()
            validate(CreateAccommodationRequestDTO::reputation).isBetween(0, 1000)
            validate(CreateAccommodationRequestDTO::availability).isPositive()
        }

        return CreateAccommodationDTO(
            name = this.name!!,
            rating = this.rating!!,
            category = AccommodationCategory.valueOf(this.category!!),
            image = this.image!!,
            reputation = this.reputation!!,
            availability = this.availability!!,
            location = this.location!!.toLocationDTO()
        )
    }
}



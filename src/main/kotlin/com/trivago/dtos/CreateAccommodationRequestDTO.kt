package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.*
import org.valiktor.Constraint
import org.valiktor.ConstraintViolationException
import org.valiktor.Validator
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


object ValidUUID : Constraint {
    override val messageBundle: String
        get() = "com/trivago/dtos"
}


data class Between<T>(val start: T, val end: T) : Constraint


fun <E> Validator<E>.Property<String?>.isValidUUID() = this.validate(ValidUUID) {
    it == null || try {
        UUID.fromString(it)
        true
    } catch (e: Throwable) {
        false
    }
}

fun <E> Validator<E>.Property<Int?>.isBetween(start: Int, end: Int) =
    this.validate(Between(start, end)) { it == null || it in start.rangeTo(end) }
package com.trivago.dtos

import com.trivago.database.tables.AccommodationCategory
import com.trivago.utils.doesNotHaveKeyword
import com.trivago.utils.isValidUUID
import kotlinx.serialization.Serializable
import org.valiktor.functions.*
import org.valiktor.validate
import java.util.*

@Serializable
data class BookingRequestDTO(
    val numberOfRooms: Int,
) {
    fun validateRequest(): BookingInfoDTO {
        validate(this) {
            validate(BookingRequestDTO::numberOfRooms).isNotNull().isGreaterThan(0)
        }

        return BookingInfoDTO(
            numberOfRooms = this.numberOfRooms
        )
    }
}

package com.trivago.controllers

import com.trivago.dtos.*
import com.trivago.services.AccommodationService
import io.ktor.http.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.UUID

class AccommodationsController(private val accommodationService: AccommodationService) {
    suspend fun getAccommodations(call: ApplicationCall) {
        val userId = UUID.fromString(call.request.headers["x-user-id"]!!)
        accommodationService.getAccommodations(
            hotelierId = userId,
            AccommodationFilterDTO(
                rating = call.request.queryParameters["rating"]?.toInt(),
                reputationBadge = call.request.queryParameters["reputationBadge"],
                city = call.request.queryParameters["city"],
            )
        ).apply {
            call.respond(this)
        }
    }

    suspend fun createAccommodation(call: ApplicationCall) {
        val userId = UUID.fromString(call.request.headers["x-user-id"]!!)
        call.receive<CreateAccommodationRequestDTO>().apply {
            accommodationService.create(userId, this.validateRequest()).apply {
                call.respond(this)
            }
        }
    }

    suspend fun getAccommodationById(call: ApplicationCall) {
        val id = call.parameters["id"]!!
        try {
            val uuid = UUID.fromString(id)
            accommodationService.getAccommodationById(uuid).apply {
                if (this == null) {
                    call.respond(
                        message = ErrorResponseDTO("No item found with the specified id."),
                        status = HttpStatusCode.NotFound
                    )
                } else {
                    call.respond(this)
                }

            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                message = ErrorResponseDTO("Invalid UUID format for parameter 'id'."),
                status = HttpStatusCode.UnprocessableEntity
            )
        }

    }

    suspend fun deleteAccommodation(call: ApplicationCall) {
        val id = call.parameters["id"]!!
        try {
            val uuid = UUID.fromString(id)
            accommodationService.deleteAccommodationById(uuid).apply {
                call.respond(SuccessMessageResponse("Accommodation successfully deleted."))
            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                message = ErrorResponseDTO("Invalid UUID format for parameter 'id'."),
                status = HttpStatusCode.UnprocessableEntity
            )
        }
    }

    suspend fun updateAccommodationById(call: ApplicationCall) {
        val id = call.parameters["id"]!!
        try {
            val uuid = UUID.fromString(id)
            call.receive<UpdateAccommodationRequestDTO>().apply {
                accommodationService.updateAccommodationById(uuid, this.validateRequest()).apply {
                    call.respond(this)
                }
            }

        } catch (e: IllegalArgumentException) {
            call.respond(
                message = ErrorResponseDTO("Invalid UUID format for parameter 'id'."),
                status = HttpStatusCode.UnprocessableEntity
            )
        }
    }

    suspend fun handleBooking(call: ApplicationCall) {
        val id = call.parameters["id"]!!
        try {
            val uuid = UUID.fromString(id)
            call.receive<BookingRequestDTO>().apply {
                accommodationService.updateAvailability(uuid, this.validateRequest()).apply {
                    call.respond(this)
                }
            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                message = ErrorResponseDTO("Invalid UUID format for parameter 'id'."),
                status = HttpStatusCode.UnprocessableEntity
            )
        }

    }
}
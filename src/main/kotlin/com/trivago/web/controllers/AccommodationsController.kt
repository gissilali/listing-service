package com.trivago.web.controllers

import com.trivago.dtos.CreateAccommodationRequestDTO
import com.trivago.services.AccommodationService
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.*
import io.ktor.server.response.*

class AccommodationsController(private val accommodationService: AccommodationService) {
    suspend fun store(call: ApplicationCall) {

        call.receive<CreateAccommodationRequestDTO>().apply {
            accommodationService.create(this.validateRequest()).apply {
                call.respond(this)
            }
        }
    }
}
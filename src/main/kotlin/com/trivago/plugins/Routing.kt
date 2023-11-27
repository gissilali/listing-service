package com.trivago.plugins

import com.trivago.controllers.AccommodationsController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val accommodationsController by inject<AccommodationsController>()

    routing {
        post("/accommodations") {
            accommodationsController.createAccommodation(context)
        }

        get("/accommodations/{id}") {
            accommodationsController.getAccommodationById(context)
        }

        patch("/accommodations/{id}") {
            accommodationsController.updateAccommodationById(context)
        }

        delete("/accommodations/{id}") {
            accommodationsController.deleteAccommodation(context)
        }
    }
}

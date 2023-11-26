package com.trivago.plugins

import com.trivago.web.controllers.AccommodationsController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val accommodationsController by inject<AccommodationsController>()

    routing {
        post("/accommodations") {
            accommodationsController.store(context)
        }
    }
}

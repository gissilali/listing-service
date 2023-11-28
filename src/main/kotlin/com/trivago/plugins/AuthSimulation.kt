package com.trivago.plugins

import com.trivago.dtos.ErrorResponseDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.UUID

val AuthSimulationPlugin = createApplicationPlugin(name = "AuthSimulationPlugin") {
    onCall { call ->
        call.request.headers.apply {
            if (this["x-user-id"] == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = ErrorResponseDTO("header 'x-user-id' is required")
                )
            } else {
                try {
                    UUID.fromString(this["x-user-id"])
                } catch (e: IllegalArgumentException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = ErrorResponseDTO("header 'x-user-id' is in an invalid form, make sure it is UUID")
                    )
                }
            }
        }
    }
}
fun  Application.configureFakeAuth() {
    install(AuthSimulationPlugin)
}
package com.trivago.plugins

import com.trivago.dtos.ErrorResponseDTO
import com.trivago.exceptions.ConcurrentUpdateException
import com.trivago.exceptions.NoAvailableRoomsException
import com.trivago.utils.parseValidationError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.valiktor.ConstraintViolationException

fun Application.configureHTTP(s: (ConcurrentUpdateException) -> String) {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        anyHost()
    }



    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respondText(text = "400: $cause", status = HttpStatusCode.BadRequest)
        }

        exception<ConstraintViolationException> { call, cause ->
            call.respond(message = parseValidationError(cause), status = HttpStatusCode.UnprocessableEntity)
        }

        exception<Throwable> { call, cause ->
            call.respond(message = ErrorResponseDTO(
                error = cause.toString()
            ), status = HttpStatusCode.InternalServerError)
        }

        exception<NoAvailableRoomsException> { call, cause ->
            call.respond(message = ErrorResponseDTO(
                cause.message.toString()
            ), status = HttpStatusCode.BadRequest)
        }

        exception<ConcurrentUpdateException> { call, cause ->
            call.respond(message = ErrorResponseDTO(
                s(cause)
            ), status = HttpStatusCode.InternalServerError)
        }
    }
}

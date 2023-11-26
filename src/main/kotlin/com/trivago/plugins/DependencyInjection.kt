package com.trivago.plugins

import com.trivago.web.controllers.AccommodationsController
import com.trivago.services.AccommodationService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val accommodationModule = module {
    single { AccommodationService() }
    single { AccommodationsController(get()) }
}
fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(accommodationModule)
    }
}


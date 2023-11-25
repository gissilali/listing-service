package com.trivago.plugins

import com.trivago.database.DatabaseFactory
import io.ktor.server.application.*

fun Application.configureDatabases() {
    DatabaseFactory.initialize()
}

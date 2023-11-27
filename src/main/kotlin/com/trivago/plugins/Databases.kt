package com.trivago.plugins

import com.trivago.database.DatabaseFactory
import io.ktor.server.application.Application

fun configureDatabases() {
    DatabaseFactory.initialize()
}

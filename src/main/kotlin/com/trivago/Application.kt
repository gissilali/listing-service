package com.trivago

import com.trivago.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureHTTP { cause -> cause.message.toString() }
    configureRouting()
    configureDependencyInjection()

}

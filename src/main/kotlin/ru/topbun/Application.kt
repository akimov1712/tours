package ru.topbun

import io.ktor.server.application.*
import io.ktor.server.netty.*
import ru.topbun.plugins.configureDatabases
import ru.topbun.plugins.configurePosting
import ru.topbun.plugins.configureRouting
import ru.topbun.plugins.configureSerialization

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureDatabases()
//    configurePosting()
}

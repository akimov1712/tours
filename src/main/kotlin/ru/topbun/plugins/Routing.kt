package ru.topbun.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.topbun.features.config.configureConfigRouting

fun Application.configureRouting() {
    configureConfigRouting()
}

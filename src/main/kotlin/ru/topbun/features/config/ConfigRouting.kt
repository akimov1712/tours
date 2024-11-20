package ru.topbun.features.config

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.topbun.models.config.Config

fun Application.configureConfigRouting() {
    routing {
        post("/config"){
            val controller = ConfigController(call)
            controller.saveConfig()
        }
        get("/config"){
            call.respond(Config.getConfigFromResource())
        }
    }
}

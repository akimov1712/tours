package ru.topbun.features.config

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.topbun.features.tours.TourScheduler
import ru.topbun.models.config.Config

class ConfigController(
    private val call: RoutingCall
) {

    suspend fun saveConfig(){
        val config = call.receive<Config>()
        config.saveConfig()
        call.respond(HttpStatusCode.OK)
    }

}
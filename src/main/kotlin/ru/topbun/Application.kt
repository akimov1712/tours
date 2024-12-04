package ru.topbun

import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.topbun.models.tours.Tours
import ru.topbun.network.api.TelegramApi
import ru.topbun.plugins.configureDatabases
import ru.topbun.plugins.configurePosting
import ru.topbun.plugins.configureRouting
import ru.topbun.plugins.configureSerialization
import ru.topbun.utills.Env

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureDatabases()
    configurePosting()
}

package ru.topbun

import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.config.Config
import ru.topbun.models.meals.Meals
import ru.topbun.models.tours.Tours
import ru.topbun.models.tours.Tours.clearTours
import ru.topbun.network.api.TelegramApi
import ru.topbun.network.api.ToursApi
import ru.topbun.plugins.configureDatabases
import ru.topbun.plugins.configurePosting
import ru.topbun.plugins.configureRouting
import ru.topbun.plugins.configureSerialization
import ru.topbun.utills.*
import java.awt.SystemColor.text

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureDatabases()
    configurePosting()
}

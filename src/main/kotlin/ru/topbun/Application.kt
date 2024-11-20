package ru.topbun

import io.ktor.client.call.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.topbun.models.config.Config
import ru.topbun.models.tours.HottourResponse
import ru.topbun.models.tours.Tours
import ru.topbun.network.TelegramApi
import ru.topbun.network.ToursApi
import ru.topbun.plugins.configureDatabases
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
    CoroutineScope(Dispatchers.IO).launch {
        val tours = ToursApi().getTours(Config.getConfigFromResource())
        tours.hottours.tours.map {
            val text = """
                Вылет из ${it.departurenamefrom}
                Прилет в ${it.countryname}

                Отель ${it.hotelname}
                Рейтинг отеля ${it.hotelrating}

                Дата вылета в ${it.flydate}

                Цена ${it.price} Р
            """.trimIndent()
            val imageLink = "https:" + it.hotelpicture.replace("small", "medium")
            println("LINKA: $imageLink")
            TelegramApi().sendMessageWithPhoto(text, imageLink, Env["CHAT_ID"])
            delay(10000)
        }
    }
}

package ru.topbun.plugins

import io.ktor.server.application.*
import ru.topbun.features.tours.TourScheduler

fun Application.configurePosting() {
    TourScheduler.start()
}
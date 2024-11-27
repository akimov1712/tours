package ru.topbun.plugins

import io.ktor.server.application.*
import ru.topbun.features.tours.TourScheduler
import ru.topbun.features.tours.TourSchedulerManager

fun Application.configurePosting() {
    TourSchedulerManager.startSchedulers()
}
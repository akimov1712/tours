package ru.topbun.features.tours

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import ru.topbun.models.config.Config
import ru.topbun.models.tours.Tours
import ru.topbun.models.tours.Tours.clearTours
import ru.topbun.network.api.TelegramApi
import ru.topbun.network.api.ToursApi
import ru.topbun.network.api.VkApi
import ru.topbun.utills.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TourScheduler(private val config: Config) {
    private val intervalFlow = MutableStateFlow(config.delayPostingMinutes)
    private var timerJob: Job? = null

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            intervalFlow.debounce(1000)
                .collect { newInterval ->
                    restartTimer(newInterval)
                }
        }
    }

    private fun restartTimer(intervalMinutes: Int) {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(intervalMinutes.toDuration(DurationUnit.MINUTES).inWholeMilliseconds)
                runCatching {
                    executeTask()
                }.onFailure { e ->
                    e.printStackTrace()
                    println("Error occurred: ${e.message}")
                }
            }
        }
    }

    fun cancel() {
        timerJob?.cancel()
    }

    private suspend fun executeTask() {
        val toursApi = ToursApi()
        val response = toursApi.getTours(config)
        val organizedTours = response.organizedTours()
        val filteredTours = organizedTours.getFilteredTours(config).takeIf { it.isNotEmpty() } ?: run {
            clearTours()
            organizedTours
        }
        filteredTours.firstOrNull()?.let {
            val tours = it.take(12)
            val tgMessage = tours.take(12).buildMessageToTelegramPost(config)
            val vkMessage = tours.buildMessageToVkPost(config)
            val imageLink = tours.getPreviewImage().buildImageLink()
            TelegramApi().sendMessageWithPhoto(tgMessage, imageLink, config.tgId)
            VkApi().sendMessageWithPhoto(vkMessage, imageLink, config.vkId)
            Tours.insertTourList(tours)
        }
    }
}
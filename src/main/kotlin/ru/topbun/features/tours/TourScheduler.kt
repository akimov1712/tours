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

object TourScheduler {
    private val intervalFlow = MutableStateFlow(getIntervalFromConfig())
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
                runCatching {
                    executeTask()
                }.onFailure { e ->
                    e.printStackTrace()
                    println("Error occurred: ${e.message}")
                }
                delay(intervalMinutes.toDuration(DurationUnit.MINUTES).inWholeMilliseconds)
            }
        }
    }

    fun updateInterval(newIntervalMinutes: Int) {
        intervalFlow.value = newIntervalMinutes
    }

    private suspend fun executeTask() {
        val toursApi = ToursApi()
        val config = Config.getConfigFromResource()
        val response = toursApi.getTours(config)
        val organizedTours = response.organizedTours()
        val filteredTours = organizedTours.getFilteredTours().takeIf { it.isNotEmpty() } ?: run {
            clearTours()
            organizedTours.getFilteredTours()
        }
        filteredTours.firstOrNull()?.let { tours ->
            val tgMessage = tours.buildMessageToTelegramPost()
            val vkMessage = tours.buildMessageToVkPost()
            val imageLink = tours.getPreviewImage().buildImageLink()
            TelegramApi().sendMessageWithPhoto(tgMessage, imageLink, Env["TG_CHAT_ID"])
            VkApi().sendMessageWithPhoto(vkMessage, imageLink, Env["VK_CHAT_ID"])
            Tours.insertTourList(tours)
        }
    }

    private fun getIntervalFromConfig(): Int {
        return Config.getConfigFromResource().delayPostingMinutes
    }
}
package ru.topbun.network.api

import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.topbun.network.ApiFactory
import ru.topbun.utills.Env

class VkApi(
    private val client: ApiFactory = ApiFactory
) {

    suspend fun sendMessageWithPhoto(text: String, photoUrl: String?, chatId: String){
        val response = if (photoUrl.isNullOrEmpty()) sendMessage(text, chatId) else sendPhoto(photoUrl, text, chatId)
        println("HTTP LOG: ${response.body<String>()}")
    }

    suspend fun sendMessage(text: String, chatId: String) = ApiFactory.telegramClient.post("/bot${Env["TG_BOT_KEY"]}/sendMessage"){
        url{
            parameters.append("TG_CHAT_ID", chatId)
            parameters.append("text", text)
            parameters.append("parse_mode", "HTML")
        }
    }

    private suspend fun sendPhoto(photoUrl: String, text: String, chatId: String) = ApiFactory.telegramClient.post("/bot${Env["TG_BOT_KEY"]}/sendPhoto") {
        parameter("TG_CHAT_ID", chatId)
        parameter("photo", photoUrl)
        parameter("caption", text)
        parameter("parse_mode", "HTML")
    }

}
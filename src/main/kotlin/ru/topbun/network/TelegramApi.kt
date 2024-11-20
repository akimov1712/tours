package ru.topbun.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.topbun.utills.Env

class TelegramApi(
    private val client: ApiFactory = ApiFactory
) {

    suspend fun sendMessageWithPhoto(text: String, photoUrl: String?, chatId: String){
        val response = if (photoUrl.isNullOrEmpty()) sendMessage(text, chatId) else sendPhoto(photoUrl, text, chatId)
        println("HTTP LOG: ${response.body<String>()}")
    }

    suspend fun sendMessage(text: String, chatId: String) = ApiFactory.telegramClient.post("/bot${Env["BOT_KEY"]}/sendMessage"){
        url{
            parameters.append("chat_id", chatId)
            parameters.append("text", text)
            parameters.append("parse_mode", "HTML")
        }
    }

    private suspend fun sendPhoto(photoUrl: String, text: String, chatId: String) = ApiFactory.telegramClient.post("/bot${Env["BOT_KEY"]}/sendPhoto") {
        parameter("chat_id", chatId)
        parameter("photo", photoUrl)
        parameter("caption", text)
        parameter("parse_mode", "HTML")
    }

}
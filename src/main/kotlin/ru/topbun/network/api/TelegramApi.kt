package ru.topbun.network.api

import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.topbun.network.ApiFactory
import ru.topbun.utills.Env

class TelegramApi(
    private val client: ApiFactory = ApiFactory
) {

    suspend fun sendMessageWithPhoto(text: String, photoUrl: String?, chatId: String){
        if (photoUrl.isNullOrEmpty()) sendMessage(text, chatId) else sendPhoto(photoUrl, text, chatId)
    }

    suspend fun sendMessage(text: String, chatId: String) = client.telegramClient.post("/bot${Env["TG_BOT_KEY"]}/sendMessage"){
        url{
            parameters.append("chat_id", chatId)
            parameters.append("text", text)
            parameters.append("parse_mode", "HTML")
        }
    }

    private suspend fun sendPhoto(photoUrl: String, text: String, chatId: String) = client.telegramClient.post("/bot${Env["TG_BOT_KEY"]}/sendPhoto") {
        url {
            parameters.append("chat_id", chatId)
            parameters.append("photo", photoUrl)
            parameters.append("caption", text)
            parameters.append("parse_mode", "HTML")
        }
    }

}
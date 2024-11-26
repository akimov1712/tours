package ru.topbun.network.api

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.topbun.network.ApiFactory

class VkApi(
    private val client: ApiFactory = ApiFactory
) {

    suspend fun sendMessageWithPhoto(text: String, photoUrl: String, groupId: String) {
        val attachment = photoUrl
        client.vkClient.get("/method/wall.post") {
            parameter("owner_id", groupId)
            parameter("message", text)
            parameter("parse_mode", "HTML")
            parameter("attachments", attachment)
            parameter("from_group", "1")
            parameter("random_id", System.currentTimeMillis())
        }
    }

}
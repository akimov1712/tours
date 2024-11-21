package ru.topbun.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.topbun.utills.Env

object ApiFactory {

    private val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }
        install(Logging) {
            level = LogLevel.BODY
        }

        defaultRequest {
            contentType(ContentType.Application.Json.withParameter("charset", "utf-8"))
        }
    }

    val toursClient = client.config {
        defaultRequest {
            url("http://tourvisor.ru")
            url {
                parameters.append("items", "100")
                parameters.append("format", "json")
                parameters.append("authlogin", Env["API_LOGIN"])
                parameters.append("authpass", Env["API_PASSWORD"])
            }
        }
    }

    val vkClient = client.config {
        defaultRequest {
            url("https://api.vk.com")
            url {
                parameters.append("access_token", Env["VK_BOT_KEY"])
                parameters.append("v", "5.199")
                parameters.append("owner_id", Env["VK_BOT_KEY"])
            }
        }
    }

    val telegramClient = client.config {
        defaultRequest {
            url("https://api.telegram.org")
        }
    }

}

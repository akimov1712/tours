package ru.topbun.models.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Config(
    val city: Int,
    val maxDays: Int,
    val countries: String?,
    val regions: String?,
    val operators: String?,
    val dateFrom: String?,
    val dateTo: String?,
    val stars: Int?,
    val rating: Int?,
    val meal: Int?,
){

    fun saveConfig(){
        val configText = Json.encodeToString(this)
        File("src/main/resources/config.json").writeText(configText)
    }

    companion object{

        fun getConfigFromResource(): Config {
            val file = File("src/main/resources/config.json").readText()
            return Json.decodeFromString<Config>(file)
        }

    }

}
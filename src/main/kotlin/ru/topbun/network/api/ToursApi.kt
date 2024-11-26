package ru.topbun.network.api

import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.topbun.models.config.Config
import ru.topbun.models.tours.HottourResponse
import ru.topbun.network.ApiFactory
import ru.topbun.toursadmin.models.meal.MealResponse

class ToursApi(
    private val client: ApiFactory = ApiFactory
) {

    suspend fun getMeals() = client.toursClient.post("/xml/list.php"){
        url{
            parameters.append("type", "meal")
        }
    }.body<MealResponse>()

    suspend fun getTours(config: Config) = client.toursClient.post("/xml/hottours.php"){
        url {
            parameters.append("city", config.city?.id ?: "1")
            parameters.append("maxDays", config.maxDays.toString())
            config.countries.let {
                val value = it.joinToString(",") { it.id }
                parameters.append("countries", value)
            }
            config.regions.let {
                val value = it.joinToString(",") { it.id }
                parameters.append("regions", value)
            }
            config.operators.let {
                val value = it.joinToString(",") { it.id }
                parameters.append("operators", value)
            }
            config.dateFrom?.let { parameters.append("dateFrom", it) }
            config.dateTo?.let { parameters.append("dateTo", it) }
            config.stars?.name?.firstOrNull()?.let { parameters.append("stars", it.toString()) }
            config.rating?.let { parameters.append("rating", it.toString()) }
            config.meal?.let { parameters.append("meal", it.id) }
        }
    }.body<HottourResponse>()

}
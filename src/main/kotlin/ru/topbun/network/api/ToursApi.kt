package ru.topbun.network.api

import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.topbun.models.config.Config
import ru.topbun.models.tours.HottourResponse
import ru.topbun.network.ApiFactory

class ToursApi(
    private val client: ApiFactory = ApiFactory
) {

    suspend fun getTours(config: Config) = ApiFactory.toursClient.post("/xml/hottours.php"){
        url {
            parameters.append("city", config.city.toString())
            parameters.append("maxDays", config.maxDays.toString())
            config.countries?.let { parameters.append("countries", it) }
            config.regions?.let { parameters.append("regions", it) }
            config.operators?.let { parameters.append("operators", it) }
            config.dateFrom?.let { parameters.append("dateFrom", it) }
            config.dateTo?.let { parameters.append("dateTo", it) }
            config.stars?.let { parameters.append("stars", it.toString()) }
            config.rating?.let { parameters.append("rating", it.toString()) }
            config.meal?.let { parameters.append("meal", it.toString()) }
        }
    }.body<HottourResponse>()

}
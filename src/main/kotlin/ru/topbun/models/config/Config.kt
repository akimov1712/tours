package ru.topbun.models.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.topbun.features.tours.TourScheduler
import ru.topbun.models.operatorToStock.OperatorToStock
import ru.topbun.toursadmin.models.city.City
import ru.topbun.toursadmin.models.country.Country
import ru.topbun.toursadmin.models.meal.Meal
import ru.topbun.toursadmin.models.operator.Operator
import ru.topbun.toursadmin.models.region.Region
import ru.topbun.toursadmin.models.stars.Star
import java.io.File

@Serializable
data class Config(
    val city: City?,
    val maxDays: Int,
    val countries: List<Country>,
    val regions: List<Region>,
    val operators: List<Operator>,
    val dateFrom: String?,
    val dateTo: String?,
    val stars: Star?,
    val rating: Float?,
    val meal: Meal?,
    val delayUniquePosts: Int,
    val delayPostingMinutes: Int,
    val domain: String,
    val stocks: List<OperatorToStock>
){

    fun saveConfig(){
        val configText = Json.encodeToString(this)
        File("config.json").writeText(configText)
        TourScheduler.updateInterval(this.delayPostingMinutes)
    }

    companion object{

        fun getConfigFromResource(): Config {
            val file = File("config.json").readText()
            return Json.decodeFromString<Config>(file)
        }

    }

}
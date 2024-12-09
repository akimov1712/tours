package ru.topbun.models.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.topbun.features.tours.TourSchedulerManager
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
    val title: String,
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
    val tgId: String = "",
    val vkId: String = "",
    val stocks: List<OperatorToStock>
){

    companion object{

        fun saveConfig(configs: List<Config>){
            val oldConfig = getConfigFromResource()
            val editableConfigs = configs.filter { !oldConfig.contains(it) }
            val configText = Json.encodeToString(configs)
            File("config.json").writeText(configText)
            editableConfigs.forEach { TourSchedulerManager.updateConfig(it) }

        }

        fun getConfigFromResource(): List<Config> {
            val file = File("config.json").readText()
            return Json.decodeFromString<List<Config>>(file)
        }

    }

}
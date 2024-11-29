package ru.topbun.utills

import ru.topbun.models.config.Config
import ru.topbun.models.meals.Meals
import ru.topbun.models.tours.HottourResponse
import ru.topbun.models.tours.TourDTO
import ru.topbun.models.tours.Tours
import ru.topbun.models.tours.Tours.date


fun List<List<TourDTO>>.getFilteredTours(): List<List<TourDTO>> =
    filter { group ->
        val delay = Config.getConfigFromResource().first { it.city?.name == group.first().departurename }.delayUniquePosts
        group.none {
            Tours.haveSuspension(it.tourid, delay)
        }
    }

fun HottourResponse.organizedTours(): List<List<TourDTO>> =
    hottours.tours.groupBy { it.countryname }.values.toList()

private fun List<TourDTO>.getNearTour(): TourDTO =
    minByOrNull { it.price } ?: throw NoSuchElementException("No tours available")

fun List<TourDTO>.getPreviewImage(): String = this.maxBy {
    it.hotelstars
}.also { println(it) }.hotelpicture

fun List<TourDTO>.buildMessageToTelegramPost(config: Config): String{
    val nearTour = this.getNearTour()
    val hotels = StringBuilder()
    val stocksBuilder = StringBuilder()
    val countryName = if (nearTour.countryname == "Россия") nearTour.hotelregionname else nearTour.countryname
    val operators = this.map { it.operatorname }.distinct()
    val groupByFlydate = this.groupBy { it.flydate }.map { it.value }.sortedBy { it.first().flydate }
    groupByFlydate.forEachIndexed { index, tour ->
        val flydateBuilder = StringBuilder()

        val date = tour.firstOrNull()?.flydate?.parseDate() ?: return@forEachIndexed

        val minNight = tour.minBy { it.nights }.nights + 1
        val maxNight = tour.maxBy { it.nights }.nights + 1
        val durationDays = if (minNight == maxNight) minNight.toString() else "$minNight - $maxNight"

        flydateBuilder.append("\uD83D\uDCC5 <b>${date.formatToDayWithMonth()} на $durationDays дней</b>\n")
        tour.forEach {
            val msg = "\uD83C\uDFE8 ${it.hotelname} (${it.hotelstars} ⭐) - ".capitalizeWords()
            val price = "<a href=\"${config.domain}/podbor-tura#tvtourid=${it.tourid}\">${formatPrice(it.price)} Руб</a>\n"
            flydateBuilder.append(msg + price)
        }
        if (groupByFlydate.size - 1 != index){
            flydateBuilder.append("\n")
        }
        hotels.append(flydateBuilder)
    }
    config.stocks.firstOrNull { it.operator.name == operators.first() }?.let { stocksBuilder.append("❤\uFE0F <b>Действует акция – ${it.stock}</b>") }
    return """
🔥 <b>$countryName из ${nearTour.departurenamefrom}</b> ✈️

🍽️ <b>Питание:</b> ${Meals.selectMeal(nearTour.meal).russian}

<b>Отели и цены:</b>

$hotels
$stocksBuilder

⚡️Цена за 1 человека при 2-местном размещении.

Обратная связь: @sanitravell
        """.trimIndent()
}

fun List<TourDTO>.buildMessageToVkPost(config: Config): String{
    val nearTour = this.getNearTour()
    val hotels = StringBuilder()
    val stocksBuilder = StringBuilder()
    val operators = this.map { it.operatorname }.distinct()
    val countryName = if (nearTour.countryname == "Россия") nearTour.hotelregionname else nearTour.countryname
    val groupByFlydate = this.groupBy { it.flydate }.map { it.value }.sortedBy { it.first().flydate }
    groupByFlydate.forEachIndexed { index, tour ->
        val flydateBuilder = StringBuilder()

        val date = tour.firstOrNull()?.flydate?.parseDate() ?: return@forEachIndexed

        val minNight = tour.minBy { it.nights }.nights + 1
        val maxNight = tour.maxBy { it.nights }.nights + 1
        val durationDays = if (minNight == maxNight) minNight.toString() else "$minNight - $maxNight"

        flydateBuilder.append("\uD83D\uDCC5 ${date.formatToDayWithMonth()} на $durationDays дней\n")
        tour.forEach {
            val msg = "\uD83C\uDFE8 ${it.hotelname} (${it.hotelstars} ⭐) - ".capitalizeWords()
            val price = "${formatPrice(it.price)} Руб\n"
            flydateBuilder.append(msg + price)
        }
        if (groupByFlydate.size - 1 != index){
            flydateBuilder.append("\n")
        }
        hotels.append(flydateBuilder)
    }
    config.stocks.firstOrNull { it.operator.name == operators.first() }?.let { stocksBuilder.append("❤\uFE0F Действует акция:${it.stock}") }
    return """
🔥 $countryName из ${nearTour.departurenamefrom} ✈️

🍽️ Питание: ${Meals.selectMeal(nearTour.meal).russian}

Отели и цены:

$hotels
$stocksBuilder

⚡️Цена за 1 человека при 2-местном размещении.
        """.trimIndent()
}
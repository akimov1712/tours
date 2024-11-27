package ru.topbun.utills

import ru.topbun.models.config.Config
import ru.topbun.models.meals.Meals
import ru.topbun.models.tours.HottourResponse
import ru.topbun.models.tours.TourDTO
import ru.topbun.models.tours.Tours


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
    val date = nearTour.flydate.parseDate()
    val hotels = StringBuilder()
    val stocksBuilder = StringBuilder()
    val countryName = if (nearTour.countryname == "Россия") nearTour.hotelregionname else nearTour.countryname
    val operators = this.map { it.operatorname }.distinct()
    if (operators.size == 1){
        config.stocks.firstOrNull { it.operator.name == operators.first() }?.let { stocksBuilder.append("Действует акция – ${it.stock}") }
    } else {
        val groupTours = this.groupBy { it.operatorname }
        groupTours.forEach { tourGroup ->
            val stockText = config.stocks.firstOrNull { it.operator.name == tourGroup.value.first().operatorname }
            if (stockText != null){
                val forHotels = tourGroup.value.map { it.hotelname }.joinToString(", ").capitalizeWords()
                stocksBuilder.append("Действует акция для <b>$forHotels</b> – ${stockText.stock}\n\n")
            }
        }
    }
    this.take(15).forEach {
        val msg = "\uD83C\uDFE8 ${it.hotelname} (${it.hotelstars} ⭐) - ".capitalizeWords()
        val price = "<a href=\"${config.domain}/podbor-tura#tvtourid=${it.tourid}\">${formatPrice(it.price)} Руб</a>\n"
        hotels.append(msg + price)
    }
    return """
🔥 <b>$countryName из ${nearTour.departurenamefrom}</b> ✈️

📅 <b>${date.formatToDayWithMonth()} на ${nearTour.nights + 1} дней</b>
🍽️ <b>Питание:</b> ${Meals.selectMeal(nearTour.meal).russian}

<b>Отели и цены:</b>
$hotels
${stocksBuilder.toString()}

⚡️Цена за 1 человека при 2-местном размещении.
        """.trimIndent()
}

fun List<TourDTO>.buildMessageToVkPost(config: Config): String{
    val nearTour = this.getNearTour()
    val date = nearTour.flydate.parseDate()
    val hotels = StringBuilder()
    val stocksBuilder = StringBuilder()
    val operators = this.map { it.operatorname }.distinct()
    val countryName = if (nearTour.countryname == "Россия") nearTour.hotelregionname else nearTour.countryname
    val stock = config.stocks.firstOrNull { it.operator.name == nearTour.operatorname }
    this.forEach {
        val msg = "\uD83C\uDFE8 ${it.hotelname} (${it.hotelstars} ⭐) - ".capitalizeWords()
        val price = "${formatPrice(it.price)} Руб\n"
        hotels.append(msg + price)
    }
    if (operators.size == 1){
        config.stocks.firstOrNull { it.operator.name == operators.first() }?.let { stocksBuilder.append("Действует акция – ${it.stock}") }
    } else {
        val groupTours = this.groupBy { it.operatorname }
        groupTours.forEach { tourGroup ->
            val stockText = config.stocks.firstOrNull { it.operator.name == tourGroup.value.first().operatorname }
            if (stockText != null){
                val forHotels = tourGroup.value.map { it.hotelname }.joinToString(", ").capitalizeWords()
                stocksBuilder.append("Действует акция для $forHotels – ${stockText.stock}\n\n")
            }
        }
    }
    return """
🔥 $countryName из ${nearTour.departurenamefrom} ✈️

📅 ${date.formatToDayWithMonth()} на ${nearTour.nights + 1} дней
🍽️ Питание: ${Meals.selectMeal(nearTour.meal).russian}

Отели и цены:
$hotels
${stocksBuilder.toString()}

⚡️Цена за 1 человека при 2-местном размещении.
        """.trimIndent()
}
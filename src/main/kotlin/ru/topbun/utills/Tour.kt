package ru.topbun.utills

import ru.topbun.models.meals.Meals
import ru.topbun.models.tours.HottourResponse
import ru.topbun.models.tours.TourDTO
import ru.topbun.models.tours.Tours


fun List<List<TourDTO>>.getFilteredTours(): List<List<TourDTO>> =
    filter { group -> group.none { Tours.haveSuspension(it.tourid) } }

fun HottourResponse.organizedTours(): List<List<TourDTO>> =
    hottours.tours.groupBy { it.countryname }.values.toList()

private fun List<TourDTO>.getNearTour(): TourDTO =
    minByOrNull { it.price } ?: throw NoSuchElementException("No tours available")

fun List<TourDTO>.getPreviewImage(): String = this.maxBy {
    it.hotelstars
}.also { println(it) }.hotelpicture

fun List<TourDTO>.buildMessageToPost(): String{
    val nearTour = this.getNearTour()
    val date = nearTour.flydate.parseDate()
    val hotels = StringBuilder()
    val countryName = if (nearTour.countryname == "Россия") nearTour.hotelregionname else nearTour.countryname
    this.forEach {
        val msg = "\uD83C\uDFE8 ${it.hotelname} (${it.hotelstars} ⭐) - ".capitalizeWords()
        val price = "<a href=\"https://tyrmarket.ru/podbor-tura#tvtourid=${it.tourid}\">${formatPrice(it.price)} Руб</a>\n"
        hotels.append(msg + price)
    }
    return """
🔥 $countryName из ${nearTour.departurenamefrom} ✈️

📅 ${date.formatToDayWithMonth()} на ${nearTour.nights + 1} дней
🍽️ Питание: ${Meals.selectMeal(nearTour.meal).russian}

🏨 Отели и цены:
$hotels

⚡️Цена за 1 человека при 2-местном размещении.
        """.trimIndent()
}
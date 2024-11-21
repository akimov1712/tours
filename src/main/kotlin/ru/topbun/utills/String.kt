package ru.topbun.utills

import kotlinx.datetime.toKotlinLocalDate
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun String.parseDate(): kotlinx.datetime.LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return LocalDate.parse(this, formatter).toKotlinLocalDate()
}

fun String.capitalizeWords(): String {
    return this.split(" ")
        .joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
}


fun formatPrice(price: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale("ru"))
    return formatter.format(price)
}

fun String.buildImageLink() = "https:" + this.replace("small", "main400")
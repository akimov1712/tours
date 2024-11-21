package ru.topbun.utills

import kotlinx.datetime.LocalDate

fun LocalDate.formatToDayWithMonth() = String.format("%02d.%02d", this.dayOfMonth, this.monthNumber)
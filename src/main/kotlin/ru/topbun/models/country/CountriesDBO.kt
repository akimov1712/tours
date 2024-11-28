package ru.topbun.models.country

import org.jetbrains.exposed.dao.id.IntIdTable

object CountriesDBO: IntIdTable() {
    val countryId = integer("countryId")
    val name = text("name")
}
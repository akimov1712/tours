package ru.topbun.models.city

import org.jetbrains.exposed.dao.id.IntIdTable

object CitiesDBO: IntIdTable() {

    val cityId = integer("cityId")
    val name = text("name")
    val namefrom = text("namefrom")

}
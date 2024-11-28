package ru.topbun.models.region

import org.jetbrains.exposed.dao.id.IntIdTable

object RegionsDBO: IntIdTable() {
    val regionId = integer("regionId")
    val configId = integer("configId")
    val country = text("country")
    val name = text("name")
}
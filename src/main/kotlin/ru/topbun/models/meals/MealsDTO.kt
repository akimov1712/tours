package ru.topbun.models.meals

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealsDTO (
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("fullname") val fullname: String,
    @SerialName("russian") val russian: String,
    @SerialName("russianfull") val russianfull: String,
)
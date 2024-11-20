package ru.topbun.models.tours

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HottoursDTO(
    @SerialName("hotcount") val hotcount: Int,
    @SerialName("tour") val tours: List<TourDTO>
)

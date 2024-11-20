package ru.topbun.models.tours

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HottourResponse(
    @SerialName("hottours") val hottours: HottoursDTO
)

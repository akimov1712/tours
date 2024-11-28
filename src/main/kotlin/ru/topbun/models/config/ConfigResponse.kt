package ru.topbun.models.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigResponse(
    val list: List<Config>
)

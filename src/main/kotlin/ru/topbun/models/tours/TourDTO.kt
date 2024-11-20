package ru.topbun.models.tours

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TourDTO(
    @SerialName("tourid") val tourid: Long,
    @SerialName("countrycode") val countrycode: Int,
    @SerialName("countryname") val countryname: String,
    @SerialName("departurecode") val departurecode: Int,
    @SerialName("departurename") val departurename: String,
    @SerialName("departurenamefrom") val departurenamefrom: String,
    @SerialName("operatorcode") val operatorcode: Int,
    @SerialName("operatorname") val operatorname:String,
    @SerialName("hotelcode") val hotelcode: Int,
    @SerialName("hotelname") val hotelname: String,
    @SerialName("hotelstars") val hotelstars: Int,
    @SerialName("hotelregioncode") val hotelregioncode: Int,
    @SerialName("hotelregionname") val hotelregionname: String,
    @SerialName("hotelrating") val hotelrating: Float,
    @SerialName("fulldesclink") val fulldesclink: String,
    @SerialName("hotelpicture") val hotelpicture: String,
    @SerialName("flydate") val flydate: String,
    @SerialName("nights") val nights: Int,
    @SerialName("meal") val meal: String,
    @SerialName("price") val price: Int,
    @SerialName("priceold") val priceold: Int,
    @SerialName("fuelcharge") val fuelcharge: Int,
    @SerialName("currency") val currency: String
)

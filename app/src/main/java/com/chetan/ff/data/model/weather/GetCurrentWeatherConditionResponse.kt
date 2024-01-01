package com.chetan.ff.data.model.weather

import com.google.gson.annotations.SerializedName


data class GetCurrentWeatherConditionResponse(
    @SerializedName("EpochTime")
    val epochTime: Int,
    @SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean,
    @SerializedName("IsDayTime")
    val isDayTime: Boolean,
    @SerializedName("Link")
    val link: String,
    @SerializedName("LocalObservationDateTime")
    val localObservationDateTime: String,
    @SerializedName("MobileLink")
    val mobileLink: String,
    @SerializedName("PrecipitationType")
    val precipitationType: Any?,
    @SerializedName("Temperature")
    val temperature: Temperature,
    @SerializedName("WeatherIcon")
    val weatherIcon: Int,
    @SerializedName("WeatherText")
    val weatherText: String
) {
    data class Temperature(
        @SerializedName("Imperial")
        val imperial: Imperial,
        @SerializedName("Metric")
        val metric: Metric
    ) {
        data class Imperial(
            @SerializedName("Unit")
            val unit: String,
            @SerializedName("UnitType")
            val unitType: Int,
            @SerializedName("Value")
            val value: Double
        )

        data class Metric(
            @SerializedName("Unit")
            val unit: String,
            @SerializedName("UnitType")
            val unitType: Int,
            @SerializedName("Value")
            val value: Double
        )
    }
}

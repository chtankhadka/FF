package com.chetan.ff.data.model.weather

import com.google.gson.annotations.SerializedName

data class WeatherUsingLatLngResponse(
    @SerializedName("base")
    val base: String = "",
    @SerializedName("clouds")
    val clouds: Clouds? ,
    @SerializedName("cod")
    val cod: Int = 0,
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("dt")
    val dt: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("timezone")
    val timezone: Int = 0,
    @SerializedName("visibility")
    val visibility: Int = 0,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    val wind: Wind
) {
    data class Clouds(
        @SerializedName("all")
        val all: Int = 0
    )

    data class Coord(
        @SerializedName("lat")
        val lat: Double = 0.0,
        @SerializedName("lon")
        val lon: Double = 0.0
    )

    data class Main(
        @SerializedName("feels_like")
        val feelsLike: Double= 0.0,
        @SerializedName("humidity")
        val humidity: Int = 0,
        @SerializedName("pressure")
        val pressure: Int = 0,
        @SerializedName("temp")
        val temp: Double = 0.0,
        @SerializedName("temp_max")
        val tempMax: Double = 0.0,
        @SerializedName("temp_min")
        val tempMin: Double = 0.0
    )

    data class Sys(
        @SerializedName("country")
        val country: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("sunrise")
        val sunrise: Int = 0,
        @SerializedName("sunset")
        val sunset: Int = 0,
        @SerializedName("type")
        val type: Int = 0
    )

    data class Weather(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("main")
        val main: String = ""
    )

    data class Wind(
        @SerializedName("deg")
        val deg: Int = 0,
        @SerializedName("speed")
        val speed: Double = 0.0
    )
}
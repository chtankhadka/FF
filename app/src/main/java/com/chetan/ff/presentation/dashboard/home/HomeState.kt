package com.chetan.ff.presentation.dashboard.home

import com.chetan.ff.data.model.weather.WeatherUsingLatLngResponse

data class HomeState(
    val locationKey : WeatherUsingLatLngResponse = WeatherUsingLatLngResponse(
        clouds = WeatherUsingLatLngResponse.Clouds(),
        coord = WeatherUsingLatLngResponse.Coord(),
        main = WeatherUsingLatLngResponse.Main(),
        sys = WeatherUsingLatLngResponse.Sys(),
        weather = emptyList<WeatherUsingLatLngResponse.Weather>(),
        wind = WeatherUsingLatLngResponse.Wind()
    )
)

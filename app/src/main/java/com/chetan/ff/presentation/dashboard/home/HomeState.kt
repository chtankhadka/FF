package com.chetan.ff.presentation.dashboard.home

import android.content.Context
import android.location.LocationManager
import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse
import com.chetan.ff.data.model.weather.WeatherUsingLatLngResponse

data class HomeState(
    val locationInfo : WeatherUsingLatLngResponse = WeatherUsingLatLngResponse(
        clouds = WeatherUsingLatLngResponse.Clouds(),
        coord = WeatherUsingLatLngResponse.Coord(),
        main = WeatherUsingLatLngResponse.Main(),
        sys = WeatherUsingLatLngResponse.Sys(),
        weather = emptyList<WeatherUsingLatLngResponse.Weather>(),
        wind = WeatherUsingLatLngResponse.Wind()
    ),
    val ffLocations: List<UpdateStatusRequestResponse> = emptyList(),
)

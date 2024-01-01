package com.chetan.ff.domain.repository

import com.chetan.ff.data.model.Resource
import com.chetan.ff.data.model.weather.GetCurrentLocationKeyResponse
import com.chetan.ff.data.model.weather.GetCurrentWeatherConditionResponse
import com.chetan.ff.data.model.weather.WeatherUsingLatLngResponse
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherRepository {
    @POST("currentconditions/v1/{locationCode}")
    suspend fun getCurrentWeatherCondition(
        @Path("locationCode") locationCode: String,
        @Query("apikey") apiKey: String
    ): List<GetCurrentWeatherConditionResponse>

    @POST("data/2.5/weather")
    suspend fun getCurrentLocationKey(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String
    ): WeatherUsingLatLngResponse
}
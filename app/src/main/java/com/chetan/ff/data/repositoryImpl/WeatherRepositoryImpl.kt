package com.chetan.ff.data.repositoryImpl

import androidx.compose.runtime.mutableStateOf
import com.chetan.ff.data.model.Resource
import com.chetan.ff.data.model.weather.GetCurrentLocationKeyResponse
import com.chetan.ff.data.model.weather.GetCurrentWeatherConditionResponse
import com.chetan.ff.data.model.weather.WeatherUsingLatLngResponse
import com.chetan.ff.domain.repository.WeatherRepository
import retrofit2.HttpException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRepository: WeatherRepository
) : WeatherRepository {
    override suspend fun getCurrentWeatherCondition(
        locationCode: String,
        apiKey: String
    ): List<GetCurrentWeatherConditionResponse> {
        val result = mutableListOf<GetCurrentWeatherConditionResponse>()
        return try {
            weatherRepository.getCurrentWeatherCondition(locationCode, apiKey).forEach {
                result.add(it)
            }
            result
        }catch (e: Exception){
            e.printStackTrace()
            result
        }
    }

    override suspend fun getCurrentLocationKey(
        lat: String,
        lon: String,
        appid: String
    ): WeatherUsingLatLngResponse{
        return weatherRepository.getCurrentLocationKey(lat,lon,appid)

    }


}
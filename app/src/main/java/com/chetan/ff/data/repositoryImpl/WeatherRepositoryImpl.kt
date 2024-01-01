package com.chetan.ff.data.repositoryImpl

import com.chetan.ff.data.model.weather.WeatherUsingLatLngResponse
import com.chetan.ff.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRepository: WeatherRepository
) : WeatherRepository {
    override suspend fun getCurrentLocationKey(
        lat: String,
        lon: String,
        appid: String
    ): WeatherUsingLatLngResponse{
        return weatherRepository.getCurrentLocationKey(lat,lon,appid)

    }


}
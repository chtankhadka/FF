package com.chetan.ff.presentation.dashboard.home

sealed interface HomeEvent {
    data class GetLocationWeather(val key: String): HomeEvent

}
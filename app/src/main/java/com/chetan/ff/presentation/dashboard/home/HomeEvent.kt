package com.chetan.ff.presentation.dashboard.home

sealed interface HomeEvent {
    data class GetLocationWeatherInfo(val latInfo: String, val logInfo: String): HomeEvent
    data class AudioProfile(val audio: String,val battery: String): HomeEvent

}
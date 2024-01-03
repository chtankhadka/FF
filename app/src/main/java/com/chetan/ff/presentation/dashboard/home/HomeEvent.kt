package com.chetan.ff.presentation.dashboard.home

import android.net.Uri

sealed interface HomeEvent {
    data class GetLocationWeatherInfo(val latInfo: String, val logInfo: String): HomeEvent

}
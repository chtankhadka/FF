package com.chetan.ff.presentation.ourlocations

import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse

data class OurLocationsState(
    val data: String = "",
    val ffLocations: List<UpdateStatusRequestResponse> = emptyList(),
    val ffusers: List<UpdateStatusRequestResponse> = emptyList()
)

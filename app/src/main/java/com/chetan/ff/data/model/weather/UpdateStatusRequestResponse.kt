package com.chetan.ff.data.model.weather

data class UpdateStatusRequestResponse(
    val id: String = "",
    val temperature : String = "",
    val ress: String = "",
    val country: String = "",
    val date: String = "",
    val weather: String = "",
    val group: String = "",
    val userProfile: String = "",
    val oneSignalId: String = "",
    val audioProfile: String = "",
    val batteryLife: String = ""
)

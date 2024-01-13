package com.chetan.ff.data.repositoryImpl

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.PushNotificationRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OneSignalRepository {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Authorization: Basic NzE5NThkOGYtMTc1ZC00OTQ1LTk3NTItZWY0ODM1NDUyYjdl"
    )
    @POST("notifications")
    suspend fun pushNotification(
        @Body requestBody: PushNotificationRequest
    ): Resource<Boolean>
}
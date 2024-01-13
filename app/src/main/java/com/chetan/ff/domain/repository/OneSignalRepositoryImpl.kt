package com.chetan.ff.domain.repository

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.PushNotificationRequest
import com.chetan.ff.data.repositoryImpl.OneSignalRepository
import retrofit2.HttpException
import javax.inject.Inject

class OneSignalRepositoryImpl @Inject constructor(
    private val oneSignal: OneSignalRepository
) : OneSignalRepository {
    override suspend fun pushNotification(requestBody: PushNotificationRequest): Resource<Boolean> {
        return try {
            oneSignal.pushNotification(requestBody)
            Resource.Success(true)
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Failure(e)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}
package com.chetan.ff.domain.repository

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse

interface FirestoreRepository{

    suspend fun updateStatus(
        data: UpdateStatusRequestResponse
    ): Resource<Boolean>

    suspend fun getAllStatus(
        group: String
    ) : Resource<List<UpdateStatusRequestResponse>>


}
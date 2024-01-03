package com.chetan.ff.domain.repository

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.ImageStorageDetails

interface FDBRepository {
    suspend fun insertImage(data: ImageStorageDetails) : Resource<Pair<String, String>>
}
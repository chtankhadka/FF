package com.chetan.ff.domain.repository

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.CommentRequestResponse
import com.chetan.ff.data.model.RealtimeModelResponse
import kotlinx.coroutines.flow.Flow

interface RealtimeRepository {
    suspend fun insert(
        item: CommentRequestResponse
    ) : Resource<String>

    fun getItems(data: CommentRequestResponse) : Flow<Resource<List<RealtimeModelResponse>>>
}
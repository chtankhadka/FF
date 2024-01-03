package com.chetan.ff.domain.use_cases.realtime

import com.chetan.ff.data.model.CommentRequestResponse
import com.chetan.ff.data.model.RealtimeModelResponse
import com.chetan.ff.domain.repository.RealtimeRepository
import javax.inject.Inject

class Insert @Inject constructor(
    private val realtimeRepository: RealtimeRepository
) {
    suspend operator fun invoke(data: CommentRequestResponse) = realtimeRepository.insert(data)
}
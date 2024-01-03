package com.chetan.ff.domain.use_cases.realtime

import com.chetan.ff.data.model.CommentRequestResponse
import com.chetan.ff.domain.repository.RealtimeRepository
import javax.inject.Inject

class GetItems @Inject constructor(
    private val realtimeRepository: RealtimeRepository
) {

    operator fun invoke(data: CommentRequestResponse) = realtimeRepository.getItems(data)
}
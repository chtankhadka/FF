package com.chetan.ff.domain.use_cases.firestore

import com.chetan.ff.data.model.StoriesDetailRequestResponse
import com.chetan.ff.domain.repository.FirestoreRepository
import javax.inject.Inject

class SetStories @Inject constructor(
    private val frepository: FirestoreRepository
) {
    suspend operator fun invoke(data: StoriesDetailRequestResponse) = frepository.setStories(data)
}
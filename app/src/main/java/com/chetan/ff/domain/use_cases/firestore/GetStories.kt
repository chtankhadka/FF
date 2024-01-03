package com.chetan.ff.domain.use_cases.firestore

import com.chetan.ff.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetStories @Inject constructor(
    private val frepository: FirestoreRepository
) {
    suspend operator fun invoke(group: String) = frepository.getStories(group)
}
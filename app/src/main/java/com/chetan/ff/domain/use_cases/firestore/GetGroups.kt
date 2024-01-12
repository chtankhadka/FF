package com.chetan.ff.domain.use_cases.firestore

import com.chetan.ff.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetGroups @Inject constructor(
    private val frepository: FirestoreRepository
) {
    suspend operator fun invoke() = frepository.getGroups()
}
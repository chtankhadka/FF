package com.chetan.ff.domain.use_cases.firestore

import com.chetan.ff.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetStatus @Inject constructor(
    private val frepository: FirestoreRepository
) {
    suspend operator fun invoke(group: String) = frepository.getAllStatus(group)
}
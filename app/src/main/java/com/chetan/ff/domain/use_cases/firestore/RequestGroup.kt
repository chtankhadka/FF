package com.chetan.ff.domain.use_cases.firestore

import com.chetan.ff.data.model.RequestGroupDeatails
import com.chetan.ff.domain.repository.FirestoreRepository
import javax.inject.Inject

class RequestGroup @Inject constructor(
    private val frepository: FirestoreRepository
) {
    suspend operator fun invoke(data: RequestGroupDeatails) = frepository.requestGroup(data)
}
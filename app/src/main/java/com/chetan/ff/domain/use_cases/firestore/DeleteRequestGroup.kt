package com.chetan.ff.domain.use_cases.firestore

import com.chetan.ff.data.model.RequestGroupDeatails
import com.chetan.ff.domain.repository.FirestoreRepository
import javax.inject.Inject

class DeleteRequestGroup @Inject constructor(
    private val frepository: FirestoreRepository
) {
    suspend operator fun invoke(data: RequestGroupDeatails) = frepository.deleteRequestGroup(data)
}
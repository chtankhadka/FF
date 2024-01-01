package com.chetan.ff.domain.use_cases.firestore

import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse
import com.chetan.ff.domain.repository.FirestoreRepository
import javax.inject.Inject

class UpdateStatus @Inject constructor(
    private val frepository: FirestoreRepository
) {
    suspend operator fun invoke(data: UpdateStatusRequestResponse) = frepository.updateStatus(data)
}
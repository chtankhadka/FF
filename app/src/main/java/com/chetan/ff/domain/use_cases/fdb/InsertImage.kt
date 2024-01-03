package com.chetan.ff.domain.use_cases.fdb

import com.chetan.ff.data.model.ImageStorageDetails
import com.chetan.ff.domain.repository.FDBRepository
import javax.inject.Inject

class InsertImage @Inject constructor(
    private val repository: FDBRepository
) {
    suspend operator fun invoke(data: ImageStorageDetails) = repository.insertImage(data)
}
package com.chetan.ff.domain.use_cases.firestore

import com.chetan.ff.data.model.SetGetGroupsName
import com.chetan.ff.domain.repository.FirestoreRepository
import javax.inject.Inject

class SetGroup @Inject constructor(
    private val frepository: FirestoreRepository
) {
    suspend operator fun invoke(data: SetGetGroupsName) = frepository.setGroups(data)
}
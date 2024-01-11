package com.chetan.ff.data.repositoryImpl

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.ImageStorageDetails
import com.chetan.ff.domain.repository.FDBRepository
import com.chetan.orderdelivery.data.local.Preference
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FDBRepositoryImpl @Inject constructor(
    private val fStorage: FirebaseStorage,
    private val preference: Preference
) : FDBRepository {
    override suspend fun insertImage(data: ImageStorageDetails): Resource<Pair<String, String>> {
        return try {
            val imageRef = fStorage.reference.child("/groups/${preference.groupName?:"test"}/" + data.imageName)
            val uploadTask = imageRef.putFile(data.imageUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            val uploadImageInfo = Pair(data.imageName, downloadUrl.toString())
            Resource.Success(uploadImageInfo)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}
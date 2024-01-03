package com.chetan.ff.data.repositoryImpl

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.StoriesDetailRequestResponse
import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse
import com.chetan.ff.domain.repository.FirestoreRepository
import com.chetan.orderdelivery.data.local.Preference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val preference: Preference):  FirestoreRepository{
    override suspend fun updateStatus(data: UpdateStatusRequestResponse): Resource<Boolean> {
        return try {
            var status = true
            firestore.collection("ff")
                .document(data.group)
                .collection("weather")
                .document(data.id)
                .set(data)
                .addOnSuccessListener {
                    status = true
                }.addOnFailureListener {
                    status = false
                }.await()
            Resource.Success(status)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getAllStatus(group: String): Resource<List<UpdateStatusRequestResponse>> {
        return try {
            var data  = mutableListOf<UpdateStatusRequestResponse>()
            val documents = firestore.collection("ff")
                .document(group)
                .collection("weather")
                .get()
                .await()
            for(document in documents.documents){
                val item = document.toObject<UpdateStatusRequestResponse>()
                item?.let {
                    data.add(it)
                }
            }
            Resource.Success(data.sortedByDescending { it.date })
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getStories(group: String): Resource<List<StoriesDetailRequestResponse>> {
        return try {
            var data  = mutableListOf<StoriesDetailRequestResponse>()
            val documents = firestore.collection("ff")
                .document(group)
                .collection("stories")
                .get()
                .await()
            for(document in documents.documents){
                val item = document.toObject<StoriesDetailRequestResponse>()
                item?.let {
                    data.add(it)
                }
            }
            Resource.Success(data.sortedByDescending { it.time })
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun setStories(data: StoriesDetailRequestResponse): Resource<Boolean> {
        return try {
            var status = true
            firestore.collection("ff")
                .document(data.group)
                .collection("stories")
                .document(data.imageId)
                .set(data)
                .addOnSuccessListener {
                    status = true
                }.addOnFailureListener {
                    status = false
                }.await()
            Resource.Success(status)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}
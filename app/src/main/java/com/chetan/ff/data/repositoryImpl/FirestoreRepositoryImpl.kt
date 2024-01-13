package com.chetan.ff.data.repositoryImpl

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.RequestGroupDeatails
import com.chetan.ff.data.model.SetGetGroupsName
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
    private val preference: Preference
) : FirestoreRepository {
    override suspend fun updateStatus(data: UpdateStatusRequestResponse): Resource<Boolean> {
        return try {
            var status = true
            firestore.collection("ff")
                .document(preference.groupName ?: "test")
                .collection("weather")
                .document(data.id)
                .set(data)
                .addOnSuccessListener {
                    status = true
                }.addOnFailureListener {
                    status = false
                }.await()
            Resource.Success(status)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getAllStatus(): Resource<List<UpdateStatusRequestResponse>> {
        return try {
            var data = mutableListOf<UpdateStatusRequestResponse>()
            val documents = firestore.collection("ff")
                .document(preference.groupName ?: "test")
                .collection("weather")
                .get()
                .await()
            for (document in documents.documents) {
                val item = document.toObject<UpdateStatusRequestResponse>()
                item?.let {
                    data.add(it)
                }
            }
            Resource.Success(data.sortedByDescending { it.date })
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getStories(): Resource<List<StoriesDetailRequestResponse>> {
        return try {
            var data = mutableListOf<StoriesDetailRequestResponse>()
            val documents = firestore.collection("ff")
                .document(preference.groupName ?: "test")
                .collection("stories")
                .get()
                .await()
            for (document in documents.documents) {
                val item = document.toObject<StoriesDetailRequestResponse>()
                item?.let {
                    data.add(it)
                }
            }
            Resource.Success(data.sortedByDescending { it.time })
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun setStories(data: StoriesDetailRequestResponse): Resource<Boolean> {
        return try {
            var status = true
            firestore.collection("ff")
                .document(preference.groupName ?: "test")
                .collection("stories")
                .document(data.userId)
                .set(data)
                .addOnSuccessListener {
                    status = true
                }.addOnFailureListener {
                    status = false
                }.await()
            Resource.Success(status)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateCommentedUserInStories(data: StoriesDetailRequestResponse): Resource<Boolean> {
        return try {
            firestore.collection("ff")
                .document(preference.groupName ?: "test")
                .collection("stories")
                .document(data.userId)
                .update("cmtUserProfile", data.cmtUserProfile)
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun setGroups(data: SetGetGroupsName): Resource<Boolean> {
        return try {
            var status = true
            firestore.collection("ff")
                .document(data.tableName)
                .collection("groups")
                .document(data.groupName)
                .set(data)
                .addOnSuccessListener {
                    status = true
                }.addOnFailureListener {
                    status = false
                }.await()
            Resource.Success(status)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getGroups(): Resource<List<SetGetGroupsName>> {
        return try {
            var data = mutableListOf<SetGetGroupsName>()
            val documents = firestore.collection("ff")
                .document(preference.tableName ?: "test")
                .collection("groups")
                .get()
                .await()
            for (document in documents.documents) {
                val item = document.toObject<SetGetGroupsName>()
                item?.let {
                    data.add(it)
                }
            }
            Resource.Success(data.sortedBy { it.groupName })
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun requestGroup(data: RequestGroupDeatails): Resource<Boolean> {
        return try {
            var status = true
            firestore.collection("ff")
                .document(data.groupAdmin)
                .collection("groupsRequests")
                .document(data.tableName)
                .set(data)
                .addOnSuccessListener {
                    status = true
                }.addOnFailureListener {
                    status = false
                }.await()
            Resource.Success(status)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deleteRequestGroup(data: RequestGroupDeatails): Resource<Boolean> {
        return try {
            var status = true
            firestore.collection("ff")
                .document(preference.tableName?:"test")
                .collection("groupsRequests")
                .document(data.tableName)
                .delete()
                .addOnSuccessListener {
                    status = true
                }.addOnFailureListener {
                    status = false
                }.await()
            Resource.Success(status)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getRequestGroup(): Resource<List<RequestGroupDeatails>> {
        return try {
            var data = mutableListOf<RequestGroupDeatails>()
            val documents = firestore.collection("ff")
                .document(preference.tableName ?: "test")
                .collection("groupsRequests")
                .get()
                .await()
            for (document in documents.documents) {
                val item = document.toObject<RequestGroupDeatails>()
                item?.let {
                    data.add(it)
                }
            }
            Resource.Success(data.sortedBy { it.groupName })
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }


}
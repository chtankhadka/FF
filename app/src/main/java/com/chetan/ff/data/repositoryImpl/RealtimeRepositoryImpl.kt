package com.chetan.ff.data.repositoryImpl

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.CommentRequestResponse
import com.chetan.ff.data.model.RealtimeModelResponse
import com.chetan.ff.domain.repository.RealtimeRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RealtimeRepositoryImpl @Inject constructor(
    private val realtime: FirebaseDatabase
) : RealtimeRepository {

    override suspend fun insert(item: CommentRequestResponse): Resource<String> {
        return try {
            realtime.getReference("comments/${item.group}").child(item.msgId).push().setValue(item).await()
            Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun getItems(data: CommentRequestResponse): Flow<Resource<List<RealtimeModelResponse>>> = callbackFlow {
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map {
                    RealtimeModelResponse(
                        item = it.getValue(CommentRequestResponse::class.java)?:CommentRequestResponse(),
                        key = it.key?:""
                    )
                }
                trySend(Resource.Success(items))
                println(items)
            }
            override fun onCancelled(error: DatabaseError) {
                Resource.Failure(error.toException())
            }

        }
        realtime.getReference("comments/${data.group}").child(data.msgId).addValueEventListener(valueEvent)
        awaitClose {
            realtime.getReference("comments/${data.group}").child(data.msgId).removeEventListener(valueEvent)
            close()
        }

    }

}
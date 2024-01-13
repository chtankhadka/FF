package com.chetan.ff.domain.repository

import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.RequestGroupDeatails
import com.chetan.ff.data.model.SetGetGroupsName
import com.chetan.ff.data.model.StoriesDetailRequestResponse
import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse

interface FirestoreRepository{

    suspend fun updateStatus(
        data: UpdateStatusRequestResponse
    ): Resource<Boolean>

    suspend fun getAllStatus(
    ) : Resource<List<UpdateStatusRequestResponse>>
    suspend fun getStories(
    ) : Resource<List<StoriesDetailRequestResponse>>
    suspend fun setStories(
        data: StoriesDetailRequestResponse
    ): Resource<Boolean>

    suspend fun updateCommentedUserInStories(
        data: StoriesDetailRequestResponse
    ): Resource<Boolean>

    suspend fun setGroups(data: SetGetGroupsName): Resource<Boolean>
    suspend fun getGroups(
    ): Resource<List<SetGetGroupsName>>
    suspend fun requestGroup(
            data: RequestGroupDeatails
    ): Resource<Boolean>
    suspend fun deleteRequestGroup(data: RequestGroupDeatails): Resource<Boolean>
    suspend fun getRequestGroup(
    ): Resource<List<RequestGroupDeatails>>

}
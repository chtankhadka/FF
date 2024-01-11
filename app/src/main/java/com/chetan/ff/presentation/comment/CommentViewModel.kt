package com.chetan.ff.presentation.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.CommentRequestResponse
import com.chetan.ff.data.model.StoriesDetailRequestResponse
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.ff.domain.use_cases.realtime.RealtimeUseCases
import com.chetan.ff.utils.MyDate
import com.chetan.orderdelivery.data.local.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val realtimeUseCases: RealtimeUseCases,
    private val preference: Preference
) : ViewModel() {
    private val _state = MutableStateFlow(CommentState())
    val state: StateFlow<CommentState> = _state

    init {
        _state.update {
            it.copy(
                userName = preference.userName?:"test",
                tableName = preference.tableName?:"test"
            )
        }
    }

    val onEvent: (event: CommentEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                is CommentEvent.OnMsgChange -> {
                    _state.update {
                        it.copy(
                            userMsg = event.value
                        )
                    }
                }
                is CommentEvent.GetCmtHistories -> {
                    try {
                        realtimeUseCases.getItems(
                            data = CommentRequestResponse(
                                msgId = event.imgId,
                                group = preference.groupName?:"test")
                        ).collect{liveData ->
                            when(liveData){
                                is Resource.Failure -> {
                                }
                                Resource.Loading -> {
                                }
                                is Resource.Success -> {
                                    println(liveData.data)
                                    _state.update {
                                        it.copy(
                                            cmtList = liveData.data,
                                            imgId = event.imgId
                                        )
                                    }
                                }
                            }

                        }

                    }catch (e: Exception){
                     e.printStackTrace()
                    }
                }
                CommentEvent.SetChatHistory -> {
                    val test = realtimeUseCases.insert(data = CommentRequestResponse(
                        cmt = state.value.userMsg,
                        msgId = state.value.imgId,
                        cmtUser = preference.userName ?: "test",
                        time = MyDate.CurrentDateTimeSDF(),
                        group = preference.groupName?:"test"

                    ))
                    _state.update {
                        it.copy(
                            userMsg = "",
                            newMsgSent = true
                        )
                    }
                }

                is CommentEvent.UpdateStories -> {
                    firestoreUseCases.updateCommentedUserInStories(
                        data = StoriesDetailRequestResponse(
                            group = preference.groupName?:"test",
                            userId = event.value,
                            cmtUserProfile = preference.gmailProfile?:"test"
                        )
                    )
                }
            }
        }
    }
}
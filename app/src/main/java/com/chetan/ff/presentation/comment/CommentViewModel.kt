package com.chetan.ff.presentation.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.ff.data.model.CommentRequestResponse
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
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
    private val preference: Preference
) : ViewModel() {
    private val _state = MutableStateFlow(CommentState())
    val state: StateFlow<CommentState> = _state

    init {
        _state.update {
            it.copy(
                userName = preference.userName?:"test"
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

                CommentEvent.SetChatHistory -> {
                    _state.update {
                        it.copy(
                            cmtList = state.value.cmtList + CommentRequestResponse(
                                cmt = state.value.userMsg,
                                msgId = preference.tableName ?: "test",
                                cmtUser = preference.userName ?: "test",
                                time = MyDate.CurrentDateTimeSDF(),
                                group = "families"

                            ),
                            userMsg = ""
                        )
                    }
                }
            }
        }
    }
}
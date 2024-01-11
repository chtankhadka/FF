package com.chetan.ff.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.ff.R
import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.ImageStorageDetails
import com.chetan.ff.data.model.StoriesDetailRequestResponse
import com.chetan.ff.domain.use_cases.fdb.FDBUseCases
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.ff.presentation.dialogs.Message
import com.chetan.orderdelivery.data.local.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val fdbUseCases: FDBUseCases,
    private val preference: Preference
) : ViewModel(){
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state
    val onEvent: (event : DashboardEvent) -> Unit = {event ->
            viewModelScope.launch {
                when(event){
                    is DashboardEvent.UploadImage -> {
                        _state.update {
                            it.copy(
                                infoMsg = Message.Loading(
                                    lottieImage = R.raw.sync,
                                    yesNoRequired = false,
                                    isCancellable = false,
                                    title = "Loading",
                                    description = "Uploading your stories"
                                )
                            )
                        }

                        val requestUrl = fdbUseCases.insertImage(
                            data = ImageStorageDetails(
                                imageUri = event.value, imageName = preference.tableName?:"test"
                            )
                        )
                        when (requestUrl) {
                            is Resource.Failure -> {}
                            Resource.Loading -> {}
                            is Resource.Success -> {
                                val setStory = firestoreUseCases.setStories(
                                    data = StoriesDetailRequestResponse(
                                        userId = preference.tableName?:"test",
                                        imageUrl = requestUrl.data.second,
                                        time = System.currentTimeMillis().toString(),
                                        group = preference.groupName?:"test"
                                    )
                                )
                                when(setStory){
                                    is Resource.Failure -> {

                                    }
                                    Resource.Loading -> {

                                    }
                                    is Resource.Success -> {
                                        _state.update {
                                            it.copy(
                                                infoMsg = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    DashboardEvent.DismissInfoMsg -> {
                        _state.update {
                            it.copy(
                                infoMsg = null
                            )
                        }
                    }

                    is DashboardEvent.SetGroupName -> {
                        preference.groupName = event.value
                        _state.update {
                            it.copy(
                                groupName = event.value
                            )
                        }
                    }
                    is DashboardEvent.OnGroupNameChange -> {
                        _state.update {
                            it.copy(
                                onChangeGroupName = event.value
                            )
                        }
                    }
                }
            }
    }

}
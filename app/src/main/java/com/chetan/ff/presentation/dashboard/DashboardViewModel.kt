package com.chetan.ff.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.ff.R
import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.ImageStorageDetails
import com.chetan.ff.data.model.StoriesDetailRequestResponse
import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse
import com.chetan.ff.data.repositoryImpl.WeatherRepositoryImpl
import com.chetan.ff.domain.use_cases.fdb.FDBUseCases
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.ff.presentation.dashboard.home.HomeEvent
import com.chetan.ff.presentation.dialogs.Message
import com.chetan.orderdelivery.data.local.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val fdbUseCases: FDBUseCases,
    private val preference: Preference
) : ViewModel(){
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state
    init {

    }
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
                                imageUri = event.value, imagePath = "/groups/families/", imageName = preference.tableName?:"test"
                            )
                        )
                        when (requestUrl) {
                            is Resource.Failure -> {}
                            Resource.Loading -> {}
                            is Resource.Success -> {
                                val setStory = firestoreUseCases.setStories(
                                    data = StoriesDetailRequestResponse(
                                        imageId = preference.tableName?:"test",
                                        imageUrl = requestUrl.data.second,
                                        time = LocalDateTime.now().toString(),
                                        group = "families"
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
                }
            }
    }

}
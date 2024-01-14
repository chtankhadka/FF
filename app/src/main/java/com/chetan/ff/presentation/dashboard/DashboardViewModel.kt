package com.chetan.ff.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.ff.R
import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.ImageStorageDetails
import com.chetan.ff.data.model.PushNotificationRequest
import com.chetan.ff.data.model.RequestGroupDeatails
import com.chetan.ff.data.model.SetGetGroupsName
import com.chetan.ff.data.model.StoriesDetailRequestResponse
import com.chetan.ff.data.repositoryImpl.OneSignalRepository
import com.chetan.ff.domain.use_cases.fdb.FDBUseCases
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.ff.presentation.dialogs.Message
import com.chetan.orderdelivery.data.local.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val fdbUseCases: FDBUseCases,
    private val preference: Preference,
    private val oneSignalRepository: OneSignalRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state

    init {
        getGroups()
        getRequestGroups()
    }

    val onEvent: (event: DashboardEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
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
                            imageUri = event.value, imageName = preference.tableName ?: "test"
                        )
                    )
                    when (requestUrl) {
                        is Resource.Failure -> {}
                        Resource.Loading -> {}
                        is Resource.Success -> {
                            val setStory = firestoreUseCases.setStories(
                                data = StoriesDetailRequestResponse(
                                    userId = preference.tableName ?: "test",
                                    imageUrl = requestUrl.data.second,
                                    time = System.currentTimeMillis().toString(),
                                    group = preference.groupName ?: "test"
                                )
                            )
                            when (setStory) {
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

                    val groupCreated = firestoreUseCases.setGroup(
                        data = SetGetGroupsName(
                            groupName = event.value,
                            groupCreated = preference.userName ?: "test",
                            tableName = preference.tableName ?: "test",
                            createdTime = LocalDateTime.now().toString()
                        )
                    )
                    when (groupCreated) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            preference.groupName = event.value
                            _state.update {
                                it.copy(
                                    groupName = event.value
                                )
                            }
                        }
                    }


                }

                is DashboardEvent.OnGroupNameChange -> {
                    _state.update {
                        it.copy(
                            onChangeGroupName = event.value
                        )
                    }
                }

                is DashboardEvent.OnChangeAdminGmail -> {
                    _state.update {
                        it.copy(
                            onChangeAdminGmail = event.value
                        )
                    }
                }

                is DashboardEvent.SendGroupRequest -> {
                    firestoreUseCases.requestGroup(
                        data = RequestGroupDeatails(
                            groupAdmin = event.groupAdmin.split("@").get(0)
                                .replace(Regex("[^A-Za-z0-9 ]"), ""),
                            groupName = event.groupName,
                            tableName = preference.tableName ?: "test",
                            groupRequested = preference.userName ?: "test"
                        )
                    )
                }

                is DashboardEvent.ChangeMyGroup -> {
                    preference.groupName = event.value
                    _state.update {
                        it.copy(
                            groupName = event.value
                        )
                    }
                }

                is DashboardEvent.AcceptGroupRequest -> {
                    val groupCreated = firestoreUseCases.setGroup(
                        data = SetGetGroupsName(
                            groupName = event.data.groupName,
                            groupCreated = event.data.groupAdmin,
                            tableName = event.data.tableName,
                            createdTime = LocalDateTime.now().toString()
                        )
                    )
                    when (groupCreated) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            firestoreUseCases.deleteRequestGroup(
                                data = RequestGroupDeatails(
                                    groupAdmin = event.data.groupAdmin,
                                    groupName = event.data.groupName,
                                    groupRequested = event.data.groupRequested,
                                    tableName = event.data.tableName
                                )
                            )
                            _state.update {
                                it.copy(
                                    groupRequestList = _state.value.groupRequestList.filterNot { it.tableName == event.data.tableName }
                                )
                            }
                        }
                    }
                }

                is DashboardEvent.DeleteRequestGroup -> {
                    val requestDelete = firestoreUseCases.deleteRequestGroup(
                        data = RequestGroupDeatails(
                            groupAdmin = event.data.groupAdmin,
                            groupName = event.data.groupName,
                            groupRequested = event.data.groupRequested,
                            tableName = event.data.tableName
                        )
                    )
                    when (requestDelete) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    groupRequestList = _state.value.groupRequestList.filterNot { it.tableName == event.data.tableName }
                                )
                            }
                        }
                    }
                }

                DashboardEvent.GetStatusNow -> {
                    val oneSignalList = firestoreUseCases.getStatus()
                    when (oneSignalList) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            try {
                                val sendNotice = oneSignalRepository.pushNotification(
                                    PushNotificationRequest(
                                        contents = mapOf("en" to "Wants your Status"),
                                        headings = mapOf("en" to preference.userName.toString()),
                                        include_player_ids = oneSignalList.data.map { it.oneSignalId }
                                    )
                                )
                                when (sendNotice) {
                                    is Resource.Failure -> {

                                    }

                                    Resource.Loading -> {

                                    }

                                    is Resource.Success -> {

                                    }
                                }
                            } catch (e: HttpException) {

                                e.printStackTrace()
                            } catch (e: Throwable) {
                                e.printStackTrace()
                            }
                        }
                    }

                }
            }
        }
    }

    fun getGroups() {
        viewModelScope.launch {
            val groupsName = firestoreUseCases.getGroups()
            when (groupsName) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            groupList = groupsName.data
                        )
                    }
                }
            }
        }


    }

    fun getRequestGroups() {
        viewModelScope.launch {
            val groupsName = firestoreUseCases.getRequestGroup()
            when (groupsName) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            groupRequestList = groupsName.data
                        )
                    }
                }
            }
        }


    }

}
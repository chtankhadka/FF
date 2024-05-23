package com.chetan.ff.presentation.ourlocations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OurLocationsViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
): ViewModel() {

    private val _state = MutableStateFlow(OurLocationsState())
    val state: StateFlow<OurLocationsState> = _state
    init {
        getStatus()
    }

    private fun getStatus(){
        viewModelScope.launch {
            val data = firestoreUseCases.getStatus()
            when(data){
                is Resource.Failure -> {

                }
                Resource.Loading -> {

                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            ffLocations = data.data,
                            ffusers = listOf(
                                UpdateStatusRequestResponse(),
                                UpdateStatusRequestResponse(),
                                UpdateStatusRequestResponse(id = "all"))
                                    + data.data + listOf(
                                UpdateStatusRequestResponse(),
                                UpdateStatusRequestResponse())
                        )
                    }
                }
            }

        }
    }
}
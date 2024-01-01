package com.chetan.ff.presentation.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.ff.data.Resource
import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse
import com.chetan.ff.data.repositoryImpl.WeatherRepositoryImpl
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.data.local.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepositoryImpl,
    private val firestoreUseCases: FirestoreUseCases,
    private val preference: Preference
) : ViewModel(){
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state
    init {
        viewModelScope.launch {
            val data = firestoreUseCases.getStatus("families")
            when(data){
                is Resource.Failure -> {

                }
                Resource.Loading -> {

                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            ffLocations = data.data
                        )
                    }
                }
            }

        }
    }
    val onEvent: (event : HomeEvent) -> Unit = {event ->
            viewModelScope.launch {
                when(event){
                    is HomeEvent.GetLocationWeatherInfo -> {
                        val result = weatherRepository.getCurrentLocationKey(event.latInfo,event.logInfo,"8c0b030035f977d2af594c1332309eb1")
                        _state.update {
                            it.copy(
                                locationInfo = result
                            )
                        }
                        val updateWeather = firestoreUseCases.updateStatus(data = UpdateStatusRequestResponse(
                            id = preference.tableName?:"common",
                            temperature = ((result.main?.temp?:273.15) -  273.15).toInt().toString(),
                            address = result.name,
                            country = result.sys.country,
                            date = result.dt.toString(),
                            weather = result.weather.first().main,
                            group = "families"
                        ))
                        when(updateWeather){
                            is Resource.Failure -> {

                            }
                            Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
                                println(result)
                            }
                        }

                    }
                }
            }
    }

}
package com.chetan.ff.presentation.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.ff.data.model.Resource
import com.chetan.ff.data.repositoryImpl.WeatherRepositoryImpl
import com.chetan.ff.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepositoryImpl
) : ViewModel(){
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state
    init {
        viewModelScope.launch {
            val result = weatherRepository.getCurrentLocationKey("27.718063","85.336949","8c0b030035f977d2af594c1332309eb1")
            _state.update {
                it.copy(
                    locationKey = result
                )
            }
        }
    }
    val onEvent: (event : HomeEvent) -> Unit = {event ->
            viewModelScope.launch {
                when(event){
                    is HomeEvent.GetLocationWeather -> {
//                        val result = weatherRepository.getCurrentWeatherCondition(state.value.locationKey,"ScPI1OI98Mk3J9cmvwmAdlEi9p1zMyLS")
//                        when(result){
//                            is Resource.Failure -> {}
//                            Resource.Loading -> {
//
//                            }
//                            is Resource.Success -> {
//                                println("+====================++++++++++++++++++++")
//
//                            }
//                        }
                    }
                }
            }
    }

}
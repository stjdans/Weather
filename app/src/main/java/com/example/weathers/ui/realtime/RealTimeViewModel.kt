package com.example.weathers.ui.realtime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathers.data.WeatherRepository
import com.example.weathers.data.model.Weather
import com.example.weathers.data.source.local.Location
import com.example.weathers.data.source.sensor.LocationProvider
import com.example.weathers.data.source.sensor.getMapXY
import com.example.weathers.util.LoadTask
import com.example.weathers.util.distanceTo
import com.example.weathers.util.toLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RealTimeUiState(
    val infoMessage: String? = null,
    val weatherItems: List<Weather> = emptyList(),
    val searchItems: List<Location> = emptyList()
)

@HiltViewModel
class RealTimeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _queryText = MutableStateFlow("")

    private val _searchItems = _queryText.map(weatherRepository::getLocationsByAdress)
    private val _gpsLocation = locationProvider.data
    private val _settingLocations = weatherRepository.getUserLocationStream().map(weatherRepository::getLocationsByCode)
    private val _task: Flow<LoadTask<List<Weather>>> = combine(_gpsLocation, _settingLocations) { gpsLocation, settingLocations ->
        mutableListOf(getCurrentPositionLocation(gpsLocation)).apply { addAll(settingLocations) }
            .map { weatherRepository.getRealTimeWeather(it) }
            .let { LoadTask.Success(it) }
    }.catch {
        LoadTask.Error(it.message ?: "")
    }

    val uiState =
        combine(_searchItems, _task) { searchs, task ->
            when (task) {
                is LoadTask.Error -> {
                    RealTimeUiState(infoMessage = task.errorMessage)
                }
                is LoadTask.Success -> {
                    RealTimeUiState(
                        searchItems = searchs,
                        weatherItems = task.data,
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RealTimeUiState()
        )

    fun search(query: String) {
        _queryText.update { query }
    }

    fun setUserLocatin(code: String) {
        viewModelScope.launch {
            weatherRepository.saveUserLocation(code)
        }
    }

    fun deleteUserLocatin(code: String) {
        viewModelScope.launch {
            weatherRepository.deleteUserLocation(code)
        }
    }

    private suspend fun getCurrentPositionLocation(current: android.location.Location?): Location {
        if (current == null) return Location()

        val (x, y) = current.getMapXY()
        val locationList = weatherRepository.getLocations(x, y)
        val curLatlng = current.toLatLng()
        return locationList.minBy { curLatlng.distanceTo(it.latLng) }
    }
}
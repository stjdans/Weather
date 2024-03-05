package com.example.weathers.ui.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathers.data.WeatherRepository
import com.example.weathers.data.model.Weather
import com.example.weathers.data.model.WeatherStaus
import com.example.weathers.data.source.local.Location
import com.example.weathers.data.source.sensor.LocationProvider
import com.example.weathers.data.source.sensor.getDiff
import com.example.weathers.data.source.sensor.getMapXY
import com.example.weathers.util.distanceTo
import com.example.weathers.util.toLatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ForecastUiState(
    val weatherItems: List<Weather> = emptyList()
)

class ForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _gpsLocation = locationProvider.data
    private val _settingLocations = weatherRepository.getUserLocationStream().map(weatherRepository::getLocationsByCode)
    private val _weathers = combine(_gpsLocation, _settingLocations) { gpsLocation, settingLocations ->
        mutableListOf(getCurrentPositionLocation(gpsLocation))
            .apply { addAll(settingLocations) }
            .map { weatherRepository.getForecastWeather(it) }
    }

    val uiState = _weathers.map {
        ForecastUiState(
            weatherItems = it
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ForecastUiState()
    )

    private suspend fun getCurrentPositionLocation(current: android.location.Location?): Location {
        if (current == null) return Location()

        val (x, y) = current.getMapXY()
        val locationList = weatherRepository.getLocations(x, y)
        val curLatlng = current.toLatLng()
        return locationList.minBy { curLatlng.distanceTo(it.latLng) }
    }
}
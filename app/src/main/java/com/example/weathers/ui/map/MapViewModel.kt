package com.example.weathers.ui.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathers.data.WeatherRepository
import com.example.weathers.data.source.local.Location
import com.example.weathers.data.source.sensor.LocationProvider
import com.example.weathers.data.source.sensor.getMapXY
import com.example.weathers.data.model.Weather
import com.example.weathers.util.LoadTask
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.LinkedList
import javax.inject.Inject

data class MapUiState(
    val infoMessage: String? = null,
    val weather: Weather? = null,
)

class MapViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider,
    private var googleMapManager: GoogleMapManager
) : ViewModel() {

    val mapState by lazy { googleMapManager.data.distinctUntilChanged() }

    val uiState = mapState.filter { !it.move }
        .map { filterLocation(it.position, it.mapLevel, it.displayGap) }
        .flatMapMerge(transform = weatherRepository::getRealTimeWeathers)
        .map { handleWeather(it) }
        .catch { emit(LoadTask.Error(it.message ?: "")) }
        .map { task ->
            when (task) {
                is LoadTask.Error -> {
                    MapUiState(infoMessage = task.errorMessage)
                }

                is LoadTask.Success -> {
                    MapUiState(weather = task.data)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MapUiState()
        )

    fun setUp(googleMap: GoogleMap) {
        googleMapManager.setUp(googleMap)
    }

    fun addMarker(context: Context, weather: Weather) {
        googleMapManager.addMarker(context, weather)
    }

    private suspend fun filterLocation(center: LatLng, level: Int, gap: Int): List<Location> {
        val (x, y) = center.getMapXY()
        val locs = when (level) {
            1 -> weatherRepository.getLocations().filter { it.level != 3 }
            2 -> weatherRepository.getLocations(x, y, 5).filter { it.level != 1 }
            else -> weatherRepository.getLocations(x, y, 1).filter { it.level == 3 }
        }.sortedWith { o1, o2 -> if (o1.distanceTo(center) > o2.distanceTo(center)) 1 else -1 }

        val list = LinkedList(locs)
        val filterList = mutableListOf<Location>()

        while (list.isNotEmpty()) {
            val p = list.poll()
            filterList.add(p)
            for (i in list.size - 1 downTo 0 step 1) {
                val distance = p.distanceTo(list[i])
                if (distance < gap)
                    list.removeAt(i)
            }
        }

        return filterList
    }

    private fun handleWeather(weather: Weather): LoadTask<Weather> {
        return if (weather.code.isEmpty())
            LoadTask.Error("location 정보가 없습니다.")
        else
            LoadTask.Success(weather)
    }

    fun infoMessageShown() {

    }

    override fun onCleared() {
        super.onCleared()
        googleMapManager.destroy()
    }
}
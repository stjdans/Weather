package com.example.weathers

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathers.data.source.sensor.LocationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


sealed interface MainUiState {
    data object Splash : MainUiState
    data class Home(val permission: Boolean = true, val gps: Boolean = true): MainUiState
}

class MainViewModel @Inject constructor(
    private val locationProvider: LocationProvider,
    private val dataStore: DataStore<Settings>
) : ViewModel() {

    private var completeSplash = MutableStateFlow(false)
    private val allowPermission = MutableStateFlow(false)
    var requestPermission: Boolean
        get() = runBlocking { dataStore.data.first().requestLocationPermission }
        set(value) { runBlocking { dataStore.updateData { it.toBuilder().setRequestLocationPermission(value).build() } } }

    val uiState = combine(completeSplash, allowPermission, locationProvider.enableGps) { completeSplash, permission, gps ->
        if (completeSplash) MainUiState.Home(permission, gps) else MainUiState.Splash
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = MainUiState.Splash
    )

    fun updateAllowPermission(allow: Boolean) {
        allowPermission.value = allow
    }

    fun updateState(complete: Boolean) {
        completeSplash.value = complete
    }

    fun startLocationUpdate() {
        locationProvider.startLocationUpdates()
    }

    fun stopLocationUpdate() {
        locationProvider.stopLocationUpdates()
    }
}
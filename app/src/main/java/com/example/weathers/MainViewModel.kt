package com.example.weathers

import androidx.lifecycle.ViewModel
import com.example.weathers.data.source.sensor.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val locationProvider: LocationProvider
) : ViewModel() {

    fun startLocationUpdate() {
        locationProvider.startLocationUpdates()
    }

    fun stopLocationUpdate() {
        locationProvider.stopLocationUpdates()
    }
}
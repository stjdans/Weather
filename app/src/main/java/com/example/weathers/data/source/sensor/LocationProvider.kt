package com.example.weathers.data.source.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.CompletableFuture

class LocationProvider(context: Context, ) {
    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var _data = MutableStateFlow<Location?>(null)
    private var isUpdating = false

    private val INTERVALMILL = 60 * 1000L

    val enableGps = flow {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        emit(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        while (true) {
            delay(5000)
            emit(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        }
    }

    var data = _data.asStateFlow()

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            for (location in result.locations) {
                _data.value = location
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(): Location? {
        if (isUpdating)
            return _data.value

        val future = CompletableFuture<Location?>()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            future.complete(location)
            _data.value = location
        }

        return future.get()
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(request: LocationRequest = LocationRequest.Builder(INTERVALMILL).build()) {
        if (isUpdating)
            return

        isUpdating = true
        fusedLocationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        isUpdating = false
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}
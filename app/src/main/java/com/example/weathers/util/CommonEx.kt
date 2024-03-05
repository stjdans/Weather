package com.example.weathers.util

import com.example.weathers.data.source.local.Location
import com.example.weathers.data.source.sensor.CoordinateConverter
import com.google.android.gms.maps.model.LatLng

fun LatLng.distanceTo(latLng: LatLng): Int {
    val result = FloatArray(1)
    android.location.Location.distanceBetween(latitude, longitude, latLng.latitude, latLng.longitude, result)
    return result[0].toInt()
}

fun android.location.Location.toLatLng() = LatLng(latitude, longitude)
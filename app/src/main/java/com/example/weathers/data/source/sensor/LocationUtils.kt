package com.example.weathers.data.source.sensor

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import kotlin.math.abs


// 날씨 API X, Y 좌표
fun Location.getMapXY() = CoordinateConverter.lamprojToMap(longitude.toFloat(), latitude.toFloat())
fun LatLng.getMapXY() = CoordinateConverter.lamprojToMap(longitude.toFloat(), latitude.toFloat())

// 위, 경도 차이
fun Location.getDiff(mapLocation: com.example.weathers.data.source.local.Location) =
    abs(mapLocation.longitude4_100 - longitude) + abs(mapLocation.latitude4_100 - latitude)


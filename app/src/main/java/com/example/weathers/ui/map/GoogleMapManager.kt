package com.example.weathers.ui.map

import android.annotation.SuppressLint
import android.content.Context
import com.example.weathers.di.ApplicationScope
import com.example.weathers.data.model.Weather
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class MapState(
    val move: Boolean = false,
    val displayGap: Int = 0,
    val mapLevel: Int = 1,
    val position: LatLng = GoogleMapManager.DEFAULT_POSITON
)

@SuppressLint("MissingPermission")
class GoogleMapManager @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope
) {
    companion object {
        val DEFAULT_POSITON: LatLng get() = LatLng(37.504082, 127.110880)
        val DEFAULT_LEVEL: Float get() = 12f
    }

    private var googleMap: GoogleMap? = null
    private val markerMap: MutableMap<Int, MutableMap<String, Marker>> = mutableMapOf(
        1 to mutableMapOf(),
        2 to mutableMapOf(),
        3 to mutableMapOf()
    )
    private val displayStrategy = DefaultMarkerDisplayStrategy()

    private val _move = MutableStateFlow(false)
    private val _zoomLevel = MutableStateFlow(DEFAULT_LEVEL.toInt())
    private val _position = MutableStateFlow(DEFAULT_POSITON)

    private val _mapLevel: Int
        get() = displayStrategy.getMapLevel(_zoomLevel.value)

    private val _displayGap: Int
        get() = displayStrategy.getDisplayGap(_zoomLevel.value)

    val data = combine(_move, _position) { move, position ->
        MapState(
            move = move,
            mapLevel = _mapLevel,
            position = position,
            displayGap = _displayGap
        )
    }

    fun setUp(googleMap: GoogleMap) {
        this.googleMap = googleMap
        init()
    }

    private fun init() {
        googleMap?.apply {
            setOnMapLoadedCallback {
                println("MapLoaded")
                animateCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_POSITON, DEFAULT_LEVEL))
                setMinZoomPreference(7.0f)
                setMaxZoomPreference(16.0f)
                isMyLocationEnabled = true;

                // 경계 설정
                //            val australiaBounds = LatLngBounds(
                //                LatLng((-44.0), 113.0),  // SW bounds
                //                LatLng((-10.0), 154.0) // NE bounds
                //            )
                //            map.moveCamera(CameraUpdateFactory.newLatLngBounds(australiaBounds, 0))

                //            // Create a LatLngBounds that includes the city of Adelaide in Australia.
                //            val adelaideBounds = LatLngBounds(
                //                LatLng(-35.0, 138.58),  // SW bounds
                //                LatLng(-34.9, 138.61) // NE bounds
                //            )
                //
                //// Constrain the camera target to the Adelaide bounds.
                //            map.setLatLngBoundsForCameraTarget(adelaideBounds)
            }

            setOnCameraMoveStartedListener {
                _move.value = true
            }

            setOnCameraMoveListener {
                if (_move.value) {
                    _zoomLevel.value = cameraPosition.zoom.toInt()
                    _position.value = cameraPosition.target
                    println("zoom = ${_zoomLevel.value}")
                }
            }

            setOnCameraIdleListener {
                _move.value = false
                chageMarkerVisible(_zoomLevel.value, markerMap)
            }
        }
    }

    fun addMarker(context: Context, weather: Weather) {
        val code = weather.code
        val mapLevel = weather.level
        var marker = markerMap.get(mapLevel)!![code]
        val prevWeather = marker?.tag as? Weather
        if (marker == null) {
            marker = googleMap?.addMarker(
                MarkerOptions()
                    .position(weather.latLng)
                    .visible(false)
            )

            if (marker == null) return

            markerMap[mapLevel] = markerMap.getOrDefault(mapLevel, mutableMapOf()).apply { this[code] = marker }
        }

        if (prevWeather == null || prevWeather != weather) {
            scope.launch {
                val descriptor = BitmapDescriptorFactory.fromBitmap(displayStrategy.getBitmap(context, weather))
                withContext(Dispatchers.Main) {
                    marker.setIcon(descriptor)
                    marker.tag = weather
                    marker.isVisible = true
                }
            }
        }
    }

    private fun chageMarkerVisible(zoomLevel: Int, markerMap: Map<Int, Map<String, Marker>>) {
        scope.launch(Dispatchers.Main) {
            val (v1, v2) = displayStrategy.getVisibleList(zoomLevel, markerMap)
            withContext(Dispatchers.Main) {
                v1.forEach { it.isVisible = true }
                v2.forEach { it.isVisible = false }
            }
        }
    }

    fun destroy() {
        markerMap.values.map { it.values }.flatten().forEach { it.remove() }
        markerMap.clear()
        googleMap = null
        scope.cancel()
    }
}

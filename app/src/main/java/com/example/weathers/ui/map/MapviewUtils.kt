package com.example.weathers.ui.map

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.MapView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

@Composable
fun rememberMapviewWithLifecyle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(key1 = mapView, key2 = lifecycle) {
        val observer = getMapviewLifecycleObserver(mapView)
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}

@Composable
fun rememberGoogleMapManager(): GoogleMapManager {
    val context = LocalContext.current
    val googleMapmanager = remember { GoogleMapManager(CoroutineScope(Job() + Dispatchers.IO)) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(key1 = googleMapmanager, key2 = lifecycle) {
        val observer = getMapManagerLifecycleObserver(googleMapmanager)
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return googleMapmanager
}

private fun getMapviewLifecycleObserver(mapView: MapView): LifecycleEventObserver {
    return LifecycleEventObserver { owner, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
        }
    }
}

private fun getMapManagerLifecycleObserver(mapManager: GoogleMapManager): LifecycleEventObserver {
    return LifecycleEventObserver { owner, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE,
            Lifecycle.Event.ON_START,
            Lifecycle.Event.ON_RESUME,
            Lifecycle.Event.ON_STOP,
            Lifecycle.Event.ON_PAUSE -> { }
            Lifecycle.Event.ON_DESTROY -> mapManager.destroy()
            else -> throw IllegalStateException()
        }
    }
}
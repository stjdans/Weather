package com.example.weathers.ui.map

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weathers.data.model.Weather
import com.google.android.gms.maps.MapView
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mapView = rememberMapviewWithLifecyle()
    val context = LocalContext.current

    uiState.weather?.let { viewModel.addMarker(context, it) }
    LaunchedEffect(key1 = mapView) {
        viewModel.setUp(mapView.awaitMap())
    }

    MapContainer(mapView = mapView)
}

@Composable
fun MapContainer(
    mapView: MapView,
) {
    AndroidView(factory = { mapView }) { map ->
        println("invalidate....")
    }
}
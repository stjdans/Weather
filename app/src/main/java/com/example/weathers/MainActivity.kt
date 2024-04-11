package com.example.weathers

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weathers.ui.main.MainScreen
import com.example.weathers.ui.theme.WeathersTheme
import com.example.weathers.util.LOCATION_PERMISSIONS
import com.example.weathers.util.checkLocationPermission
import com.example.weathers.util.goToDeviceLocation
import com.example.weathers.util.goToDeviceSettings
import com.example.weathers.util.setUpLocationContract
import com.example.weathers.util.showRequestPermissionRationalesAny
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


data class MainUiState(
    val allowedLocation: Boolean = false,
    val enableGps: Boolean = false
)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var locationContracts: ActivityResultLauncher<Array<String>>
    private val allowLocationPermission = MutableStateFlow(false)

    private val enableGps = flow {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        emit(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        while (true) {
            delay(5000)
            emit(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        }
    }

    private val uiState = combine(allowLocationPermission, enableGps) { permission, gps ->
        MainUiState(permission, gps)
    }.stateIn(
        scope = lifecycleScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = MainUiState()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeathersTheme {
                MainScreen(
                    uiState = uiState,
                    onPermissionRequestClick = ::handleNotAllowLocationPermission,
                    onDeviceSettingClick = ::goToDeviceLocation
                )
            }
        }

        locationContracts = setUpLocationContract(onGranted = ::updateLocationPermissionState)
    }

    override fun onResume() {
        super.onResume()
        updateLocationPermissionState()
        viewModel.startLocationUpdate()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLocationUpdate()
    }

    private fun handleNotAllowLocationPermission() {
        when {
            showRequestPermissionRationalesAny(LOCATION_PERMISSIONS) -> locationContracts.launch(LOCATION_PERMISSIONS)
            else -> goToDeviceSettings()
        }
    }

    private fun updateLocationPermissionState() {
        allowLocationPermission.value = checkLocationPermission()
    }
}
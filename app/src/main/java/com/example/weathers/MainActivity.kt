package com.example.weathers

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weathers.ui.main.MainScreen
import com.example.weathers.ui.splash.SplashScreen
import com.example.weathers.ui.theme.WeathersTheme
import com.example.weathers.util.LOCATION_PERMISSIONS
import com.example.weathers.util.checkLocationPermission
import com.example.weathers.util.checkLocationPermissions
import com.example.weathers.util.goToDeviceLocation
import com.example.weathers.util.goToDeviceSettings
import com.example.weathers.util.setUpLocationContract
import com.example.weathers.util.showRequestPermissionRationalesAny
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var splashContracts: ActivityResultLauncher<Array<String>>
    private lateinit var mainContracts: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashContracts = setUpLocationContract(
            onGranted = ::updateState,
            onDenied = ::updateState
        )

        mainContracts = setUpLocationContract(
            onGranted = ::updateLocationPermissionState
        )

        setContent {
            WeathersTheme {
                WeatherApp(
                    viewModel = viewModel,
                    onSplashComplete = ::checkPermission,
                    onPermissionRequestClick = ::handleNotAllowLocationPermission,
                    onDeviceSettingClick = ::goToDeviceLocation
                )
            }
        }
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

    private fun checkPermission() {
        checkLocationPermissions(
            onGranted = ::updateState,
            onRationnale = ::requestLocationPermission,
            onDenied = ::requestLocationPermission
        )
    }

    private fun requestLocationPermission() {
        if (!viewModel.requestPermission) {
            viewModel.requestPermission = true
        }

        splashContracts.launch(LOCATION_PERMISSIONS)
    }

    private fun handleNotAllowLocationPermission() {
        when {
            showRequestPermissionRationalesAny(LOCATION_PERMISSIONS) -> mainContracts.launch(LOCATION_PERMISSIONS)
            else -> goToDeviceSettings()
        }
    }

    private fun updateLocationPermissionState() {
        viewModel.updateAllowPermission(checkLocationPermission())
    }

    private fun updateState() {
        viewModel.updateState(true)
    }
}

@Composable
private fun WeatherApp(
    viewModel: MainViewModel,
    onSplashComplete: () -> Unit,
    onPermissionRequestClick: () -> Unit,
    onDeviceSettingClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (state) {
        MainUiState.Splash -> {
            SplashScreen(onSplashComplete = onSplashComplete)
        }
        is MainUiState.Home -> {
            MainScreen(
                state = state as MainUiState.Home,
                onPermissionRequestClick = onPermissionRequestClick,
                onDeviceSettingClick = onDeviceSettingClick
            )
        }
    }
}


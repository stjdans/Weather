package com.example.weathers

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weathers.component.BaseActivity
import com.example.weathers.databinding.ActivityMainBinding
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
import kotlinx.coroutines.launch
import javax.inject.Inject


data class MainUiState(
    val allowedLocation: Boolean = false,
    val enableGps: Boolean = false
)

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

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
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        locationContracts = setUpLocationContract(onGranted = ::updateLocationPermissionState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                uiState.collect { state ->
                    when {
                        !state.allowedLocation -> showErrorMessage(
                            id = R.string.error_location_permission,
                            onClick = this@MainActivity::handleNotAllowLocationPermission
                        )

                        !state.enableGps -> showErrorMessage(
                            id = R.string.error_gps,
                            onClick = this@MainActivity::goToDeviceLocation
                        )

                        else -> hideErrorMessage()
                    }
                }
            }
        }
    }

    private fun handleNotAllowLocationPermission() {
        when {
            showRequestPermissionRationalesAny(LOCATION_PERMISSIONS) -> locationContracts.launch(LOCATION_PERMISSIONS)
            else -> goToDeviceSettings()
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

    private fun showErrorMessage(id: Int, onClick: () -> Unit = {}) {
        binding.infoMessage.apply {
            text = getString(id)
            visibility = View.VISIBLE
            setOnClickListener { onClick() }
        }
    }

    private fun hideErrorMessage() {
        binding.infoMessage.run {
            text = ""
            visibility = View.GONE
            setOnClickListener(null)
        }
    }

    private fun updateLocationPermissionState() {
        allowLocationPermission.value = checkLocationPermission()
    }
}
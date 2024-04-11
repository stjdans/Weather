package com.example.weathers

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.datastore.core.DataStore
import com.example.weathers.ui.splash.SplashScreen
import com.example.weathers.ui.theme.WeathersTheme
import com.example.weathers.util.LOCATION_PERMISSIONS
import com.example.weathers.util.checkLocationPermissions
import com.example.weathers.util.setUpLocationContract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var dataStore: DataStore<Settings>

    private lateinit var locationContracts: ActivityResultLauncher<Array<String>>
    private var isRequestLocationPermission: Boolean = false

    private val ANIMAITION_DELAY = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeathersTheme {
                LaunchedEffect(key1 = Unit) {
                    delay(ANIMAITION_DELAY)
                    checkLocationPermissions(
                        onGranted = ::requestMainUi,
                        onRationnale = ::requestLocationPermission,
                        onDenied = ::requestLocationPermission)
                }
                SplashScreen()
            }
        }

        locationContracts = setUpLocationContract(
            onGranted = ::requestMainUi,
            onDenied = ::requestMainUi
        )

        isRequestLocationPermission = runBlocking { dataStore.data.first().requestLocationPermission }

    }

    private fun requestMainUi() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun requestLocationPermission() {
        if(!isRequestLocationPermission) {
            isRequestLocationPermission = true
            runBlocking { dataStore.updateData { it.toBuilder().setRequestLocationPermission(true).build() } }
        }

        locationContracts.launch(LOCATION_PERMISSIONS)
    }
}
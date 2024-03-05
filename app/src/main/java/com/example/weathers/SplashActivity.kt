package com.example.weathers

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import com.example.weathers.databinding.ActivitySplashBinding
import com.example.weathers.util.LOCATION_PERMISSIONS
import com.example.weathers.util.checkLocationPermissions
import com.example.weathers.util.setUpLocationContract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var dataStore: DataStore<Settings>

    private lateinit var binding: ActivitySplashBinding
    private lateinit var locationContracts: ActivityResultLauncher<Array<String>>
    private var isRequestLocationPermission: Boolean = false

    private val ANIMAITION_DELAY = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationContracts = setUpLocationContract(
            onGranted = ::requestMainUi,
            onDenied = ::requestMainUi
        )

        isRequestLocationPermission = runBlocking { dataStore.data.first().requestLocationPermission }

        setUpAnimation()

        MainScope().launch {
            delay(ANIMAITION_DELAY)
            checkLocationPermissions(
                onGranted = ::requestMainUi,
                onRationnale = ::requestLocationPermission,
                onDenied = ::requestLocationPermission)
        }
    }

    private fun setUpAnimation() {
        val animation1 = AnimationUtils.loadAnimation(this, R.anim.splash1)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.splash2)
        binding.centerSun.startAnimation(animation2)
        binding.centerCloud.startAnimation(animation1)
        binding.centerSnow.startAnimation(animation1)
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
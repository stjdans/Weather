package com.example.weathers.component

import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.example.weathers.databinding.ActivityMainBinding
import com.example.weathers.di.ServiceModule
import com.example.weathers.di.ServiceModule_ProvideLocationProviderFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

open class BaseActivity<T: ViewBinding> : AppCompatActivity() {

    private var _vb: T? = null

    protected var binding
        get() = _vb!!
        set(value) {
            _vb = value
        }
    override fun onDestroy() {
        super.onDestroy()
        _vb = null
    }
}
package com.example.weathers

import android.app.Application
import com.example.weathers.data.source.local.Location
import com.example.weathers.data.source.local.WeatherDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application()
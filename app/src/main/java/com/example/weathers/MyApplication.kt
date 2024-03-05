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
open class MyApplication : Application() {

    @Inject
    lateinit var weatherDatabase: WeatherDatabase

    override fun onCreate() {
        super.onCreate()
        getCVS(this, weatherDatabase)
    }
}

fun getCVS(application: Application, weatherDatabase: WeatherDatabase) {
    CoroutineScope(Dispatchers.IO).launch {
        var list = weatherDatabase.locationDao().getAll()
        if (list.isNotEmpty())
            return@launch

        application.assets.open("location_20210401.csv").use {
            val locations = it.bufferedReader().readLines().map(::toLocation)
            weatherDatabase.locationDao().inserts(*locations.toTypedArray())
        }

        list = weatherDatabase.locationDao().getAll()
        println(list.size)
    }
}

fun toLocation(cvs: String): Location {
    val split = cvs.split(",")
    val level = when {
        split[2].isEmpty() && split[3].isEmpty() -> 1
        split[3].isEmpty() -> 2
        else -> 3
    }
    return Location(
        split[0], split[1], split[2], split[3], split[4].toInt(),
        split[5].toInt(), split[6].toInt(), split[7].toInt(), split[8].toFloat(), split[9].toInt(),
        split[10].toInt(), split[11].toFloat(), split[12].toDouble(), split[13].toDouble(),
        level = level
    )
}
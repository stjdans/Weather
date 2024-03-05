package com.example.weathers.data

import androidx.datastore.core.DataStore
import com.example.weathers.Settings
import com.example.weathers.data.source.local.Location
import com.example.weathers.data.source.local.LocationDao
import com.example.weathers.data.source.local.WeatherDao
import com.example.weathers.data.model.Weather
import com.example.weathers.data.source.network.NetworkDataSource
import com.example.weathers.di.ApplicationScope
import com.example.weathers.di.IoDispatcher
import com.example.weathers.data.model.WeatherStaus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class DefaultWeatherRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val locationDao: LocationDao,
    private val weatherDao: WeatherDao,
    private val dataStore: DataStore<Settings>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : WeatherRepository {

    var ayncJob: Job? = null

    override suspend fun getRealTimeWeather(location: Location): Weather {
        if (location.code.isEmpty()) return Weather(status = WeatherStaus.Fail)

        return withContext(dispatcher) {

            val realTime = weatherDao.getRealTimeByCode(location.code)
            val (date, time) = Calendar.getInstance().rtBaseTime()
            if (realTime != null && realTime.equalBase(date, time))
                realTime.toWeather(location)
            else {
                val weather = networkDataSource.getRealTimeWeather(location)
                if (weather.status == WeatherStaus.Success)
                    weatherDao.upserts(weather.toRealTime())

                weather
            }
        }
    }

    override fun getRealTimeWeathers(locations: List<Location>): Flow<Weather> {
        ayncJob?.cancel()
        var f: Flow<Weather>? = null
        ayncJob = scope.launch {
            f = flow() {
                val (date, time) = Calendar.getInstance().rtBaseTime()
                for (location in locations) {
                    val realTime = weatherDao.getRealTimeByCode(location.code)
                    if (realTime != null && realTime.equalBase(date, time))
                        emit(realTime.toWeather(location))
                    else {
                        val weather = networkDataSource.getRealTimeWeather(location)
                        if (weather.status == WeatherStaus.Success)
                            weatherDao.upserts(weather.toRealTime())

                        emit(weather)
                    }
                }
            }.flowOn(dispatcher).also { f = it }
        }

        while (f == null) {
            Thread.sleep(100)
        }

        return f!!
    }

    override suspend fun getForecastWeather(location: Location): Weather {
        if (location.code.isEmpty()) return Weather(status = WeatherStaus.Fail)

        return withContext(dispatcher) {
            val forecast = weatherDao.getUltraForecastByCode(location.code)
            val (date, time) = Calendar.getInstance().ufctBaseTime()
            if (forecast != null && forecast.equalBase(date, time))
                forecast.toWeather(location)
            else {
                val weather = networkDataSource.getForecastWeather(location)
                if (weather.status == WeatherStaus.Success)
                    weatherDao.upserts(weather.toUltraForecast())
                weather
            }
        }
    }

    override suspend fun getLocations(): List<Location> {
        return withContext(dispatcher) {
            locationDao.getAll()
        }
    }

    override suspend fun getLocationsByAdress(adress: String): List<Location> {
        return withContext(dispatcher) {
            locationDao.getLocationsByAdress(adress)
        }
    }

    override suspend fun getLocationsByCode(code: List<String>): List<Location> {
        return withContext(dispatcher) {
            locationDao.getLocationsByCode(code)
        }
    }

    override suspend fun getLocations(x: Int, y: Int): List<Location> {
        return withContext(dispatcher) {
            locationDao.getLocations(x, y)
        }
    }

    override suspend fun getLocations(level: Int): List<Location> {
        return withContext(dispatcher) {
            locationDao.getLocations(level)
        }
    }

    override suspend fun getLocations(x: Int, y: Int, distance: Int): List<Location> {
        return withContext(dispatcher) {
            println("getLocations distance = $distance")
            locationDao.getLocations(x, y, distance)
        }
    }

    override fun getUserLocationStream(): Flow<List<String>> = dataStore.data.map { settings -> settings.userLocationsList }

    override suspend fun saveUserLocation(code: String) {
        dataStore.updateData { currentSetting ->
            currentSetting.toBuilder().run {
                if (userLocationsList.none { it == code })
                    addUserLocations(code).build()
                else
                    currentSetting
            }
        }
    }

    override suspend fun deleteUserLocation(code: String) {
        dataStore.updateData { currentSetting ->
            currentSetting.toBuilder().run {
                val newList = userLocationsList.filter { code != it }
                clearUserLocations().addAllUserLocations(newList).build()
            }
        }
    }

    override fun isRequestLocationPermission(): Flow<Boolean> = dataStore.data.map { it.requestLocationPermission }
}
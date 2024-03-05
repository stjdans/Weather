package com.example.weathers.data

import com.example.weathers.data.source.local.Location
import com.example.weathers.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getRealTimeWeather(location: Location) : Weather
    fun getRealTimeWeathers(locations: List<Location>) : Flow<Weather>

    suspend fun getForecastWeather(location: Location) : Weather

    suspend fun getLocations(): List<Location>
    suspend fun getLocationsByAdress(adress: String): List<Location>

    suspend fun getLocationsByCode(code: List<String>): List<Location>
    suspend fun getLocations(x: Int, y: Int): List<Location>
    suspend fun getLocations(level: Int): List<Location>
    suspend fun getLocations(x: Int, y: Int, distance: Int): List<Location>

    // datastore
    fun getUserLocationStream(): Flow<List<String>>
    suspend fun saveUserLocation(code: String)
    suspend fun deleteUserLocation(code: String)

    fun isRequestLocationPermission(): Flow<Boolean>


}


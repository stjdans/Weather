package com.example.weathers.data.source.network

import com.example.weathers.data.source.local.Location
import com.example.weathers.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource {
    suspend fun getRealTimeWeather(location: Location): Weather
//    suspend fun getRealTimeWeather(locations: List<Location>): Flow<Weather>

    suspend fun getForecastWeather(location: Location) : Weather
}
